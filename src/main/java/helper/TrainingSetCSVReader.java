package helper;

import preclassification.PreClassification;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TrainingSetCSVReader implements Constants {
    BufferedReader br;
    String line;
    String csvSplitBy = ";";

    public TrainingSetCSVReader() {
        line = "";
    }

    public List<Review> transformToReviews(String csvFile) {
        List <Review> reviews = new ArrayList<Review>();
        try{
            br = new BufferedReader(new FileReader(csvFile));

            //skip headers
            line = br.readLine();
            line = br.readLine();

            int lineCount = 0;
            while (line != null) {
                lineCount++;
                try {
                    //use separator
                    String [] reviewElement = line.split(CSV_SPLIT);
                    Review review = createReview(reviewElement);
                    reviews.add(review);
                } catch (Exception e) {
                    System.err.println("CSVReader: could not read in line #"
                            + lineCount + "\t"+ e);
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    private Review createReview(String[] reviewElement) {
        String app = reviewElement[1];
        String appVersion = reviewElement[2];
        String reviewText = reviewElement[3];
        String reviewAuthor = reviewElement[4];

        //date parser
        DateFormat formatter = new SimpleDateFormat("dd.mm.yy", Locale.ENGLISH);
        Date date = null;
        try {
            date = formatter.parse(reviewElement[5]);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int ratingStar = Integer.parseInt(reviewElement[6]);
        ArDocClassification arDocClassification = ArDocClassification.valueOf(reviewElement[7]);
        PreClassification preClassification = PreClassification.valueOf(reviewElement[8]);
        String subClassification = reviewElement[9];

        //fill in Review
        Review review = new Review(reviewAuthor,reviewText, date, ratingStar, appVersion);
        review.setApp(app);
        review.setArDocClassification(arDocClassification);
        review.setPreClassification(preClassification);
        review.setSubClassification(subClassification);

        return review;
    }


}
