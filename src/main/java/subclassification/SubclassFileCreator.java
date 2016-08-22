package subclassification;

import helper.Review;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class SubclassFileCreator {
    PrintWriter writer;
    public SubclassFileCreator(String fileName) throws IOException {
        writer = new PrintWriter(new FileWriter(fileName, true));
    }


    public void writeCorrectClassified(Review review) {
        writer.println("====================================================================================================================");
        writer.println("Review: \"" + review.getReviewText() + "\"");
        writer.println("Correctly classified into subclass: " + review.getSubClassification());
        writer.println("====================================================================================================================");
        writer.flush();
    }

    public void writeWrongClassified(Review review, Map.Entry<String, Double> highestDistributionClass) {
        writer.println("====================================================================================================================");
        writer.println("Review: \"" + review.getReviewText() + "\"");
        writer.println("Wrong classification:   Weka: " + highestDistributionClass.getKey() + "\t" + "DB:" + review.getSubClassification());
        writer.println("====================================================================================================================");
        writer.flush();
    }

    public void writeNewClassified(Review review, Map.Entry<String, Double> highestDistributionClass) {
        writer.println("====================================================================================================================");
        writer.println("Review: \"" + review.getReviewText() + "\"");
        writer.println("new Subclassification: " + highestDistributionClass.getKey() + " with precision of " + highestDistributionClass.getValue());
        writer.println("====================================================================================================================");
        writer.flush();
    }

    public void writeSummary( double correctClassified, double toClassify) {
        writer.println("\n*****************************************************************************************************************************");
        writer.println("Correct classified: " + (int) correctClassified + "\t Wrong classified: " + (int) (toClassify - correctClassified));
        writer.println("Classification-Precision: " + (correctClassified / toClassify));
        writer.println("*****************************************************************************************************************************\n");
        writer.flush();
    }

    public void closeWriter() {
        writer.close();
    };
}
