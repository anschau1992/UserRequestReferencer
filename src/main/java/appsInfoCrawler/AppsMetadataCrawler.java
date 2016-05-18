package appsInfoCrawler;

import server.DBWriter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Crawler that collects information about apps that are specified in a .csv file.
 */
public class AppsMetadataCrawler {
    private static final int BATCH_SIZE = 50;
    private static final String DB_NAME = "appInfos";
    private static final String COLLLECTION_NAME = "infos2";
    private static final Logger log = Logger.getLogger(AppsMetadataCrawler.class.getName());

    private final ExtendedAppInfoCrawler extendedAppInfoCrawler;
    private final DBWriter dbWriter;

    private BufferedReader reader;
    private int index;

    public AppsMetadataCrawler(String filename) throws IOException {
        this.reader = new BufferedReader(new FileReader(filename));
        this.extendedAppInfoCrawler = new ExtendedAppInfoCrawler();
        this.dbWriter = new DBWriter(DB_NAME, COLLLECTION_NAME);
        this.index = 0;
    }

    public void crawlAppsMetadata() throws IOException {
        try {
            int count = 0;
            while (true) {
                log.info("Crawling batch " + (count + 1));
                List<ExtendedAppInfo> appInfos = extendedAppInfoCrawler.crawlAppInfos(getBatchOfAppIds());
                if (appInfos.isEmpty()) {
                    break;
                }
                log.info("Writing or updating " + appInfos.size() + " appInfos rows to db.");
                dbWriter.writeAppInfosToDb(appInfos, COLLLECTION_NAME);
            }
        } finally {
            cleanup();
        }
    }

    private Iterable<String> getBatchOfAppIds() throws IOException {
        List<String> appIds = new ArrayList<String>();
        String appId;
        for (int i = 0; i < BATCH_SIZE; i++) {
            appId = reader.readLine();
            if (appId == null) {
                break;
            }
            appIds.add(appId);
        }
        return appIds;
    }

    public void cleanup() throws IOException {
        extendedAppInfoCrawler.finish();
        if (reader != null) {
            reader.close();
        }
    }


    public static void main(String[] args) throws Exception {
        AppsMetadataCrawler crawler = new AppsMetadataCrawler("./resources/trainData.csv");
        crawler.crawlAppsMetadata();
    }
}
