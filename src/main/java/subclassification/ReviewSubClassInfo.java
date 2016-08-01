package subclassification;

import org.apache.commons.math3.linear.RealVector;
import preclassification.PreClassification;
import subclassification.stanfordNLP.NLPType;

import java.util.List;

public class ReviewSubClassInfo {
    String id;
    String reviewText;
    PreClassification preClassification;
    int sentimentScore;
    NLPType nlpType;
    int ratingStars;
    RealVector termsVector;


    public ReviewSubClassInfo(String id, String reviewText, PreClassification preClassification, int ratingStars) {
        this.id = id;
        this.reviewText = reviewText;
        this.preClassification = preClassification;
        this.ratingStars = ratingStars;
    }

    public String getId() {
        return id;
    }

    public String getReviewText() {
        return reviewText;
    }

    public PreClassification getPreClassification() {
        return preClassification;
    }

    public void setSentimentScore(int sentimentScore) {
        this.sentimentScore = sentimentScore;
    }

    public int getSentimentScore() {
        return sentimentScore;
    }

    public NLPType getNlpType() {
        return nlpType;
    }

    public void setNlpType(NLPType nlpType) {
        this.nlpType = nlpType;
    }

    public RealVector getTermsVector() {
        return termsVector;
    }

    public void setTermsVector(RealVector termsVector) {
        this.termsVector = termsVector;
    }

    public int getRatingStars() {
        return ratingStars;
    }

    public void setRatingStars(int ratingStars) {
        this.ratingStars = ratingStars;
    }
}
