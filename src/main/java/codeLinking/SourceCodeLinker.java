package codeLinking;

import com.mongodb.*;
import crawler.Constants;
import helper.Review;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.File;
import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SourceCodeLinker implements Constants {
    private String dbName;
    private Map<String, List<Review>> linkingReviews;
    private List<String> appNames;

    public SourceCodeLinker(List<Review> reviews, String dbName) throws IOException, ParseException {
        this.dbName = dbName;
        linkingReviews = hashRelevantReviews(reviews);
        Map<String, String> sourceCodeLinks = getSourceCodeLink();

        //Get sourceCode files into /sourceCode
        SourceCodeCrawler sourceCodeCrawler = new SourceCodeCrawler();
        sourceCodeCrawler.crawl(appNames, sourceCodeLinks);

        //create Index for each app
        RecursiveFileReader recursiveFileReader = new RecursiveFileReader();
        if (new File(SOURCE_CODE_PATH).exists()) {
            for (String appName : appNames) {
                String fileName = appName.replaceAll(" ", "_").toLowerCase();
                File file = new File(SOURCE_CODE_PATH + "/" + fileName);
                recursiveFileReader.readeFiles(file, fileName);
            }
        } else {
            System.out.println("There is no source-code! Program is turning off");
            System.exit(0);
        }


        //TODO foreach review in linkingReviews --> preprocessing with narrow goal-folders down
        SourceCodeSearcher sourceCodeSearcher = new SourceCodeSearcher();
        for (String appName: appNames) {
            String fileName = appName.replaceAll(" ", "_").toLowerCase();
            Path path = Paths.get("./"+ INDEX_FOLDER_PATH + "/" + fileName);
            List<Review> appReviews = linkingReviews.get(appName);
            sourceCodeSearcher.searchForReviews(path, appReviews);
        }
    }


    /**
     * hashes the reviews by App's, and filters the wrong subclassified out
     *
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
