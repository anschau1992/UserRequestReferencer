/**
 * Get all the reviews of a app based on the given app name
 */
package crawler;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import review.Review;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class GooglePlayStoreCrawler {
    //TODO: move them to right place --> Constants or into method
    private static final String play_store_base_link = "https://play.google.com/store/apps/details?id=";
    private static final String next_reviews_button = "//button[@aria-label='See More' and @class='expand-button expand-next']";
    private static final String reviews_language = "en";

    private Date dateOfLastCrawl = null; // is given as parameter if already crawled before
    private boolean dateOfLastCrawlIsReached = false;

    AppInfo appInfo = null;
    private WebDriver driver = null;
    private WebDriverWait wait = null;


    public GooglePlayStoreCrawler() {
        driver = new SafariDriver();
        //set the browser outside of the monitor
        driver.manage().window().setPosition(new Point(-2000, -2000));
        driver.manage().window().maximize();
    }

    public void setAppInfo(AppInfo appInfo, Date dateOfLastCrawl) {
        this.appInfo = appInfo;
        this.dateOfLastCrawl = dateOfLastCrawl;
        System.out.println("Crawling all new reviews of "+ appInfo.getName() + " since " + dateOfLastCrawl);

        setupDriverWithLink();
        prepareReviews();

    }

    public GooglePlayStoreCrawler(AppInfo appInfo, Date dateOfLastCrawl){
        this.appInfo = appInfo;

        System.out.println("Crawling all new reviews of "+ appInfo.getName() + " since " + dateOfLastCrawl);
        this.dateOfLastCrawl = dateOfLastCrawl;

        setupDriverWithLink();
        prepareReviews();
    }
    private void  setupDriverWithLink(){
        //open Google Play Store App
        String appLink = play_store_base_link + appInfo.getId() + "." + appInfo.getLinkName() + "&hl=" + reviews_language;
        driver.navigate().to(appLink);
    }
    private void prepareReviews(){
        clickNextButton();
        scrollPage(0,-250);
        changeReviewSortOrderToNewest();
        moveHoveSoItShowsReviewDate();
    }

    public void clickNextButton() {
        //Wait until next button is clickable
        this.wait = new WebDriverWait(driver, 10);
        try{
            this.wait.until(ExpectedConditions.elementToBeClickable(By.xpath(next_reviews_button)));

            WebElement nextButton = driver.findElements(By.xpath(next_reviews_button)).get(1);
            nextButton.click();
        } catch (Throwable error) {
            System.err.println("No Next-Button found");
        }
    }
    private void scrollPage(int xAxis, int yAxis){
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        jse.executeScript("window.scrollBy(" + xAxis + "," + yAxis + ")", "");
    }
    private void changeReviewSortOrderToNewest() {
        //Open sortOrder Dropdown menu
        this.wait = new WebDriverWait(driver, 10);
        this.wait.until(ExpectedConditions.elementToBeClickable(By.className("dropdown-menu")));
        WebElement sortOrderButton = driver.findElements(By.className("dropdown-menu")).get(0);
        sortOrderButton.click();

        //Change order to 'Newest'
        this.wait = new WebDriverWait(driver, 10);
        WebElement newestOrderButton = driver.findElement(By.xpath("//button[contains(.,'Newest')]"));
        try{
            this.wait.until(ExpectedConditions.elementToBeClickable(newestOrderButton));
            newestOrderButton.click();
        }catch (Exception e) {
            sleep(2000);
            System.err.print(e + " // Waiting for 2 seconds!");
            newestOrderButton.click();
        }


    }
    //Focuses the mousehover somewhere else, otherwise review date is hidden
    private void moveHoveSoItShowsReviewDate() {
        WebElement hoverElement = driver.findElement(By.className("score"));
        Actions builder = new Actions(driver);
        //builder.moveToElement(hoverElement).perform();
    }

    public List<Review> getReviewsOfPage(){
        //TODO: implemented forPageLoaded, but not tested yet
        ExpectedCondition<Boolean> expectation = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor)driver)
                                .executeScript("return document.readyState").equals("complete");
                    }
                };

        WebDriverWait wait = new WebDriverWait(driver,30);

        try {
            wait.until(expectation);
            List <WebElement> newReviewsAsWebElement = getAllReviewsOfCurrentPage();
            List<Review> reviewsOfPage = reviewsSortedOutByDate(newReviewsAsWebElement, dateOfLastCrawl);
            return reviewsOfPage;

        } catch(Throwable error) {
            error.printStackTrace();
            return null;
        }
    }

    private List<WebElement> getAllReviewsOfCurrentPage() {
        final String reviewClassName = "single-review";
        return driver.findElements(By.className(reviewClassName));
    }

    private List<Review> reviewsSortedOutByDate(List<WebElement> crawledReviews, Date dateOfLastCrawl) {
        List<Review> sortedReviews = new ArrayList<Review>();
        DateFormat formatter = new SimpleDateFormat("MMMM dd,yyyy", Locale.ENGLISH);
        Date date = null;

        for (WebElement review : crawledReviews) {
            //sort out the empty strings
            if(!review.getText().equals("") && review.getText() != null) {
                String dateAsText = review.findElement(By.className("review-date")).getText();
                String reviewText = review.findElement(By.className("review-body")).getText();
                String author = review.findElement(By.className("author-name")).getText();
                String ratingStarWidth = review.findElement(By.className("current-rating")).getAttribute("style");
                int ratingStars = getRatingsStarByStyleAttribute(ratingStarWidth);

                if(dateOfLastCrawl != null){
                    //Parse date into Dateformat
                    try {
                        date = formatter.parse(dateAsText);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //add to List if newer than lastCrawl
                    if(date.after(dateOfLastCrawl)) {
                        //TODO: get the real number of stars
                        Review newReview = new Review(author, reviewText, date, ratingStars);
                        sortedReviews.add(newReview);
                    }else{
                        dateOfLastCrawlIsReached = true;
                        return sortedReviews;
                    }

                }
            }
        }
        return sortedReviews;
    }

    private int getRatingsStarByStyleAttribute(String ratingStarWidth) {
        int ratingStars;
        if(ratingStarWidth.contains("20%")){
            ratingStars = 1;
        } else if(ratingStarWidth.contains("40%")) {
            ratingStars = 2;
        }else if(ratingStarWidth.contains("60%")) {
            ratingStars = 3;
        }else if(ratingStarWidth.contains("80%")) {
            ratingStars = 4;
        }else if(ratingStarWidth.contains("100%")) {
            ratingStars = 5;
        } else {
            ratingStars = -1;
        }
        return ratingStars;
    }

    public boolean crawlNextSite() {
        if(nextButtonExists() && !dateOfLastCrawlIsReached){
            return true;
        }else {
            return false;
        }
    }
    private boolean nextButtonExists() {
        List<WebElement> arrowButton = this.driver.findElements(By.xpath(next_reviews_button));

        try{
            this.wait = new WebDriverWait(driver, 8);
            wait.until(ExpectedConditions.elementToBeClickable(arrowButton.get(1)));
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public void terminateDriver(){
        this.driver.quit();
    }

    private void sleep(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void waitForPageLoaded(WebDriver driver) {





    }

    public void setDateOfLastCrawlIsReached(boolean dateOfLastCrawlIsReached) {
        this.dateOfLastCrawlIsReached = dateOfLastCrawlIsReached;
    }
}


