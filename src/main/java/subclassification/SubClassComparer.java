package subclassification;

import crawler.Constants;
import helper.Review;

import java.util.List;
import java.util.Map;

public class SubClassComparer implements Constants {
    public SubClassComparer() {
    }

    /**
     * Check the classification by Weka is done corretly compared with the manual classification, if set
     * Otherwise set the subclasssification of weka
     * @param reviews the reviews with the Weka result
     * @return review with new subclassifications
     */
    public List<Review> compareWekaSubClass(List<Review> reviews) {
        double toClassify = 0;
        double correctClassified = 0;

        for (Review review : reviews) {
            Map.Entry<String, Double> highestDistributionClass = getHighestNumbPosition(review.getSubClassFDistribution());
            //subClass not defined
            if (review.getSubClassification().equals(ARFF_EMPTY_SIGN) || review.getSubClassification() == null) {
                review.setSubClassification(highestDistributionClass.getKey());
                printNewSubclass(highestDistributionClass, review);
            } else {
                toClassify++;
                if (review.getSubClassification().equals(highestDistributionClass.getKey())) {
                    review.setWekaCorrectClassified(true);
                    correctClassified++;
                    System.out.println("====================================================================================================================");
                    System.out.println("Review: \"" + review.getReviewText() + "\"");
                    System.out.println("Correctly classified into subclass: " + review.getSubClassification());
                    System.out.println("====================================================================================================================");
                } else {
                    review.setWekaCorrectClassified(false);
                    System.out.println("====================================================================================================================");
                    System.out.println("Review: \"" + review.getReviewText() + "\"");
                    System.out.println("Wrong classification:   Weka: " + highestDistributionClass.getKey() + "\t" + "DB:" + review.getSubClassification());
                    System.out.println("====================================================================================================================");
                }
            }
        }

        if (toClassify > 0.0) {
            System.out.println("\n*****************************************************************************************************************************");
            System.out.println("Correct classified: " + (int) correctClassified + "\t Wrong classified: " + (int) (toClassify - correctClassified));
            System.out.println("Classification-Precision: " + (correctClassified / toClassify));
            System.out.println("*****************************************************************************************************************************\n");
        }
        return reviews;
    }

    private static Map.Entry<String, Double> getHighestNumbPosition(Map<String, Double> fDistribution) {
        Map.Entry<String, Double> maxEntry = null;
        for (Map.Entry<String, Double> entry : fDistribution.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        return maxEntry;
    }

    private static void printNewSubclass(Map.Entry<String, Double> highestDistributionClass, Review review) {
        System.out.println("====================================================================================================================");
        System.out.println("Review: \"" + review.getReviewText() + "\"");
        System.out.println("new Subclassification: " + highestDistributionClass.getKey() + " with precision of " + highestDistributionClass.getValue());
        System.out.println("====================================================================================================================");
    }
}

