package helper;

import crawler.Constants;
import crawler.DBWriter;

import java.util.List;

/**
 * This class is used to read in the training-set into DB
 * Once the training set is in DB, there is no need for further use of it
 */
public class TrainingSetToDB implements Constants{
    private String dbName;
    public TrainingSetToDB(boolean testMode) {
        if(testMode) {
            dbName = DBNAME_TEST;
        } else{
            dbName = DBNAME;
        }
    }

    public void  writeTrainingSetIntoDB() {
        TrainingSetCSVReader reader = new TrainingSetCSVReader();
        List<Review> trainingSetReviews = reader.transformToReviews(TRAININGSET_CSV_PATH);
        DBWriter dbWriter = new DBWriter(this.dbName,TRAININGSET_COLLECTION, APP_INFOS_COLLLECTION);
        dbWriter.writeReviewsToDB(trainingSetReviews);
    }
}
