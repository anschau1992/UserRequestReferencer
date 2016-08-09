# URL - UserRequestLocalizer

## Introduction
As a part of my bachelor thesis I developed this prototype. The idea of the project is to link review of specific Application in the Google Play Store with the corresponding Source Code file. So a developer can faster find the location of a f.ex bugfix or error.

The first application (One_Crawling.main()) crawls reviews of listed apps and stores them into a local MongoDatabase. Afterwards it exports the review text into a csv-File, so a manual preclassification can be done. 
With the second application (Two_Linking.main()) the preclassified reviewText are imported again into the DB. The reviews will be subclassified with WEKA based on the preclassification and at last the best fitting files of the source-code will be listed for each review. The goal is to find the location in the app, where the review is referencing on it.

##Setup
- Install mongodb v3.2.4 : https://docs.mongodb.com/manual/installation/
- Install Selenium extension for Safari, if running on Mac: WebDriver:http://elementalselenium.com/tips/69-safari
- Install the standard Firefox Browser, if running on Windows: https://www.mozilla.org/en-US/firefox/new/

##Process
The requested apps have to be written down in a .csv-file, which path has to be given as an argument for the application.

The csv-File must have the following format:
##'app-Name;app-Category; link-ID*; source-code-ID'.
*the string behind "id=" from the Link of a App.
==> f. ex: https://play.google.com/store/apps/details?id=Draziw.Button.Mines has Link-ID: 'Draziw.Button.Mines'.
There is a example-csv under ./docs/testAppInfos.csv

## Run the application
- Start MongoDB with firing 'mongod' in the console. Make sure it runs on default port 27017
- Run 'AppReviewsToDB' with arguments:
- argv[0]: csv-Path (f.ex './docs/testAppInfos.csv')
- argv[1]: WebBrowser ('safari' or 'firefox')


##Useful Links
- [Selenium Tutorial](http://www.tutorialspoint.com/selenium/)
- [MongoDB Tutorial](http://www.tutorialspoint.com/mongodb/)
