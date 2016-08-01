package review;

import preclassification.PreClassification;

import java.util.Date;
import java.util.Map;

public class Review {
    private String reviewAuthor;
    private String reviewText;
    private Date reviewDate;
    private int ratingStars;
    private String app;
    private String appVersion;
    private ArDocClassification arDocClassification;
    private PreClassification preClassification = null;
    private String subClassification = null;
    private Map<String, Double> wordsWithWeights = null;

    public Review(String reviewAuthor, String reviewText, Date reviewDate,int ratingStars, String appVersion){
        this.reviewAuthor = reviewAuthor;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
        this.ratingStars = ratingStars;
        this.appVersion = appVersion;
    }

    public String getReviewAuthor() {
        return this.reviewAuthor;
    }

    public String getReviewText() {
        return this.reviewText;
    }

    public Date getReviewDate() {
        return this.reviewDate;
    }

    public int getRatingStars() {
        return this.ratingStars;
    }

    public ArDocClassification getArDocClassification() {
        return arDocClassification;
    }

    public void setArDocClassification(ArDocClassification arDocClassification) {
        this.arDocClassification = arDocClassification;
    }

    public PreClassification getPreClassification() {
        return preClassification;
    }

    public void setPreClassification(PreClassification preClassification) {
        this.preClassification = preClassification;
    }

    public String getSubClassification() {
        return subClassification;
    }

    public void setSubClassification(String subClassification) {
        this.subClassification = subClassification;
    }

    public void setWordsWithWeights(Map<String, Double> wordsWithWeights) {
        this.wordsWithWeights = wordsWithWeights;
    }

    public Map<String, Double> getWordsWithWeights(){
        return this.wordsWithWeights;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getAppVersion() {
        return appVersion;
    }
}
