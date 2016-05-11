package appsInfoCrawler;

import server.DBWriter;

import java.util.List;

/**
 * Main class for crawling apps per category.
 */
public class MainCrawler {
    private static final String DB_NAME = "appInfos";
    private static final String COLLLECTION_NAME = "infos";

    private static final String playUrl = "https://play.google.com/store/apps/category/";
    private static final String[] playCategories = new String[]{
            "ANDROID_WEAR", "BOOKS_AND_REFERENCE"
    };
    private final ExtendedAppInfoCrawler extendedAppInfoCrawler;
    private final DBWriter dbWriter;

    public MainCrawler() {
        extendedAppInfoCrawler = new ExtendedAppInfoCrawler();
        dbWriter = new DBWriter(DB_NAME, COLLLECTION_NAME);
    }

    public void crawl() {
        for (String playCategory : playCategories) {
            String url = playUrl + playCategory;
            extendedAppInfoCrawler.setAppInfoCategory(playCategory);
            System.out.println("Crawling url category: " + url);
            List<ExtendedAppInfo> appInfos = extendedAppInfoCrawler.crawlWithSeeMore(url);
            dbWriter.writeAppInfosToDb(appInfos);
            System.out.println("--------------------------------------------");
            for (ExtendedAppInfo appInfo : appInfos) {
                System.out.println(appInfo);
                System.out.println();
            }
            System.out.println("--------------------------------------------");
        }
        cleanup();
    }

    public void cleanup() {
        extendedAppInfoCrawler.finish();
    }

    public static void main(String[] args) {
        MainCrawler mainCrawler = new MainCrawler();
        mainCrawler.crawl();
    }
}
