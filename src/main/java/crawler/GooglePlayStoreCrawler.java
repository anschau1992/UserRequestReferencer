/**
 * Get all the reviews of a app based on the given app name
 */
package crawler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GooglePlayStoreCrawler {
    private static final int searchLimit = 10; //TODO: only testing purpose --> remove afterwards
    private int count = 0;

    //TODO: move them to right place --> Constants or into method
    private static final String play_store_base_link = "https://play.google.com/store/apps/details?id=com.";
    private static final String next_reviews_button = "//button[@aria-label='See More' and @class='expand-button expand-next']";
    private static final String reviews_language = "en";

    //Overloading: First Crawl
    public List<String> getReviewsByAppName(String appName){

        System.out.println("Crawling without last Date of crawl");

        WebDriver driver = connectWithDriverOfLink(appName);
        return getReviewsByDriver(driver);
    }

    //Overloading: Date of last Crawl is defined
    public List<String> getReviewsByAppName(String appName, Date dateOfLastCrawl){

        System.out.println("Crawling with last Date of crawl");

        WebDriver driver = connectWithDriverOfLink(appName);
        return getReviewsByDriver(driver);
    }


    private WebDriver connectWithDriverOfLink(String appName){
        String appLink = play_store_base_link + appName + "&hl=" + reviews_language;

        WebDriver driver = new FirefoxDriver();
        driver.navigate().to(appLink);

        return driver;
    }

    private List<String> getReviewsByDriver(WebDriver driver) {

        List<String> reviews = new ArrayList<String>();

        //first click = start with reviews
        clickNextButton(driver);
        changeReviewSortOrderToNewest(driver);

        do {
            reviews = addNextReviewsAsString(reviews, driver);
            clickNextButton(driver);

        //TODO: change to unlimited search
            count ++;
        //}while (nextButtonExists(driver));
        }while (count < searchLimit);

        return reviews;
    }

    private void clickNextButton(WebDriver driver) {
        WebElement nextButton = driver.findElements(By.xpath(next_reviews_button)).get(1);
        nextButton.click();

        //TODO: replace it with a trigger after refreshed website
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void changeReviewSortOrderToNewest(WebDriver driver) {
        WebElement sortOrderButton = driver.findElements(By.className("dropdown-menu")).get(0);
        sortOrderButton.click();

        WebElement newestOrderButton = driver.findElement(By.xpath("//button[contains(.,'Newest')]"));
        newestOrderButton.click();

        //TODO: replace it with a trigger after refreshed website
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private List<String> addNextReviewsAsString(List<String> crawledReviews, WebDriver driver) {
        final String reviewClassName = "review-body";

        List<WebElement> newReviewsAsElement = driver.findElements(By.className(reviewClassName));

        for (WebElement review : newReviewsAsElement) {
            //sort out the empty strings
            if(!review.getText().equals("")) {
                crawledReviews.add(review.getText());
            }
        }
        return crawledReviews;
    }

    private boolean nextButtonExists(WebDriver driver) {
        List<WebElement> arrowButton = driver.findElements(By.xpath(next_reviews_button));

        if(arrowButton.size() >= 1){
            return true;
        }else{
            return false;
        }
    }
}
