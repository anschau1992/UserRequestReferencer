package preclassification;

import com.mongodb.*;
import crawler.Constants;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PreclassificationDBImport implements Constants{

    DBCollection collection;

    private PreclassificationDBImport(int portnumb, String dbName, String collectionName) {
        MongoClient mongoClient = new MongoClient("localhost", portnumb);
        DB db = mongoClient.getDB(dbName);
        this.collection = db.getCollection(collectionName);
    }

    private void importPreClassificationIntoDB() {
        try{
            BufferedReader br = new BufferedReader(new FileReader(PRECLASSIFICATION_EXPORT_PATH));
            String line = br.readLine();
            int lineCount = 0;
            while (line != null) {
                lineCount++;
                try {
                    //use separator
                    String [] review = line.split(CSV_SPLIT);
                    addPreClassificationInDB(review, lineCount);

                }catch (Exception e) {
                    System.err.println("CSVReader: could not read in line #"
                            + lineCount + "\t"+ e);
                }
                line = br.readLine();
            }
            System.out.println("Successfully imported the preclassification-values into the DB");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addPreClassificationInDB(String[] review, int lineNumber) {
        String id = review[0];
        PreClassification preClassification = null;
        try {
            preClassification = PreClassification.valueOf(review[2]);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(
                    "Invalid value for enum PreClassification at Line " + lineNumber);
        }

        if(preClassification != null) {
            BasicDBObject filter = new BasicDBObject().append("_id", new ObjectId(id));
            BasicDBObject newPreClassification = new BasicDBObject();
            newPreClassification.put("preclassification", preClassification.toString());
            BasicDBObject updateOperationDocument = new BasicDBObject("$set", newPreClassification);
            collection.update(filter, updateOperationDocument);
        }
    }

    public static void main(String args[]) {
        PreclassificationDBImport preclassificationDBImport = new PreclassificationDBImport(MONGODB_PORT, DBNAME, REVIEW_COLLLECTION);
        preclassificationDBImport.importPreClassificationIntoDB();
    }
}
