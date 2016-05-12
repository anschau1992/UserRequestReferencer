package appsInfoCrawler;

import com.mongodb.BasicDBObject;
import crawler.AppInfo;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that stores extended info about an app.
 */
public class ExtendedAppInfo extends AppInfo {
    private static final int STARS = 5;
    private static final int PRICE_MULTIPLIER = 100;

    private double starRating;
    private String category;
    private String badge;
    private String author;
    private long totalNrOfReviews;
    private long[] reviewsPerStars = new long[STARS];
    private String description;
    private String whatsNew;
    private int price;

    public ExtendedAppInfo(String name, String linkName, String id) {
        super(name, linkName, id);
    }

    public double getStarRating() {
        return starRating;
    }

    public void setStarRating(double starRating) {
        this.starRating = starRating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getBadge() {
        return badge;
    }

    public void setAuthor(String company) {
        this.author = company;
    }

    public String getAuthor() {
        return author;
    }

    public void setTotalNrOfReviews(long totalNrOfReviews) {
        this.totalNrOfReviews = totalNrOfReviews;
    }

    public long getTotalNrOfReviews() {
        return totalNrOfReviews;
    }

    public void setReviewsPerStars(long[] reviewsPerStars) {
        this.reviewsPerStars = reviewsPerStars;
    }

    public long[] getReviewsPerStars() {
        return reviewsPerStars;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setWhatsNew(String whatsNew) {
        this.whatsNew = whatsNew;
    }

    public String getWhatsNew() {
        return whatsNew;
    }

    public void setPrice(double price) {
        this.price = (int) (price * PRICE_MULTIPLIER);
    }

    public double getPrice() {
        return ((double) price) / PRICE_MULTIPLIER;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("id: " + getId());
        sb.append("\nname: " + getName());
        sb.append("\nlinkName: " + getLinkName());
        sb.append("\nprice: " + getPrice());
        sb.append("\nstarRating: " + getStarRating());
        sb.append("\ncategory: " + getCategory());
        sb.append("\nbadge: " + getBadge());
        sb.append("\nauthor: " + getAuthor());
        sb.append("\ntotalNrOfReviews: " + getTotalNrOfReviews());
        sb.append("\nreviewsPerStarRating: " + getReviewsPerStarString());
        sb.append("\ndescription: " + getDescription());
        sb.append("\nwhatsNew: " + getWhatsNew());
        return sb.toString();
    }

    private String getReviewsPerStarString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < STARS; i++) {
            sb.append((i + 1) + " stars: " + reviewsPerStars[i] + " ");
        }
        return sb.toString();
    }


    public Document convertToDocument() {
        List<BasicDBObject> starsRatings = new ArrayList();
        for (int i = 0; i < STARS; i++) {
            starsRatings.add(new BasicDBObject(String.valueOf(i + 1), reviewsPerStars[i]));
        }

        return new Document()
                .append("id", getId())
                .append("name", getName())
                .append("linkName", getLinkName())
                .append("price", getPrice())
                .append("starRating", getStarRating())
                .append("category", getCategory())
                .append("badge", getBadge())
                .append("author", getAuthor())
                .append("totalNrOfReviews", getTotalNrOfReviews())
                .append("reviewsPerStarRating", starsRatings)
                .append("description", getDescription())
                .append("whatsNew", getWhatsNew());
    }
}
