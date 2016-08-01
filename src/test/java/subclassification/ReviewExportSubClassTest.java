package subclassification;

import crawler.Constants;
import org.junit.Before;
import org.junit.Test;
import preclassification.PreClassification;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReviewExportSubClassTest implements Constants {
    List<ReviewSubClassInfo> reviewInfos;

    @Before
    public void createTestReviewInfos() {
        ReviewExportSubClass exporter = new ReviewExportSubClass(MONGODB_PORT, DBNAME_TEST, REVIEW_COLLLECTION_TEST, PreClassification.USAGE);
        reviewInfos = exporter.createReviewSubClassInfo();
    }
    @Test
    public void testReviewInfosSize() {
        assertEquals("The number of created ReviewSubClassInfo's is correct", 8, reviewInfos.size());
    }

    @Test
    public void testFirstReviewInfo() {
        assertEquals("First reviewInfo has correct author", "579e4ae163d8a01177baa479", reviewInfos.get(0).getId());
        assertEquals("First reviewInfo has correct reviewText", "But……….. ⚠️ Personal Data Shared", reviewInfos.get(0).getReviewText());
        assertEquals("First reviewInfo has correct preclassification", PreClassification.USAGE, reviewInfos.get(0).getPreClassification());
    }
}
