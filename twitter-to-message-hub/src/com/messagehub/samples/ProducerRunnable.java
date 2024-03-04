package com.messagehub.samples;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.errors.TimeoutException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.JSONObject;

public class ProducerRunnable implements Runnable {
	private static final Logger logger = Logger.getLogger(ProducerRunnable.class);

	private final KafkaProducer<String, String> kafkaProducer;
	private final String topic;
	private volatile boolean closing = false;

	public ProducerRunnable(Properties producerProperties, String topic) {
		this.topic = topic;

		// Create a Kafka producer with the provided client configuration
		kafkaProducer = new KafkaProducer<String, String>(producerProperties);

		try {
			// Checking for topic existence.
			// If the topic does not exist, the kafkaProducer will retry for
			// about 60 secs
			// before throwing a TimeoutException
			// see configuration parameter 'metadata.fetch.timeout.ms'
			List<PartitionInfo> partitions = kafkaProducer.partitionsFor(topic);
			logger.log(Level.INFO, partitions.toString());
		} catch (TimeoutException kte) {
			logger.log(Level.ERROR, "Topic '" + topic + "' may not exist - application will terminate");
			kafkaProducer.close();
			throw new IllegalStateException("Topic '" + topic + "' may not exist - application will terminate", kte);
		}
	}

	@Override
	public void run() {
		logger.log(Level.INFO, ProducerRunnable.class.toString() + " is starting.");

		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(""); //insert Consumer Key provided by your Twitter account credentials
		cb.setOAuthConsumerSecret(""); //insert Consumer Secret provided by your Twitter account credentials
		cb.setOAuthAccessToken(""); //insert Access Token provided by your Twitter account credentials
		cb.setOAuthAccessTokenSecret(""); //insert Access Token Secret provided by your Twitter account credentials

		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

		StatusListener listener = new StatusListener() {

			@Override
			public void onException(Exception arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStatus(Status status) {
				User user = status.getUser();

				// gets Username
				String username = status.getUser().getScreenName();

				String profileLocation = user.getLocation();

				long tweetId = status.getId();
				
				String content = status.getText().replace("\n", " ").replace("\r", " ");
				
				String key = "key";
				String message = "{\"tweets\": [{\"Username\":\"" + username + "\","
						+ "\"Timestamp\":\"" + status.getCreatedAt() + "\","
								+ "\"Message\":\"" + content + "\"}]}";


				// If a partition is not specified, the client will use
				// the default partitioner to choose one.
				ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, key, message);

				// Send record asynchronously
				Future<RecordMetadata> future = kafkaProducer.send(record);

				logger.log(Level.INFO, "Message produced: " + message);
			

			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub

			}

		};
		
		
		FilterQuery fq = new FilterQuery();

		String keywords[] = { "#serverless" };

		fq.track(keywords);

		twitterStream.addListener(listener);
		twitterStream.filter(fq);

	}

	public void shutdown() {
		closing = true;
		logger.log(Level.INFO, ProducerRunnable.class.toString() + " is shutting down.");
	}
}
