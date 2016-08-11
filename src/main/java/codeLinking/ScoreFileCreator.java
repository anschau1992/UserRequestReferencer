package codeLinking;

import helper.Review;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.*;

public class ScoreFileCreator {

    PrintWriter writer;
    public ScoreFileCreator(String fileName) throws IOException {
        writer = new PrintWriter(new FileWriter(fileName, true));
    }

    public void writeScore(Review review, TopDocs hits, IndexSearcher searcher) throws IOException {

        writer.println("ID;App;Preclassification;Subclassification; Review");
        writer.println(review.getId() + ";"+ review.getApp() + ";"
                + review.getPreClassification() + ";" + review.getSubClassification() + ";"
                + review.getReviewText() + ";");

        for(ScoreDoc scoreDoc: hits.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            writer.println(doc.get("id"));
        }
        writer.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        writer.println();
        writer.flush();
    }

    public void closeWriter() {
        writer.close();
    };
}
