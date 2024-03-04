function myAction(arg) {

    var Cloudant = require('cloudant');

    var sUsername = ""; // insert Username provided by CloudantDB Service Credentials
    var sPassword  = ""; // insert Password provided by CloudantDB Service Credentials

    console.log('Input Data: ' + JSON.stringify(arg));

    // Initialize the library with my account.
    var cloudant = Cloudant({account:sUsername, password:sPassword});

    if (cloudant) {
        console.log('CLoudant connection established');
    }

    // Specify the database we are going to use (alice)...
    var twitterDB = cloudant.db.use('twitter');

    var readTweetsDoc = function(oTweet) {

        return new Promise(function(resolve, reject) {

           twitterDB.get('tweets', { revs_info: true }, function(err, body) {

                 if (err) {
                     console.log(err);
                     reject({message: "Damn!!!Something went wrong while reading the Doc!"})
                 } else {
                     console.log('Yihaaa');
                     console.log(body);
                     resolve(body);
                 }
             })

         });
    };//End: readTweetsDoc


    var updateTweetsDoc = function(oJson) {
        return new Promise(function(resolve, reject) {

            var oUpdatedTweets = [];

              // Extract tweets from input data (message hub format)
              var aMsg = arg.messages;

              // Iterate over all messages
              for (var i = 0; i < aMsg.length; i++) {

                 var oMsgVal = JSON.parse(JSON.stringify(aMsg[i].value));
                 console.log(oMsgVal);

                 oUpdatedTweets = oJson.tweets.concat(oMsgVal.tweets);

              }

               console.log('Updated Tweets: ' + oUpdatedTweets);

               var oData = {
                   _id: "tweets",
                   _rev: oJson._rev,
                   tweets: oUpdatedTweets
               };

               twitterDB.insert(oData, function(err, body) {

                 if (err) {
                   console.log(err);
                   reject({message: "Damn!!! Check log!"})
                 } else {
                   console.log('Yihaaa');
                   console.log(body);
                   resolve({message: "Yihaaa!!!"});
                 }

             });
        });
    };//End: updateTweetsDoc


    return readTweetsDoc(arg).then(updateTweetsDoc);

}

exports.main = myAction;

/**** Example call 1 - single****
{
    "tweets": [
        {
            "Username": "test1",
            "Timestamp": "2017-02-02",
            "Message": "#Yolo LOLO!"
        }
    ]
}
*/

/**** Example call 2 - multi ****
{
    "tweets": [
        {
            "Username": "test2a",
            "Timestamp": "2017-02-02",
            "Message": "#Yolo LOLO!"
        },
        {
            "Username": "test2b",
            "Timestamp": "2017-02-02",
            "Message": "#Yolo LOLO!"
        }
    ]
}
*/
