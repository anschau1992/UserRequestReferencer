package crawler;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class Crawler implements Constants {



        public void startGooglePlayStoreCrawler(){

            for(final String appName : APP_NAMES){
                new Thread(new Runnable() {
                    public void run() {
                        GooglePlayStoreCrawler googlePlayStoreCrawler = new GooglePlayStoreCrawler();

                        List<String> googleReviews = new ArrayList<String>();

                        if(DATE_OF_LAST_CRAWL != null){
                            googleReviews = googlePlayStoreCrawler.getReviewsByAppName(appName, DATE_OF_LAST_CRAWL);
                        }else{
                            googleReviews = googlePlayStoreCrawler.getReviewsByAppName(appName);
                        }


                        //TODO: remove. Print out for testing
                        for (String review: googleReviews){
                            System.out.println(review);
                        }
                    }
                }).start();
            }
        }

    //TODO: insert class Apple-Crawler here
}
