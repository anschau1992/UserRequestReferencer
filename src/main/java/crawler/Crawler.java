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
    static Date lastCrawlDate;
    static String dbName = "Reviews";
    static String collectionName = "reviews";

    public  static void main(String[] args) throws InterruptedException {

        GooglePlayStoreCrawler googlePlayStoreCrawler = new GooglePlayStoreCrawler();
        DBWriter dbWriter = new DBWriter(dbName, collectionName);

        crawlForEachAPP(googlePlayStoreCrawler, dbWriter);
    }

    private static void crawlForEachAPP(GooglePlayStoreCrawler googlePlayStoreCrawler, DBWriter dbWriter) throws InterruptedException {
        int [] reviewsCounts = new int[APP_INFOS.length];
        int looper = 0;
        for (final AppInfo appInfo : APP_INFOS) {
            int reviewCount = 0;
            System.out.println(" ************** Start ReviewCrawler for App " + appInfo.getName()+" **************");

            //get Date of latest review. If not exist, give default 1970
            lastCrawlDate = dbWriter.getLatestReviewsDateOfApp(appInfo.getName());

            googlePlayStoreCrawler.setAppInfo(appInfo, lastCrawlDate);
            sleep(2000);

            //get reviews of first page
            List<Review> reviewsOfPage = googlePlayStoreCrawler.getReviewsOfPage();
            if(reviewsOfPage.size() > 0){
                reviewCount = reviewCount + reviewsOfPage.size();
                reviewsOfPage = setArDocAndAppName(reviewsOfPage, appInfo.getName());
                dbWriter.writeReviewsToDB(reviewsOfPage);
            }

            while(googlePlayStoreCrawler.crawlNextSite()){

                googlePlayStoreCrawler.clickNextButton();
                reviewsOfPage = googlePlayStoreCrawler.getReviewsOfPage();

                if(reviewsOfPage != null){
                    reviewCount = reviewCount + reviewsOfPage.size();
                    reviewsOfPage = setArDocAndAppName(reviewsOfPage, appInfo.getName());
                    dbWriter.writeReviewsToDB(reviewsOfPage);
                }

                //reviewsPrintout(reviewsOfPage);

            }

            System.out.println(appInfo.getName() + ": finished crawling. Crawled reviews: " + reviewCount);
            reviewsCounts[looper] = reviewCount;
            looper++;

                googlePlayStoreCrawler.setDateOfLastCrawlIsReached(false);
        }


        //finish print out
        System.out.println("==================================================================================================================================================================================================================");
        for(int i = 0; i < reviewsCounts.length ; i++){
            if(i % 4 == 0){
                System.out.println("");
            }

            System.out.print(APP_INFOS[i].getName() + ", new Reviews: " + reviewsCounts[i] + " || ");


        }
        System.out.println("");
        System.out.println("==================================================================================================================================================================================================================");
    }

    private static List<Review> setArDocAndAppName (List<Review> reviewsOfPage, String appName) {
        Parser p = Parser.getInstance().getInstance();
        try{
            for (Review review: reviewsOfPage) {
                review.setApp(appName);
                ArrayList <Result> res = p.extract(ARDOC_SEARCH_METHOD, review.getReviewText());

                for (Result r : res ) {
                    review.setArDocClassification(turnSentenceIntoArDocClass(r.getSentenceClass()));
                }
            }
        } catch (UnknownCombinationException ex) {
            ex.printStackTrace();
        }
        return reviewsOfPage;
    }

    private static ArDocClassification turnSentenceIntoArDocClass(String sentenceClass) {
        String classWithUnderscore = sentenceClass.replaceAll(" ", "_");

        return ArDocClassification.valueOf(classWithUnderscore);
    }

    //TODO: remove, just testing purpose
    private static void reviewsPrintout(List<Review> reviewsOfPage) {
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
