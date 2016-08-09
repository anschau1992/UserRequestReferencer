package crawler;

import helper.Constants;
import org.ardoc.Parser;
import org.ardoc.Result;
import org.ardoc.UnknownCombinationException;
import helper.ArDocClassification;
import helper.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * With the use of ARDOC:: http://www.ifi.uzh.ch/en/seal/people/panichella/tools/ARdoc.html
 * the review will be classified
 */
public class ArDocDecider implements Constants {
    private static ArDocDecider uniqueInstance;

    private ArDocDecider() {}

    public static ArDocDecider getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new ArDocDecider();
        }
        return uniqueInstance;
    }

    public List<Review> setArDocAndAppName(List<Review> reviewsOfPage, String appName) {
        Parser p = Parser.getInstance().getInstance();
        try {
            for (Review review : reviewsOfPage) {
                review.setApp(appName);
                ArrayList<Result> res = p.extract(ARDOC_SEARCH_METHOD, review.getReviewText());

                for (Result r : res) {
                    review.setArDocClassification(turnSentenceIntoArDocClass(r.getSentenceClass()));
                }
            }
        } catch (UnknownCombinationException ex) {
            ex.printStackTrace();
        }
        return reviewsOfPage;
    }

    private ArDocClassification turnSentenceIntoArDocClass(String sentenceClass) {
        String classWithUnderscore = sentenceClass.replaceAll(" ", "_");

        return ArDocClassification.valueOf(classWithUnderscore);
    }
}
