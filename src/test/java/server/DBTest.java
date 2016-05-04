package server;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import crawler.AppInfo;
import crawler.Constants;
import org.bson.Document;
import org.junit.Test;




public class DBTest implements Constants{
    String dbname = "review";
    String dbCollection = "review";

    MongoClient mongoClient = new MongoClient();
    DB db =  mongoClient.getDB(this.dbname);

    @Test
    public void printDBCount () {
        DBCollection coll = db.getCollection(dbCollection);

        System.out.println("Number of reviews: " + coll.count());
    }

    @Test
    public void printAppsAndCount() {

        for(int i = 0; i < APP_INFOS.length; i++) {
            DBCollection coll = db.getCollection(dbCollection);
            BasicDBObject doc = new BasicDBObject("app", APP_INFOS[i].getName());
            DBCursor dbCursor = coll.find(doc);




            System.out.println(APP_INFOS[i].getName() + ", # of reviews: " + dbCursor.count());
            }
        }
    }


