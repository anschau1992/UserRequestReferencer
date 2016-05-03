package server;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import crawler.AppInfo;

import org.bson.Document;
import org.junit.Test;

import static net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime.eq;


public class DBTest {
    String dbname = "review";

    MongoClient mongoClient = new MongoClient();
    MongoDatabase db =  mongoClient.getDatabase(this.dbname);


    @Test
    public void testDB () {
       MongoCollection coll = db.getCollection(dbname);


        System.out.println(coll.count());
    }


}
