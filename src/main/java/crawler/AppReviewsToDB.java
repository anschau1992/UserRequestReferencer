package crawler;


import java.io.File;
import java.util.Date;
import java.util.List;

import review.Review;

import static java.lang.Thread.sleep;


public  class AppReviewsToDB implements Constants {
    private Date lastCrawlDate;
    private GooglePlayStoreCrawler googlePlayStoreCrawler;
    private DBWriter dbWriter;
    private List<AppInfo> appInfos;


    public static void main(String[] args) throws InterruptedException {
        checkArguments(args);

        String csvPath = args[0];
        UsingBrowser browser = UsingBrowser.valueOf(args[1].toUpperCase());

        AppReviewsToDB mainAppReviewsToDB = new AppReviewsToDB(csvPath, browser);
        mainAppReviewsToDB.writeAppInfosToDB();
        mainAppReviewsToDB.crawlForEachAPP();

    }

    private static void checkArguments(String[] args) {
        if (args.length == 2) {
            File f = new File(args[0]);
            if (f.exists() && !f.isDirectory()) {
                return;
            } else {
                System.err.println("The given filepath is not correct");
                System.exit(0);
            }
        } else {
            System.err.println("The number of given argument is not correct! 1: csv-Path 2: browser");
            System.exit(0);
        }
    }


    private AppReviewsToDB(String appInfosCSV, UsingBrowser browser) {
        this.googlePlayStoreCrawler = new GooglePlayStoreCrawler(browser);
        this.dbWriter = new DBWriter(DBNAME_TEST, REVIEW_COLLLECTION_TEST, APP_INFOS_COLLLECTION_TEST);

        CSVReader csvReader = new CSVReader();
        this.appInfos = csvReader.transformToAppInfos(appInfosCSV);
    }

    public void writeAppInfosToDB() {
        dbWriter.writeAppInfosToDB(appInfos);
    }

    public void crawlForEachAPP() throws InterruptedException {
        int[] reviewsCounts = new int[appInfos.size()];
        int looper = 0;
        for (final AppInfo appInfo : appInfos) {
            int reviewCount = 0;
            printAppCrawlStart(appInfo);

            //get Date of latest review. If not exist, give default 1970
            lastCrawlDate = dbWriter.getLatestReviewsDateOfApp(appInfo.getName());
            googlePlayStoreCrawler.setAppInfo(appInfo, lastCrawlDate);
            sleep(2000);

            //get reviews of first page
            reviewCount = writeCurrentReviewsToDB(reviewCount, appInfo);

            while (googlePlayStoreCrawler.crawlNextSite() && reviewCount <= REVIEWSPERAPPLIMIT) {
                googlePlayStoreCrawler.clickNextButton();
                //TODO: find better way to avoid empty reviewwindow
//                if(googlePlayStoreCrawler.getReviewsOfPage().size() <= 0){
//                    break;
//                }
                reviewCount = writeCurrentReviewsToDB(reviewCount, appInfo);
            }

            System.out.println(appInfo.getName() + ": finished crawling. Crawled reviews: " + reviewCount);
            reviewsCounts[looper] = reviewCount;
            looper++;
            googlePlayStoreCrawler.setDateOfLastCrawlIsReached(false);
        }
        //finish print out
        printAppCrawlEnd(reviewsCounts);
    }

    private void printAppCrawlStart(AppInfo appInfo) {
        System.out.println(" ************** Start ReviewCrawler for App " + appInfo.getName() + " **************");
    }

    private int writeCurrentReviewsToDB(int reviewCount, AppInfo appInfo) {
        List<Review> reviewsOfPage = googlePlayStoreCrawler.getReviewsOfPage();
        int counter = reviewCount;
        ArDocDecider arDocDecider = ArDocDecider.getInstance();
        if (reviewsOfPage.size() > 0) {
            counter = counter + reviewsOfPage.size();
            reviewsOfPage = arDocDecider.setArDocAndAppName(reviewsOfPage, appInfo.getName());
            dbWriter.writeReviewsToDB(reviewsOfPage);
        }
        return counter;
    }

    private void printAppCrawlEnd(int[] reviewsCounts) {
        System.out.println("===================================================================================================" +
                "===============================================================================================================");
        for (int i = 0; i < reviewsCounts.length; i++) {
            if (i % 4 == 0) {
                System.out.println("\n");
            }

            System.out.print(appInfos.get(i).getName() + ", new Reviews: " + reviewsCounts[i] + " || ");


        }
        System.out.println("\n=================================================================================================" +
                "=================================================================================================================");
    }
}