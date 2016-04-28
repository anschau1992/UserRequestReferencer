package server;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import crawler.AppInfo;
import org.bson.Document;
import review.Review;

import java.util.*;

public class DBWriter {

    MongoDatabase db;
    String databaseName;
    String collectionName;

    public DBWriter(String databaseName, String collectionName) {
        this.databaseName = cleanName(databaseName);
        this.collectionName = cleanName(collectionName);
        MongoClient mongoClient = new MongoClient();
        db = mongoClient.getDatabase(this.databaseName);
    };

    private String cleanName(String databaseName) {
       String cleanName = databaseName.replaceAll("[^a-zA-Z ]", "").toLowerCase();
        return cleanName.replaceAll(" ", "");
    }

    public void writeReviewsToDB(List<Review> reviews) {
        for (Review review : reviews) {
            MongoCollection mongoCollection = db.getCollection(cleanName(collectionName));
            writeReviewToDB(review, mongoCollection);
        }
        if(reviews.size() > 0){
            System.out.println(reviews.get(0).getApp() +": Wrote " + reviews.size()
                    + " Reviews to DB ");
        }else {
            System.out.println("Reviews on DB are up to date ");
        }

    }

    private void writeReviewToDB(Review review, MongoCollection mongoCollection) {
        mongoCollection.insertOne(
                new Document()
                .append("author",review.getReviewAuthor())
                .append("reviewText", review.getReviewText())
                .append("date", review.getReviewDate())
                .append("ratingStars", review.getRatingStars())
                .append("classification", review.getArDocClassification().toString())
                .append("app", review.getApp())
        );
    }

    public Date getLatestReviewsDateOfApp(String appName) {
        FindIterable<Document> iterable =  db.getCollection(cleanName(collectionName))
                .find(new Document("app", appName))
                .sort(new BasicDBObject("date",-1)).limit(1);
        if(iterable.first() == null){
            return new Date(0);
        }
        Document latestReview = iterable.first();
        return latestReview.getDate("date");
    }
}
