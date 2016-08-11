package helper;

import org.apache.commons.math3.linear.RealVector;
import preclassification.PreClassification;
import subclassification.stanfordNLP.NLPType;

import java.util.Date;
import java.util.Map;

public class Review {
    private String id;
    private String reviewAuthor;
    private String reviewText;
    private Date reviewDate;
    private int ratingStars;
    private String app;
    private String appVersion;
    private ArDocClassification arDocClassification;
    private PreClassification preClassification = null;
    private String subClassification = null;
    private int sentimentScore;
    private NLPType nlpType;
    RealVector termsVector;
    Map <String, Double> subClassFDistribution;

    private boolean wekaCorrectClassified = false;



    public Review(String reviewAuthor, String reviewText, Date reviewDate,int ratingStars, String appVersion){
        this.reviewAuthor = reviewAuthor;
        this.reviewText = reviewText;
        this.reviewDate = reviewDate;
        this.ratingStars = ratingStars;
        this.appVersion = appVersion;
    }

    public Review(String id, String reviewText, PreClassification preClassification, int ratingStars) {
        this.id = id;
        this.reviewText = reviewText;
        this.preClassification = preClassification;
        this.ratingStars = ratingStars;
    }

    public String getReviewAuthor() {
        return this.reviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
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

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSentimentScore() {
        return sentimentScore;
    }

    public void setSentimentScore(int sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public NLPType getNlpType() {
        return nlpType;
    }

    public void setNlpType(NLPType nlpType) {
        this.nlpType = nlpType;
    }

    public Map<String, Double> getSubClassFDistribution() {
        return subClassFDistribution;
    }

    public void setSubClassFDistribution(Map<String, Double> subClassFDistribution) {
        this.subClassFDistribution = subClassFDistribution;
    }

    public RealVector getTermsVector() {
        return termsVector;
    }

    public void setTermsVector(RealVector termsVector) {
        this.termsVector = termsVector;
    }

    public boolean isWekaCorrectClassified() {
        return wekaCorrectClassified;
    }

    public void setWekaCorrectClassified(boolean wekaCorrectClassified) {
        this.wekaCorrectClassified = wekaCorrectClassified;
    }
}
