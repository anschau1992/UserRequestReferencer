package crawler;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class Crawler implements Constants {

    Date testDate = new Date(116, 02, 29);

    public void saveAllReviewsOfAppsIntoDB(){
        for(final String appName : APP_NAMES){
            new Thread(new Runnable() {
                public void run() {
                    GooglePlayStoreCrawler googlePlayStoreCrawler = new GooglePlayStoreCrawler(appName, testDate);

                    while(googlePlayStoreCrawler.crawlNextSite()){
                        List<Review> reviewsOfPage = googlePlayStoreCrawler.getReviewsOfPage();
                        googlePlayStoreCrawler.clickNextButton();

                        for (Review review: reviewsOfPage){
                            System.out.println(review.getReviewText() + "// Date: " + review.getReviewDate().toString() + " // Stars: " + review.getNumberOfStars());
                        }

                        //TODO: categorise with ARDOC
                        //TODO: save to database
                        System.out.println("NOW THE SAVE TO THE DATABASE WILL BE EXECUTED!!!!!!");
                    }
                    googlePlayStoreCrawler.terminateDriver();
                }
            }).start();
        }
    }


    //TODO: insert class Apple-Crawler here
}
