package appsInfoCrawler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import server.DBWriter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static utils.SafariDriverUtils.createSafariDriver;
import static utils.SafariDriverUtils.goToUrl;

/**
 * Main class for crawling apps per category.
 */
public class MainCrawler {
    private static final String DB_NAME = "appInfos";
    private static final String COLLLECTION_NAME = "infos";

    private static final String playUrlNew = "https://play.google.com/store/apps/new";
    private static final String playUrlTop = "https://play.google.com/store/apps/top";
    private final ExtendedAppInfoCrawler extendedAppInfoCrawler;
    private final DBWriter dbWriter;
    private WebDriver driver;

    public MainCrawler() {
        extendedAppInfoCrawler = new ExtendedAppInfoCrawler();
        dbWriter = new DBWriter(DB_NAME, COLLLECTION_NAME);
        driver = createSafariDriver();
    }

    public void crawl() {
        String url = playUrlNew;
        System.out.println("Crawling url category: " + url);
        List<ExtendedAppInfo> appInfos = extendedAppInfoCrawler.crawlWithSeeMore(url);
        dbWriter.writeAppInfosToDb(appInfos);
        cleanup();
    }

    public void crawlAppUrls() {
        goToUrl(driver, playUrlNew);
        List<WebElement> seeMoreLinks = driver.findElements(By.className("see-more"));
        Set<String> moreLinks = new HashSet();
        for (WebElement seeMoreLink : seeMoreLinks) {
            moreLinks.add(seeMoreLink.getAttribute("href"));
        }
    }

    private void printResults(List<ExtendedAppInfo> appInfos) {
        System.out.println("--------------------------------------------");
        for (ExtendedAppInfo appInfo : appInfos) {
            System.out.println(appInfo);
            System.out.println();
        }
        System.out.println("--------------------------------------------");
    }

    public void cleanup() {
        extendedAppInfoCrawler.finish();
    }

    public static void main(String[] args) {
        MainCrawler mainCrawler = new MainCrawler();
        mainCrawler.crawlAppUrls();
    }
}
