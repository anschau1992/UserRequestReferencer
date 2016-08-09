# URL - UserRequestLocalizer

## Introduction
As a part of my bachelor thesis I developed this prototype. The idea of the project is to link review of specific Application in the Google Play Store with the corresponding Source Code file. So a developer can faster find the location of a f.ex bugfix or error.

The first application (One_Crawling.main()) crawls reviews of listed apps and stores them into a local MongoDatabase. Afterwards it exports the review text into a csv-File, so a manual preclassification can be done. 
With the second application (Two_Linking.main()) the preclassified reviewText are imported again into the DB. The reviews will be subclassified with WEKA based on the preclassification and at last the best fitting files of the source-code will be listed for each review. The goal is to find the location in the app, where the review is referencing on it.

##Setup
- Install mongodb v3.2.4 : https://docs.mongodb.com/manual/installation/
- Install Selenium extension for Safari, if running on Mac: WebDriver:http://elementalselenium.com/tips/69-safari
- Install the standard Firefox Browser, if running on Windows: https://www.mozilla.org/en-US/firefox/new/

##Process - Run the application
![alt tag](https://github.com/anschau1992/UserRequestLocalizer/blob/developer/ba_URL_process_app.png)

1. In ./docs/appInfos.csv the apps you want to crawl can be added. Make sure the csv-file always has the proper format of:  **appname;category;id;sourceCodeLink**
  - where id is the end of the Google Play Store link.
  - f.ex:  https://play.google.com/store/apps/details?id=com.duckduckgo.mobile.android&hl=en
  - ==> id: com.duckduckgo.mobile.android
  - until now it only works with sourceCode referencing to a GitHub-repository!
2. Start the local MongoDb with firing 'mongod' in the console. Make sure it runs on default port 27017
3. Run One_Crawling.main() with arguments:  **<csvPath> <browser> <mode>**
  - whereas <csvPath> is the filePath to the Appinfos, <browser> is either 'firefox' or 'safari' and <moded> is 'test' or 'prod'
  - When choosen 'test' the reviews are stored in a DB 'testreviews' otherwise in one called 'review'
4. Open the new created csv-File 'preclassification.csv'. Do Preclassification and Subclassification(optional) based on the [predefined heuristics](https://github.com/anschau1992/UserRequestLocalizer/blob/developer/docs/preclassification_heuristics.pdf) for the reviews.
  - Create for that a csv-file './docs/preclassification_with_classes' with the format:  **reviewID;reviewText;preclassification;subclassification**
  - Not every review must have a defined pre- & subclassification in the file.
5. Start Two_Linking.main() with arguments:**<preclassification> <mode>**
  - Where \<preclassification\> is either 'RESSOURCES', 'PRICING', 'PROTECTION', 'USAGE', 'COMPATIBILITY'
6. The result for the SourceCode-Linking is printed into the file 'scoring-result.txt' 
  
##Testing 

For testing the SourceCode-Linking with a self-written review, you can use the Test './src/test/codeLinking/SourceCodeLinkerTest.java and change the reviews with your own text. The result of the linking will be printed into the scoring_result.txt

##Useful Links
- [Selenium Tutorial](http://www.tutorialspoint.com/selenium/)
- [MongoDB Tutorial](http://www.tutorialspoint.com/mongodb/)
- [Weka Tool](http://www.cs.waikato.ac.nz/ml/weka/)
