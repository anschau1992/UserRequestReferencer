package subclassification;

import helper.Constants;
import org.junit.Before;
import org.junit.Test;
import preclassification.PreClassification;
import helper.Review;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReviewExportSubClassTest implements Constants {
    List<Review> review;

    @Before
    public void createTestReviewInfos() {
        ReviewExportSubClass exporter = new ReviewExportSubClass(MONGODB_PORT, DBNAME_TEST, REVIEW_COLLLECTION, PreClassification.USAGE);
        review = exporter.createReviewSubClassInfo();
    }
    @Test
    public void testReviewInfosSize() {
        assertEquals("The number of created ReviewSubClassInfo's is correct", 8, review.size());
    }

    @Test
    public void testFirstReviewInfo() {
        assertEquals("First reviewInfo has correct author", "579e4ae163d8a01177baa479", review.get(0).getId());
        assertEquals("First reviewInfo has correct reviewText", "But……….. ⚠️ Personal Data Shared", review.get(0).getReviewText());
        assertEquals("First reviewInfo has correct preclassification", PreClassification.USAGE, review.get(0).getPreClassification());
    }
}
