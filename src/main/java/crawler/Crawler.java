package crawler;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.ardoc.Parser;
import org.ardoc.Result;
import org.ardoc.UnknownCombinationException;


public class Crawler implements Constants {

    Date testDate = new Date(116, 03, 8);

    public void saveAllReviewsOfAppsIntoDB(){
        for(final String appName : APP_NAMES){
            new Thread(new Runnable() {
                public void run() {
                    GooglePlayStoreCrawler googlePlayStoreCrawler = new GooglePlayStoreCrawler(appName, testDate);

                    while(googlePlayStoreCrawler.crawlNextSite()){
                        List<Review> reviewsOfPage = googlePlayStoreCrawler.getReviewsOfPage();
                        googlePlayStoreCrawler.clickNextButton();

                        //TODO: do some refactoring; very inefficent & ugly
                        Parser p = Parser.getInstance();
                        try{
                            for (Review review: reviewsOfPage){
                                ArrayList<Result> res = p.extract("NLP+SA", review.getReviewText());

                                for (Result r : res) {
                                    review.setClassification(r.getSentenceClass());
                                }
                            }
                        } catch (UnknownCombinationException ex){
                            ex.printStackTrace();
                        }

                        for (Review review: reviewsOfPage){
                            System.out.println(review.getReviewText() + "// Date: " + review.getReviewDate().toString()
                                    + " // Stars: " + review.getNumberOfStars() + " // Classification: " + review.getClassification());

                        }


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
