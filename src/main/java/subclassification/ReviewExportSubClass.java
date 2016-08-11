package subclassification;

import com.mongodb.*;
import helper.ArDocClassification;
import helper.Constants;
import preclassification.PreClassification;
import helper.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewExportSubClass implements Constants {


    DBCollection collection;
    PreClassification choosenPreClassification;

    public ReviewExportSubClass(int portnumb, String dbName, String collectionName,
                                PreClassification choosenPreclassification) {
        this.choosenPreClassification = choosenPreclassification;

        MongoClient mongoClient = new MongoClient("localhost", portnumb);
        DB db = mongoClient.getDB(dbName);
        this.collection = db.getCollection(collectionName);
    }

    public List<Review> createReviewSubClassInfo() {
        DBCursor reviewsbyPreClass;
        BasicDBObject query = new BasicDBObject("preclassification", choosenPreClassification.toString());
        reviewsbyPreClass = collection.find(query);

        List<Review> reviews = new ArrayList<Review>();
        try {
            while (reviewsbyPreClass.hasNext()) {
                DBObject reviewByPreClass = reviewsbyPreClass.next();
                String id = reviewByPreClass.get("_id").toString();
                String author = reviewByPreClass.get("author").toString();
                String reviewText = reviewByPreClass.get("reviewText").toString();
                int ratingStars = (Integer) reviewByPreClass.get("ratingStars");
                ArDocClassification ardoc = ArDocClassification.valueOf(reviewByPreClass.get("ardocclass").toString());
                String app = reviewByPreClass.get("app").toString();
                String appVersion = reviewByPreClass.get("appVersion").toString();
                PreClassification preclassification = PreClassification.valueOf(reviewByPreClass.get("preclassification").toString());
                String subclassification = reviewByPreClass.get("subclassification").toString();

                Review review = new Review(id, reviewText, preclassification, ratingStars);
                review.setReviewAuthor(author);
                review.setArDocClassification(ardoc);
                review.setApp(app);
                review.setApp(app);
                review.setAppVersion(appVersion);
                if(subclassification != null) {
                    review.setSubClassification(subclassification);
                } else {
                    review.setSubClassification(null);
                }
                reviews.add(review);
            }
        } finally {
            reviewsbyPreClass.close();
        }
        System.out.println("Successfully created all ReviewSubclassInfo's out of DB");

        return reviews;
    }
}
