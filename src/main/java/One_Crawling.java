import java.io.File;
import java.util.Date;
import java.util.List;

import crawler.*;
import helper.Constants;
import helper.Review;
import org.apache.commons.io.FilenameUtils;

import static java.lang.Thread.sleep;


public  class One_Crawling implements Constants {
    private Date lastCrawlDate;
    private GooglePlayStoreCrawler googlePlayStoreCrawler;
    private DBWriter dbWriter;
    private List<AppInfo> appInfos;

    private One_Crawling(String appInfosCSV, UsingBrowser browser, String db) {
        this.googlePlayStoreCrawler = new GooglePlayStoreCrawler(browser);
        this.dbWriter = new DBWriter(db, REVIEW_COLLLECTION, APP_INFOS_COLLLECTION);

        AppInfoCSVReader csvReader = new AppInfoCSVReader();
        this.appInfos = csvReader.transformToAppInfos(appInfosCSV);
    }


    public void writeAppInfosToDB() {
        dbWriter.writeAppInfosToDB(appInfos);
    }

    /**
     * Foreach app saved in DB, it opens the PlayStore-link and crawls reviews.
     * It crawls until it reaches the date of last crawl or a limit specified in 'Constants
     * @throws InterruptedException
     */
    public void crawlForEachAPP() throws InterruptedException {
        int[] reviewsCounts = new int[appInfos.size()];
        int looper = 0;
        for (final AppInfo appInfo : appInfos) {
            int reviewCount = 0;
            printAppCrawlStart(appInfo);

            //get Date of latest review. If not exist, give default 1970
            lastCrawlDate = dbWriter.getLatestReviewsDateOfApp(appInfo.getName());
            googlePlayStoreCrawler.setAppInfo(appInfo, lastCrawlDate);
            sleep(2000);

            //get reviews of first page
            reviewCount = writeCurrentReviewsToDB(reviewCount, appInfo);

            while (googlePlayStoreCrawler.crawlNextSite() && reviewCount <= REVIEWSPERAPPLIMIT) {
                googlePlayStoreCrawler.clickNextButton();
                //TODO: find better way to avoid empty reviewwindow
//                if(googlePlayStoreCrawler.getReviewsOfPage().size() <= 0){
//                    break;
//                }
                reviewCount = writeCurrentReviewsToDB(reviewCount, appInfo);
            }

            System.out.println(appInfo.getName() + ": finished crawling. Crawled reviews: " + reviewCount);
            reviewsCounts[looper] = reviewCount;
            looper++;
            googlePlayStoreCrawler.setDateOfLastCrawlIsReached(false);
        }
        //finish print out
        printAppCrawlEnd(reviewsCounts);
    }

    private void printAppCrawlStart(AppInfo appInfo) {
        System.out.println(" ************** Start ReviewCrawler for App " + appInfo.getName() + " **************");
    }

    /**
     * Write all the reviews the crawler found on one page into DB
     * @param reviewCount number of total found reviews of the app
     * @param appInfo the corresponding appInfos
     * @return
     */
    private int writeCurrentReviewsToDB(int reviewCount, AppInfo appInfo) {
        List<Review> reviewsOfPage = googlePlayStoreCrawler.getReviewsOfPage();
        int counter = reviewCount;
        ArDocDecider arDocDecider = ArDocDecider.getInstance();
        if (reviewsOfPage.size() > 0) {
            counter = counter + reviewsOfPage.size();
            reviewsOfPage = arDocDecider.setArDocAndAppName(reviewsOfPage, appInfo.getName());
            dbWriter.writeReviewsToDB(reviewsOfPage);
        }
        return counter;
    }

    private void printAppCrawlEnd(int[] reviewsCounts) {
        System.out.println("===================================================================================================" +
                "===============================================================================================================");
        for (int i = 0; i < reviewsCounts.length; i++) {
            if (i % 4 == 0) {
                System.out.println("\n");
            }

            System.out.print(appInfos.get(i).getName() + ", new Reviews: " + reviewsCounts[i] + " || ");


        }
        System.out.println("\n=================================================================================================" +
                "=================================================================================================================");
    }


    public static void main(String[] args) throws InterruptedException {
        argumentCheck(args);

        String csvPath = args[0];
        UsingBrowser browser = UsingBrowser.valueOf(args[1].toUpperCase());
        String dbName;
        if(args[2].equals("test")) {
            dbName = DBNAME_TEST;
        } else {
            dbName = DBNAME;
        }

        One_Crawling mainAppReviewsToDB = new One_Crawling(csvPath, browser, dbName);
        mainAppReviewsToDB.writeAppInfosToDB();
        mainAppReviewsToDB.crawlForEachAPP();

    }

    private static void argumentCheck(String[] args) {
        if(args.length != 3) {
            System.out.println("Wrong number of arguments! Use of program: java <appName> <csv-path> <browser> <mode>");
            System.out.println("Whereas <browser> is either 'safari' or 'firefox'");
            System.out.println("and <mode> is either 'test' or 'prod'");
            System.exit(0);
        }

        File f = new File(args[0]);
        if (!f.exists() || f.isDirectory()) {
            System.err.println("The given filepath is not correct");
            System.exit(0);
        }

        if(!FilenameUtils.getExtension(args[0]).equals("csv")) {
            System.err.println("The given file is not in csv-format");
            System.exit(0);
        }

        if(!args[1].equals("safari") && !args[1].equals("firefox")) {
            System.out.println("Wrong browser! Use one of the following as second argument:");
            System.out.println("\t safari, firefox");
            System.exit(0);
        }

        if(!args[2].equals("test") && !args[1].equals("prod")) {
            System.out.println("Wrong mode! Use one of the following as third argument:");
            System.out.println("\t test, prod");
            System.exit(0);
        }
    }
}

