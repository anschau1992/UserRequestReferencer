package helper;

import crawler.AppInfo;
import crawler.Constants;
import crawler.DBWriter;
import review.Review;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to read in the training-set into DB
 * Once the training set is in DB, there is no need for further use of it
 */
public class TrainingSetToDB implements Constants{
    public static void main(String[] args) {
        TrainingSetCSVReader reader = new TrainingSetCSVReader();
        List<Review> trainingSetReviews = reader.transformToReviews(TRAININGSET_CSV_PATH);

        DBWriter dbWriter = new DBWriter(DBNAME_TEST,TRAININGSET_COLLECTION, APP_INFOS_COLLLECTION);
        dbWriter.writeReviewsToDB(trainingSetReviews);
    }
}
