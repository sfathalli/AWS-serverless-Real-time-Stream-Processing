# OpenWhisk

Figured out how to use 3rd party node modules within OpenWhisk:

    Needed to access Object Storage or CloudantDB within OpenWhisk function
  
    Solution: 

        Local development of OpenWhisk function 
  
        Define OpenWhisk function structure + node module dependencies in package.json
  
        Locally download necessary node modules
  
        Zip everything
  
        Create OpenWhisk action via OpenWhisk CLI which will upload the whole json file
  
  
Decided not to use ObjectStorage, because it is not possible to load a file and modify it within OpenWhisk function


Research how asynchronous function calls are handled within OpenWhisk:

    Problem:    Asynchronous operations as database operations etc. won’t work initially
                OpenWhisk just terminates and does not execute callbacks
    Solution:   Using Javascript ‘Promises’
                => incorporate into OpenWhisk function
            
Created Cloudant DB e.g. ‘twitter’ and document tweets

Created OpenWhisk action e.g. ‘CloudantTwitterAction’:

    The action takes 1-n tweets in json format as input
  
    It reads the current ‘tweets’-document from the Cloudant ‘twitter’-DB
  
    Updates the Tweets array within the document and writes the data back to the DB

Implement and deploy final OpenWhisk function:

    Especially setting up the connection between Message Hub and OpenWhisk
  
    Adjust the processing of the JSON input of the OpenWhisk function so that the function actually works on the JSON structure provided by Message Hub
