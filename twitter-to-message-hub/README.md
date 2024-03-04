# Twitter to Message Hub Java Application

## General

This Java application is basically a deriviation of the sample application on how to connect to Message Hub provided by IBM (https://github.com/ibm-messaging/message-hub-samples). 

The following chapters briefly describe, how to use the Twitter API for Java Applications and how to set up your Java Application in order to push messages to Message Hub. 

Mostly all of the implementations can be found in "ProducerRunnable.java".

## Twitter API (twitter4j)

In order to use the Twitter API, the twitter4j library must be installted: http://twitter4j.org/en/index.html

Keywords for this are: ConfigurationBuilder, TwitterStream, StatusListener and FilterQuery.

First thing to do is to find out your ConsumerKey, ConsumerSecret, AccessToken and AccessTokenSecret porvided by your twitter account. You need those to set up the ConfigurationBuilder.

```shell
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true);
		cb.setOAuthConsumerKey(""); //insert Consumer Key provided by your Message Hub instance credentials
		cb.setOAuthConsumerSecret(""); //insert Consumer Secret provided by your Message Hub instance credentials
		cb.setOAuthAccessToken(""); //insert Access Token provided by your Message Hub instance credentials
		cb.setOAuthAccessTokenSecret(""); //insert Access Token Secret provided by your Message Hub instance credentials
```
With the ConfigurationBuilder, a TwitterStream instance can be created. By creating a TwitterStream instance, twitter4j creates a Thread consuming the Stream. The StatusListener reacts, when certain requirements are met. There requirements can be set in the FilterQuery, for example a certain hashtag or location. Example code can be found here: http://twitter4j.org/en/code-examples.html#streaming.

## Apache Kafka

This application uses the Apache Kafka library since Message Hub is based on Apache Kafka.

There are two ways to run the application. First way is to run it locally and push messages from you desktop to the Message Hub. Therfore your credentials of the Message Hub instance in your IBM Bluemix environment have to be provided. In the Git repository provided at the beginning of this file you see, where you can find your credentials. Once you have figured out you credentials, you need to run your application with this command:

```shell
java -jar build/libs/kafka-java-console-sample-2.0.jar <kafka_brokers_sasl> <kafka_admin_url> <api_key>
```
The second way to run your application is to push it into IBM Bluemix and connect your application to IBM Message Hub via the web console. In order to connect the service to your application, you need to go to your MessageHub instance and connect your application by selecting it. You can test your application easily by implement a consumer, which reads all filtered Twitter messages sent into the Message Hub. The consumer application can then be run locally. More informations can be found on the provided Git repository at the beginning.
