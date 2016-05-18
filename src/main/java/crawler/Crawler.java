package crawler;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.ardoc.Parser;
import org.ardoc.Result;
import org.ardoc.UnknownCombinationException;
import review.ArDocClassification;
import review.Review;
import server.DBWriter;

import static java.lang.Thread.sleep;


public  class Crawler implements Constants {
    private Date lastCrawlDate;
    private GooglePlayStoreCrawler googlePlayStoreCrawler;
    private DBWriter dbWriter;
    private static Crawler uniqueInstance;

    private Crawler() {
        googlePlayStoreCrawler = new GooglePlayStoreCrawler();
        dbWriter = new DBWriter(DBNAME, COLLLECTIONNAME);
    }

    public static Crawler getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new Crawler();
        }
        return uniqueInstance;
    }

    public void crawlForEachAPP() throws InterruptedException {
        int [] reviewsCounts = new int[APP_INFOS.length];
        int looper = 0;
        for (final AppInfo appInfo : APP_INFOS) {
            boolean reviewsOnFirstPage = true;
            int reviewCount = 0;
            printAppCrawlStart(appInfo);

            //get Date of latest review. If not exist, give default 1970
            lastCrawlDate = dbWriter.getLatestReviewsDateOfApp(appInfo.getName());
            googlePlayStoreCrawler.setAppInfo(appInfo, lastCrawlDate);
            sleep(2000);

            //get reviews of first page
            reviewCount = writeCurrentReviewsToDB(reviewCount, appInfo);

            while(googlePlayStoreCrawler.crawlNextSite() && reviewCount <= REVIEWSPERAPPLIMIT){
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
        System.out.println(" ************** Start ReviewCrawler for App " + appInfo.getName()+" **************");
    }

    private int writeCurrentReviewsToDB(int reviewCount, AppInfo appInfo) {
        List<Review> reviewsOfPage = googlePlayStoreCrawler.getReviewsOfPage();
        int counter = reviewCount;
        ArDocDecider arDocDecider = ArDocDecider.getInstance();
        if(reviewsOfPage.size() > 0){
            counter = counter + reviewsOfPage.size();
            reviewsOfPage = arDocDecider.setArDocAndAppName(reviewsOfPage, appInfo.getName());
            dbWriter.writeReviewsToDB(reviewsOfPage);
        }
        return counter;
    }

    private void printAppCrawlEnd(int[] reviewsCounts) {
        System.out.println("===================================================================================================" +
                "===============================================================================================================");
        for(int i = 0; i < reviewsCounts.length ; i++){
            if(i % 4 == 0){
                System.out.println("\n");
            }

            System.out.print(APP_INFOS[i].getName() + ", new Reviews: " + reviewsCounts[i] + " || ");


        }
        System.out.println("\n=================================================================================================" +
                "=================================================================================================================");
    }

    //TODO: remove, just testing purpose
    private void reviewsPrintout(List<Review> reviewsOfPage) {
        for (Review review: reviewsOfPage){
            System.out.println(review.getReviewText()
                    + " // Author: " + review.getReviewAuthor()
                    + " // Date: " + review.getReviewDate().toString()
                    + " // ArDoc-Classification: " + review.getArDocClassification()
                    + " // RatingStars: " + review.getRatingStars()
                    + " // App: " + review.getReviewDate());

        }
    }
}
