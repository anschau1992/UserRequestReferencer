package appsInfoCrawler;

import com.google.common.base.Strings;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static utils.SafariDriverUtils.*;

/**
 * Selenium crawler that crawls the extended apps info for a given url with a specific category.
 */
public class ExtendedAppInfoCrawler {
    private static final Integer MAX_APPS = 100;
    private static final String INSTALL = "install";
    private static final String BUY = " buy";
    public static final String COMMA = ",";
    public static final String EMPTY = "";

    private WebDriver driver;

    public ExtendedAppInfoCrawler() {
        this(createSafariDriver());
    }

    public ExtendedAppInfoCrawler(WebDriver driver) {
        this.driver = driver;
    }

    public List<ExtendedAppInfo> crawlWithSeeMore(String url) {
        goToUrl(driver, url);
        return crawlMoreLinks(findMoreLinks());
    }

    private Set<String> findMoreLinks() {
        List<WebElement> seeMoreLinks = driver.findElements(By.className("see-more"));
        Set<String> moreLinks = new HashSet();
        for (WebElement seeMoreLink : seeMoreLinks) {
            moreLinks.add(seeMoreLink.getAttribute("href"));
        }
        return moreLinks;
    }

    public List<ExtendedAppInfo> crawlMoreLinks(Set<String> moreLinks) {
        List<ExtendedAppInfo> appInfos = new ArrayList();
        for (String moreLink : moreLinks) {
            appInfos.addAll(crawl(moreLink));
        }
        return appInfos;
    }

    public List<ExtendedAppInfo> crawl(String subcategoryUrl) {
        goToUrl(driver, subcategoryUrl);
        List<ExtendedAppInfo> appInfos = new ArrayList();
        while (true) {
            appInfos.addAll(crawlAppInfos(findAppUrls()));
            if (showMoreIsVisible(driver)) {
                break;
            }
            scrollPage(driver);
            sleep(2000);
        }
        return appInfos;
    }

    private boolean showMoreIsVisible(WebDriver driver) {
        WebElement element = driver.findElement(By.id("show-more-button"));
        return element != null && element.isDisplayed();
    }

    private Set<String> findAppUrls() {
        List<WebElement> webElementLinks = driver.findElements(By.className("title"));
        Set<String> appUrls = new HashSet();
        for (WebElement webElementLink : webElementLinks) {
            String href = webElementLink.getAttribute("href");
            if (!Strings.isNullOrEmpty(href)) {
                appUrls.add(webElementLink.getAttribute("href"));
            }
        }
        return appUrls;
    }

    private List<ExtendedAppInfo> crawlAppInfos(Set<String> appUrls) {
        List<ExtendedAppInfo> appInfos = new ArrayList<ExtendedAppInfo>();
        int i = 0;
        for (String appUrl : appUrls) {
            if (i++ >= MAX_APPS) {
                System.out.println(">>>>>>>>>>> limit reached");
                break;
            }

            ExtendedAppInfo appInfo = crawlAppInfo(appUrl);
            if (appInfo != null) {
                appInfos.add(appInfo);
            }
        }
        return appInfos;
    }

    private ExtendedAppInfo crawlAppInfo(String appUrl) {
        try {
            goToAppUrl(appUrl);
            return extractAppInfo(appUrl);
        } catch (Exception e) {
            System.out.println("Skipping app with url " + appUrl + " that caused exception: " + e.getMessage());
        }
        return null;
    }

    private void goToAppUrl(String appUrl) {
        System.out.println("Navigating to " + appUrl);
        goToUrl(driver, appUrl);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("reviewers-small")));
    }

    private ExtendedAppInfo extractAppInfo(String appUrl) {
        String googleAppName = extractGoogleAppName(appUrl);
        String appTitle = extractAppTitle(driver);
        ExtendedAppInfo appInfo = new ExtendedAppInfo(appTitle, googleAppName, googleAppName);
        appInfo.setPrice(extractPrice(driver, appUrl));
        appInfo.setStarRating(Double.valueOf(extractStarRating(driver)));
        appInfo.setBadge(extractBadge(driver));
        appInfo.setAuthor(extractAuthor(driver));
        appInfo.setCategory(extractCategory(driver));
        appInfo.setTotalNrOfReviews(Long.valueOf(extractTotalNrOfReviews(driver)));
        appInfo.setReviewsPerStars(extractReviewsPerStar(driver));
        appInfo.setDescription(extractDescription(driver));
        appInfo.setWhatsNew(extractWhatsNew(driver));
        return appInfo;
    }

    private double extractPrice(WebDriver driver, String appUrl) {
        WebElement priceElement = driver.findElement(By.className("price"));
        String priceText = getText(priceElement);
        if (priceText.indexOf(INSTALL) != -1) {
            return 0;
        }
        // Price text is of the form CHF 4,89 Buy
        int endIndex = priceText.indexOf(BUY);
        double price = Double.valueOf(priceText.substring(4, endIndex).replace(COMMA, EMPTY));
        return price;
    }

    private String extractGoogleAppName(String appUrl) {
        int index = appUrl.indexOf("id=");
        return appUrl.substring(index + 3);
    }

    private String extractAppTitle(WebDriver driver) {
        WebElement title = driver.findElement(By.className("id-app-title"));
        return title.getText();
    }

    private String extractStarRating(WebDriver driver) {
        WebElement starRating = driver.findElement(By.className("star-rating-non-editable-container"));
        String text = starRating.getAttribute("aria-label");
        int startIndex = 6;
        int endIndex = text.indexOf(" stars");
        return text.substring(startIndex, endIndex);
    }

    private String extractBadge(WebDriver driver) {
        try {
            WebElement badge = driver.findElement(By.className("badge-title"));
            return badge.getText();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    private String extractAuthor(WebDriver driver) {
        WebElement author = driver.findElement(By.className("primary"));
        return author.getText();
    }

    private String extractCategory(WebDriver driver) {
        WebElement category = driver.findElement(By.className("category"));
        return category.getText();
    }

    private String extractTotalNrOfReviews(WebDriver driver) {
        WebElement totalNrOfReviews = driver.findElement(By.className("rating-count"));
        return totalNrOfReviews.getText().replace(",", "");
    }

    private long[] extractReviewsPerStar(WebDriver driver) {
        String[] classNames = {"five", "four", "three", "two", "one"};
        // TODO(aci): refactor to constant
        long[] reviewsPerStar = new long[5];

        for (int i = 0; i < reviewsPerStar.length; i++) {
            String className = classNames[i];
            WebElement element = driver.findElement(By.className(className));
            reviewsPerStar[i] = Long.valueOf(element.getText().replaceAll("\\s", "").replace(",", ""));
        }
        return reviewsPerStar;
    }

    private String extractDescription(WebDriver driver) {
        WebElement description = driver.findElement(By.className("description"));
        String descriptionText = description.getText();
        if (!descriptionText.contains("READ MORE")) {
            return descriptionText;
        }
        WebElement readMoreButton = driver.findElement(By.className("show-more"));
        readMoreButton.click();
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("show-more")));
        sleep(2000);
        description = driver.findElement(By.className("description"));
        return description.getText();
    }

    private String extractWhatsNew(WebDriver driver) {
        try {
            scrollToElement(driver, "whatsnew");
            WebElement whatsnew = driver.findElement(By.className("whatsnew"));
            return whatsnew.getText();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    public void finish() {
        System.out.println("Quitting the driver.");
        driver.quit();
    }
}
