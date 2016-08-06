package codeLinking;

import com.mongodb.*;
import crawler.AppInfo;
import crawler.Constants;
import helper.Review;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SourceCodeLinker implements Constants {
    private String dbName;
    private Map<String, List<Review>> linkingReviews;
    private List<String> appNames;

    public SourceCodeLinker(List<Review> reviews, String dbName) throws IOException {
        this.dbName = dbName;
        linkingReviews = hashRelevantReviews(reviews);
        Map<String, String> sourceCodeLinks = getSourceCodeLink();

        //Get sourceCode files into /sourceCode
        SourceCodeCrawler sourceCodeCrawler = new SourceCodeCrawler();
        sourceCodeCrawler.crawl(appNames, sourceCodeLinks);

        //TODO foreach file (recursive) check format(take .java .xml) --> call indexer with File
        //TODO build proper index for sourceCode-files in lucene --> use different indexer with different analyzer?
        SourceCodeIndexer sci = new SourceCodeIndexer();

        //TODO foreach review in linkingReviews --> preprocessing with narrow goal-folders down

        System.out.println("testing");
    }


    /**
     * hashes the reviews by App's, and filters the wrong subclassified out
     * @param reviews
     * @return a hash with List of reviews split by App-name
     */
    private Map<String, List<Review>> hashRelevantReviews(List<Review> reviews) {
        appNames = new ArrayList<String>();
        Map<String, List<Review>> linkingReviews = new HashMap<String, List<Review>>();
        for (Review review : reviews) {
            if (review.isWekaCorrectClassified()) {
                String appName = review.getApp();
                if (!appNames.contains(appName)) {
                    appNames.add(appName);
                    List<Review> appReviews = new ArrayList<Review>();
                    appReviews.add(review);
                    linkingReviews.put(appName, appReviews);
                } else {
                    List<Review> appReviews = linkingReviews.get(appName);
                    appReviews.add(review);
                    linkingReviews.put(appName, appReviews);
                }
            }
        }

        return linkingReviews;
    }

    public Map<String, List<Review>> getLinkingReviews() {
        return linkingReviews;
    }

    public List<String> getAppNames() {
        return appNames;
    }

    public Map<String, String> getSourceCodeLink() {
        String collectionName = APP_INFOS_COLLLECTION;
        int portnumb = MONGODB_PORT;
        MongoClient mongoClient = new MongoClient("localhost", portnumb);
        DB db = mongoClient.getDB(dbName);
        DBCollection appCollection = db.getCollection(collectionName);

        Map<String, String> appLinks = new HashMap<String, String>();
        for (String appName : appNames) {
            BasicDBObject query = new BasicDBObject();
            query.put("name", appName);

            DBObject appInfos = appCollection.findOne(query);
            appLinks.put(appName, appInfos.get("sourceCodeLink").toString());
        }
        return appLinks;
    }
}
