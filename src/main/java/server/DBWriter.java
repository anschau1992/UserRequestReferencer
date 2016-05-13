package server;

import appsInfoCrawler.ExtendedAppInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import review.Review;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class DBWriter {

    private MongoDatabase db;
    private String databaseName;
    private String collectionName;
    private MongoCollection mongoCollection;

    public DBWriter(String databaseName, String collectionName) {
        this.databaseName = cleanName(databaseName);
        this.collectionName = cleanName(collectionName);
        MongoClient mongoClient = new MongoClient();
        db = mongoClient.getDatabase(this.databaseName);
        mongoCollection = db.getCollection(collectionName);
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
                .append("appVersion", review.getAppVersion())
        );
    }

    public void writeAppInfosToDb(List<ExtendedAppInfo> appInfos, String collectionName) {
        mongoCollection = db.getCollection(collectionName);
        writeAppInfosToDb(appInfos, mongoCollection);
    }

    public void writeAppInfosToDb(List<ExtendedAppInfo> appInfos, MongoCollection mongoCollection) {
        if (appInfos.isEmpty()) {
            return;
        }
        for (Document appInfoDocument : convertToDocuments(appInfos)) {
            String appInfoId = (String) appInfoDocument.get("id");
            FindIterable<Document> iterable = mongoCollection.find(new Document("id", appInfoId));
            Document first = iterable.first();
            if (first != null) {
                mongoCollection.updateOne(eq("id", appInfoId), new Document("$set", appInfoDocument));
            } else {
                mongoCollection.insertOne(appInfoDocument);
            }
        }
    }

    public int writeAppLinksToDb(Iterable<String> appLinks) {
        MongoCollection mongoCollection = db.getCollection("appLinks");
        List<Document> appLinksDocuments = new ArrayList<Document>();
        for (String appLink : appLinks) {
            FindIterable<Document> iterable = mongoCollection.find(new Document("appLink", appLink));
            if (iterable.first() == null) {
                appLinksDocuments.add(convertToDocument(appLink));
            }
        }
        if (!appLinksDocuments.isEmpty()) {
            mongoCollection.insertMany(appLinksDocuments);
        }
        return appLinksDocuments.size();
    }

    private Document convertToDocument(String appLink) {
        return new Document().append("appLink", appLink);
    }

    private List<Document> convertToDocuments(List<ExtendedAppInfo> appInfos) {
        List<Document> documents = new ArrayList();
        for (ExtendedAppInfo appInfo : appInfos) {
            documents.add(appInfo.convertToDocument());
        }
        return documents;
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
