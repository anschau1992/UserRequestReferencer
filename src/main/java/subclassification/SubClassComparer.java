package subclassification;

import helper.Constants;
import helper.Review;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SubClassComparer implements Constants {
    SubclassFileCreator fileCreator;
    public SubClassComparer() throws IOException {
    }

    /**
     * Check the classification by Weka is done corretly compared with the manual classification, if set
     * Otherwise set the subclasssification of weka
     * @param reviews the reviews with the Weka result
     * @return review with new subclassifications
     */
    public List<Review> compareWekaSubClass(List<Review> reviews) throws IOException {
        double toClassify = 0;
        double correctClassified = 0;

        fileCreator =  new SubclassFileCreator(SUBCLASS_FILE_PATH + reviews.get(0).getPreClassification().toString() + ".txt");

        for (Review review : reviews) {
            Map.Entry<String, Double> highestDistributionClass = getHighestNumbPosition(review.getSubClassFDistribution());
            //subClass not defined
            if (review.getSubClassification().equals(ARFF_EMPTY_SIGN) || review.getSubClassification() == null) {
                review.setWekaCorrectClassified(true);
                review.setSubClassification(highestDistributionClass.getKey());
                fileCreator.writeNewClassified(review, highestDistributionClass);
                printNewSubclass(highestDistributionClass, review);
            } else {
                toClassify++;
                if (review.getSubClassification().equals(highestDistributionClass.getKey())) {
                    review.setWekaCorrectClassified(true);
                    correctClassified++;
                    fileCreator.writeCorrectClassified(review);
                    System.out.println("====================================================================================================================");
                    System.out.println("Review: \"" + review.getReviewText() + "\"");
                    System.out.println("Correctly classified into subclass: " + review.getSubClassification());
                    System.out.println("====================================================================================================================");
                } else {
                    review.setWekaCorrectClassified(false);
                    fileCreator.writeWrongClassified(review, highestDistributionClass);
                    System.out.println("====================================================================================================================");
                    System.out.println("Review: \"" + review.getReviewText() + "\"");
                    System.out.println("Wrong classification:   Weka: " + highestDistributionClass.getKey() + "\t" + "DB:" + review.getSubClassification());
                    System.out.println("====================================================================================================================");
                }
            }
        }

        if (toClassify > 0.0) {
            fileCreator.writeSummary(correctClassified, toClassify);
            fileCreator.closeWriter();
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

