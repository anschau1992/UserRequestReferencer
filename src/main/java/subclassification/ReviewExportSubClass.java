package subclassification;

import com.mongodb.*;
import crawler.Constants;
import preclassification.PreClassification;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReviewExportSubClass implements Constants {


    DBCollection collection;

    public ReviewExportSubClass(int portnumb, String dbName, String collectionName) {
        MongoClient mongoClient = new MongoClient("localhost", portnumb);
        DB db = mongoClient.getDB(dbName);
        this.collection = db.getCollection(collectionName);
    }

    public List<ReviewSubClassInfo> createReviewSubClassInfo() {

        DBCursor reviews = collection.find();
        List<ReviewSubClassInfo> reviewSubClassInfos = new ArrayList<ReviewSubClassInfo>();
        try {
            while (reviews.hasNext()) {
                DBObject review = reviews.next();
                String id = review.get("_id").toString();
                String reviewText = review.get("reviewText").toString();
                PreClassification preclassification = PreClassification.valueOf(review.get("preclassification").toString());

                ReviewSubClassInfo reviewSubClassInfo = new ReviewSubClassInfo(id, reviewText, preclassification);
                reviewSubClassInfos.add(reviewSubClassInfo);
            }
        } finally {
            reviews.close();
        }
        System.out.println("Successfully created all ReviewSubclassInfo's out of DB");

        return reviewSubClassInfos;
    }
}
