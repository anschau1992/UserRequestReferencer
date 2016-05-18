import crawler.Crawler;
import crawler.GooglePlayStoreCrawler;
import server.DBWriter;

import java.util.Date;

public class ReviewToDB {
    static Date lastCrawlDate;
    static GooglePlayStoreCrawler googlePlayStoreCrawler;
    static DBWriter dbWriter;



    public  static void main(String[] args) throws InterruptedException {
        Crawler crawler = Crawler.getInstance();
        crawler.crawlForEachAPP();
    }
}
