package crawler;

import com.mongodb.*;
import crawler.Constants;
import org.junit.Test;




public class DBTest implements Constants {
    String dbname = "review";
    String dbCollection = "review";

    MongoClient mongoClient = new MongoClient();
    DB db =  mongoClient.getDB(this.dbname);

    AppInfo testApp1 = new AppInfo("Adblock Plus", "Internet",
            "org.adblockplus.adblockplussbrowser&hl=en", "https://hg.adblockplus.org/adblockplusandroid" );
    AppInfo testApp2 = new AppInfo("Xabber", "Instant Messaging",
            "com.xabber.android", "https://github.com/redsolution/xabber-android");
    AppInfo testApp3 = new AppInfo("Marine Compass", "Navigation",
            "net.pierrox.mcompass", "http://svn.pierrox.net/mcompass/trunk");

    AppInfo[] appInfos = new AppInfo[] {testApp1, testApp2, testApp3};


    @Test
    public void printDBCount () {
        DBCollection coll = db.getCollection(dbCollection);

        System.out.println("Number of reviews: " + coll.count());
    }

    @Test
    public void printAppsAndCount() {

        for(int i = 0; i < appInfos.length; i++) {
            DBCollection coll = db.getCollection(dbCollection);
            BasicDBObject doc = new BasicDBObject("app", appInfos[i].getName());
            DBCursor dbCursor = coll.find(doc);




            System.out.println(appInfos[i].getName() + ", # of reviews: " + dbCursor.count());
            }
        }
    }


