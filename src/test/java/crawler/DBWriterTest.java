//package server;
//
//import com.mongodb.MongoClient;
//import com.mongodb.client.MongoDatabase;
//import crawler.AppInfo;
//import org.junit.After;
//import org.junit.Test;
//import review.ArDocClassification;
//import review.Review;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
//public class DBWriterTest {
//    AppInfo testAppInfo = new AppInfo("Test App", "test.app", "com");
//    String cleanCollectionName = "testapp";
//    DBWriter dbWriter = new DBWriter(testAppInfo);
//    MongoDatabase db;
//    MongoClient mongoClient = new MongoClient();
//
//
//    @Test
//    public void testGetLatestReviewsDate() {
//        db = mongoClient.getDatabase(cleanCollectionName);
//
//        //test when no review is in DB --> returns default Date
//        Date defaultDate = dbWriter.getLatestReviewsDateOfApp();
//        assertEquals("GetLatestReviewsDate returns default Date if no reviews are in DB ", new Date(0), defaultDate);
//    }
//
//    @Test
//    public void testWriteToDBAndGetLatestDateAfterwards() {
//        db = mongoClient.getDatabase(cleanCollectionName);
//
//        //test returns latest Date in DB
//        Review testReview = new Review("TestAutor", "This review 0 is just for testing purpose", new Date(), 5);
//        Review testReview1 = new Review("TestAutor", "This review 1 is just for testing purpose", new Date(200), 5);
//        Review testReview2 = new Review("TestAutor", "This review 2 is just for testing purpose", new Date(300), 5);
//        testReview.setArDocClassification(ArDocClassification.valueOf("OTHER"));
//        testReview1.setArDocClassification(ArDocClassification.valueOf("OTHER"));
//        testReview2.setArDocClassification(ArDocClassification.valueOf("OTHER"));
//
//        List<Review> testReviewList = new ArrayList<Review>();
//        testReviewList.add(testReview);
//        testReviewList.add(testReview1);
//        testReviewList.add(testReview2);
//
//        dbWriter.writeReviewsToDB(testReviewList, cleanCollectionName);
//        Date latestDateinDB = dbWriter.getLatestReviewsDateOfApp();
//        assertEquals("GetLatestReviewsDate return the latest Date", withoutTime( new Date()), withoutTime(latestDateinDB));
//
//
//    }
//
//    private Date withoutTime(Date date) {
//        Date res = date;
//        Calendar calendar = Calendar.getInstance();
//
//        calendar.setTime(date);
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//
//        res = calendar.getTime();
//
//        return res;
//    }
//
//    @After
//    public void cleanup () {
//        db.getCollection("testapp").drop();
//    }
//}
