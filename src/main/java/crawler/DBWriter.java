package crawler;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import helper.Review;

import java.util.*;

public class DBWriter implements Constants {

    MongoDatabase db;
    String databaseName;
    String reviewCollectionName;
    String appInfoCollectionName;

    public DBWriter(String databaseName, String reviewCollectionName, String appInfoCollectionName) {
        this.databaseName = cleanName(databaseName);
        this.reviewCollectionName = cleanName(reviewCollectionName);
        this.appInfoCollectionName = appInfoCollectionName;
        MongoClient mongoClient = new MongoClient();
        db = mongoClient.getDatabase(this.databaseName);
    }

    ;

    private String cleanName(String databaseName) {
        String cleanName = databaseName.replaceAll("[^a-zA-Z ]", "").toLowerCase();
        return cleanName.replaceAll(" ", "");
    }

    public void writeAppInfosToDB(List<AppInfo> appInfos) {
        int newAppsCount = 0;
        MongoCollection mongoCollection = db.getCollection(cleanName(appInfoCollectionName));
        for (AppInfo appInfo : appInfos) {
            if (appIsNotOnDB(appInfo, mongoCollection)) {
                newAppsCount++;
                writeAppInfoToDB(appInfo, mongoCollection);
            }
        }

        System.out.println("New added AppInfos in DB: " + newAppsCount);
    }

    private boolean appIsNotOnDB(AppInfo appInfo, MongoCollection mongoCollection) {
        FindIterable iterable = mongoCollection.find(new Document("name", appInfo.getName()));
        if (iterable.first() != null) {
            return false;
        }
        return true;
    }

    private void writeAppInfoToDB(AppInfo appInfo, MongoCollection mongoCollection) {
        mongoCollection.insertOne(new Document()
                .append("name", appInfo.getName())
                .append("category", appInfo.getCategory())
                .append("playStoreLinkID", appInfo.getPlayStoreLinkID())
                .append("sourceCodeLink", appInfo.getSourceCodeLink())
        );
    }

    public void writeReviewsToDB(List<Review> reviews) {
        //TODO: change back if not works
        MongoCollection mongoCollection = db.getCollection(cleanName(reviewCollectionName));
        for (Review review : reviews) {
            writeReviewToDB(review, mongoCollection);
        }
        if (reviews.size() > 0) {
            System.out.println(reviews.get(0).getApp() + ": Wrote " + reviews.size()
                    + " Reviews to DB ");
        } else {
            System.out.println("Reviews on DB are up to date ");
        }

    }

    private void writeReviewToDB(Review review, MongoCollection mongoCollection) {
        String preClass = checkPreClass(review);
        String subclass = checkSubClass(review);
        String ardocCLass = checkArDocClass(review);
        mongoCollection.insertOne(
                new Document()
                        .append("author", review.getReviewAuthor())
                        .append("reviewText", review.getReviewText())
                        .append("date", review.getReviewDate())
                        .append("ratingStars", review.getRatingStars())
                        .append("ardocclass", ardocCLass)
                        .append("app", review.getApp())
                        .append("appVersion", review.getAppVersion())
                        .append("preclassification", preClass)
                        .append("subclassification", subclass)
        );
    }

    private String checkPreClass(Review review) {
        String preClass;
        if (review.getPreClassification() != null) {
            preClass = review.getPreClassification().toString();
        } else {
            preClass = ARFF_EMPTY_SIGN;
        }
        return preClass;
    }

    private String checkSubClass(Review review) {
        String subClass;
        if (review.getSubClassification() != null) {
            subClass = review.getSubClassification().toString();
        } else {
            subClass = ARFF_EMPTY_SIGN;
        }
        return subClass;
    }

    private String checkArDocClass(Review review) {
        String ardocClass;
        if (review.getArDocClassification() != null) {
            ardocClass = review.getArDocClassification().toString();
        } else {
            ardocClass = "OTHER";
        }
        return ardocClass;
    }

    public Date getLatestReviewsDateOfApp(String appName) {
        FindIterable<Document> iterable = db.getCollection(cleanName(reviewCollectionName))
                .find(new Document("app", appName))
                .sort(new BasicDBObject("date", -1)).limit(1);
        if (iterable.first() == null) {
            return new Date(0);
        }
        Document latestReview = iterable.first();
        return latestReview.getDate("date");
    }
}
