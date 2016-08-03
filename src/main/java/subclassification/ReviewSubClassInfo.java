package subclassification;

import org.apache.commons.math3.linear.RealVector;
import preclassification.PreClassification;
import subclassification.stanfordNLP.NLPType;

import java.util.List;
import java.util.Map;

public class ReviewSubClassInfo {
    String id;
    String reviewText;
    PreClassification preClassification;
    String subClassification;
    int sentimentScore;
    NLPType nlpType;
    int ratingStars;
    RealVector termsVector;
    Map <String, Double> subClassFDistribution;


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

    public String getSubClassification() {
        return subClassification;
    }

    public void setSubClassification(String subClassification) {
        this.subClassification = subClassification;
    }

    public Map<String, Double> getSubClassFDistribution() {
        return subClassFDistribution;
    }

    public void setSubClassFDistribution(Map<String, Double> subClassFDistribution) {
        this.subClassFDistribution = subClassFDistribution;
    }
}
