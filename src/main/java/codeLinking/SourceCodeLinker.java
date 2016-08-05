package codeLinking;

import helper.Review;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SourceCodeLinker {
    private Map<String, List<Review>> linkingReviews;
    private List<String> appNames;

    public SourceCodeLinker(List<Review> reviews) {
        linkingReviews = hashRelevantReviews(reviews);

        System.out.println("testing");
    }

    /**
     * hashes the reviews by App's
     * @param reviews
     * @return a hash with List of reviews split by App-name
     */
    private Map<String,List<Review>> hashRelevantReviews(List<Review> reviews) {
        appNames = new ArrayList<String>();
        Map<String, List<Review>> linkingReviews = new HashMap<String, List<Review>>();
        for (Review review : reviews) {
            String appName = review.getApp();
            if(!appNames.contains(appName)) {
                appNames.add(appName);
                List<Review> appReviews = new ArrayList<Review>();
                appReviews.add(review);
                linkingReviews.put(appName, appReviews);
            } else {
                List<Review> appReviews = linkingReviews.get(appName);
                appReviews.add(review);
                linkingReviews.put(appName, appReviews);
            }
        }

        return linkingReviews;
    }

    public Map<String, List<Review>> getLinkingReviews() {
        return linkingReviews;
    }

    public List<String> getAppNames() {
        return appNames;
    }
}
