package subclassification;

import com.mongodb.*;
import crawler.Constants;
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
                String reviewText = reviewByPreClass.get("reviewText").toString();
                PreClassification preclassification = PreClassification.valueOf(reviewByPreClass.get("preclassification").toString());
                int ratingStars = (Integer) reviewByPreClass.get("ratingStars");
                String subclassification = reviewByPreClass.get("subclassification").toString();

                Review review = new Review(id, reviewText, preclassification, ratingStars);
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
