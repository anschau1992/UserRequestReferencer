package preclassification;

import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import crawler.Constants;

import java.io.FileWriter;
import java.io.IOException;

public class ReviewCSVExport implements Constants {


    DBCollection collection;

    private ReviewCSVExport(int portnumb, String dbName, String collectionName) {
        MongoClient mongoClient = new MongoClient("localhost", portnumb);
        DB db = mongoClient.getDB(dbName);
        this.collection = db.getCollection(collectionName);
    }

    private void createReviewsInCSV() {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(PRECLASSIFICATION_EXPORT_PATH);
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
            System.err.println("Error while opening file in ReviewCSVExport");
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
        ReviewCSVExport reviewCSVExport = new ReviewCSVExport(MONGODB_PORT, DBNAME, REVIEW_COLLLECTION);
        reviewCSVExport.createReviewsInCSV();
    }
}
