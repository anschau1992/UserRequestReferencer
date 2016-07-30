# bachelor_thesis_reviewCrawler

## Introduction
This application gathers reviews for Google Play Store apps using Selenium Webdriver and stores the information into the local MongoDB.
The apps to crawl needed to be saved in a csv-File
The app gathers reviews and appInformation for the work in my Bachelor Thesis

##Setup
- Install mongodb v3.2.4 : https://docs.mongodb.com/manual/installation/
- Install Selenium extension for Safari, if prefer Safari Webdriver(recommended) before Firefox WebDriver:http://elementalselenium.com/tips/69-safari

## csv-file
The requested apps have to be written down in a .csv-file, which path has to be given as an argument for the application.

The csv-File must have the following format:
'app-Name;app-Category; link-ID*; source-code-ID'
*the string behind "id=" from the Link of a App
==> f. ex: https://play.google.com/store/apps/details?id=Draziw.Button.Mines has Link-ID: 'Draziw.Button.Mines'
There is a example-csv under ./docs/testAppInfos.csv

## Run the application
Run 'AppReviewsToDB' with arguments:
argv[0]: csv-Path (f.ex './docs/testAppInfos.csv')
argv[1]: WebBrowser ('safari' or 'firefox')


##Useful Links
[Selenium Tutorial](http://www.tutorialspoint.com/selenium/)
