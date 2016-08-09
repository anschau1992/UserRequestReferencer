package preclassification;

import com.mongodb.*;
import helper.Constants;

import java.io.FileWriter;
import java.io.IOException;

public class ReviewCSVExportPreClass implements Constants {


    DBCollection collection;

    private ReviewCSVExportPreClass(int portnumb, String dbName, String collectionName) {
        MongoClient mongoClient = new MongoClient("localhost", portnumb);
        DB db = mongoClient.getDB(dbName);
        this.collection = db.getCollection(collectionName);
    }

    private void createReviewsInCSV() {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(PRECLASSIFICATION_EXPORT_PATH_TEST);
            DBCursor reviews = collection.find();

            try {
                while (reviews.hasNext()) {
                    DBObject review = reviews.next();
                    String id = review.get("_id").toString();
                    String reviewText = review.get("reviewText").toString();

                    fileWriter.append(id);
                    fileWriter.append(";");
                    fileWriter.append(reviewText);
                    fileWriter.append("\n");
                }
            } finally {
                reviews.close();
            }
            System.out.println("CSV file was created successfully");

        } catch (IOException e) {
            System.err.println("Error while opening file in ReviewCSVExportPreClass");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.err.println("Error while flushing/closing fileWriter");
                e.printStackTrace();
            }
        }

    }

    public static void main(String args[]) {
        ReviewCSVExportPreClass reviewCSVExportPreClass = new ReviewCSVExportPreClass(MONGODB_PORT, DBNAME_TEST, REVIEW_COLLLECTION);
        reviewCSVExportPreClass.createReviewsInCSV();
    }
}
