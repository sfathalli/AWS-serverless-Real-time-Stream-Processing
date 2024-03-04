# EC-Group3-WS16_17

# Introduction / Challenge

In this repository you can find the result for the Assignment of the course Enterprise Computing at TU Berlin. As a Group of four our task was to rebuild an AWS serverless reference architecture for Real-time Stream Processing on either IBM Bluemix or Google Cloud Platform. Because of the work with IBM Bluemix during the exercises of the Enterprise Computing course we decided to implement our solution with it. Also there were not enough services provided by the Google Cloud Platform to fully reimplement the reference architecture (Google Cloud Function is in alpha and our whitelist request remained unanswered).

# AWS Serverless Reference Architecture: Real-time Stream Processing

![alt tag](https://cloud.githubusercontent.com/assets/23037714/22731047/97400040-ede9-11e6-8edb-bf801c778672.png)

All information can be found on this website: http://www.allthingsdistributed.com/2016/06/aws-lambda-serverless-reference-architectures.html

... and in this Git repository: https://github.com/awslabs/lambda-refarch-streamprocessing

# Result: Real-time Stream Processing on IBM Bluemix

The following picture shows a serverless architecture for Real-time Stream Processing built on IBM Bluemix: 

![Screenshot](https://cloud.githubusercontent.com/assets/19613306/22619178/44d8ca3c-eaef-11e6-92ab-2778f3623527.png)

Similar to AWS Kinesis we used IBM Message Hub to create a stream or queue, where we can push Twitter messages into. In order to do so we needed to implement an application (Java), which uses the Twitter Stream API to filter messages for specific content (hashtags). The application then pushs the filtered messages with several other information like timestamp and username into the Message Hub. 

As soon as a message/ messages arrive/s into the Message Hub, an OpenWhisk (OW) function (implemented in node.js) is triggered. OW does basically the same as AWS Lambda. By triggering the OW function, Message Hub automatically passes the message/s (JSON) in the queue to the OW fucntion. The OW function has then the task to parse the delivered messages and store them into CloudantDB, a NoSQL Storage service of IBM Bluemix. The messages are appended to the existing table of messages in CloudantDB.

By connecting CloudantDB with dashDB for Analytics, activities and transactions in CloudantDB can be tracked and monitored. DashDB, which is a SQL Cloud Storage Service of the IBM Bluemix Platform, pulls data from the CloudantDB and creates a warehouse, where the data can be analysed.

It was not possible to use the Oject Storage Service of IBM Bluemix with a trial student account. Therefore something equal to AWS S3 could not be implemented. 

In this repository you can find the implemented Java application for filtering Twitter messages for certain content and pushing those to Message Hub (folder: "twitter-to-message-hub"). Also you can find the implemented OpenWhisk function, which receives the messages from Message Hub and Stores them into Cloudant DB (folder: "OpenWhisk"). There you will find more detailed information about the application/ function.

