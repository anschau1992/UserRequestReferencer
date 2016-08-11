package codeLinking;

import helper.Constants;
import helper.Review;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import preclassification.PreClassification;
import subclassification.subclasses.SUBCLASS_PROTECTION;
import subclassification.subclasses.SUBCLASS_USAGE;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Does TopDoc-Search on all reviews corresponding to a app
 */
public class SourceCodeSearcher implements Constants{
    public SourceCodeSearcher() {

    }


    public void searchForReviews(Path path, List<Review> appReviews) throws IOException, ParseException {
        Directory dir = FSDirectory.open(path);

        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser("content", new SimpleAnalyzer());

        ScoreFileCreator fileCreator = new ScoreFileCreator(RESULT_FILE);

        for (Review review : appReviews) {
            //TODO create query with reviewtext

            SimpleAnalyzer analyzer = new SimpleAnalyzer();
            TokenStream tokenStream = analyzer.tokenStream("Fieldname", review.getReviewText());
            OffsetAttribute offsetAttribute = tokenStream.getAttribute(OffsetAttribute.class);
            CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);

            BooleanQuery bq = new BooleanQuery();
            //add all terms of review-text in query
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                int startOffset = offsetAttribute.startOffset();
                int endOffset = offsetAttribute.endOffset();
                bq.add(new TermQuery(new Term("content", termAttribute.toString())), BooleanClause.Occur.SHOULD);
            }
            tokenStream.close();

            //defines IndexCategory and integrate to search
            IndexCategory  category = defineIndexCategory(review);
            if(category != IndexCategory.OTHER) {
                bq.add(new TermQuery(new Term("category", category.toString())), BooleanClause.Occur.SHOULD);
            }


            //Query query = parser.parse(review.getReviewText());

            TopDocs hits = searcher.search(bq, SEARCH_RESULT_NUMB);

            //print result into File
            fileCreator.writeScore(review, hits, searcher);

            //Print into console
            System.out.println("++++++++++++++++++Score for review: " + review.getReviewText() +"+++++++++++++++++");
            System.out.println("Preclassification: " + review.getPreClassification() + "\t Subclassification: " + review.getSubClassification());
            for(ScoreDoc scoreDoc: hits.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                System.out.println(doc.get("id"));
            }
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        }
        System.out.println("Succeccfully finished File-linking for all reviews");
        fileCreator.closeWriter();
        reader.close();
    }

    /**
     * Decides a IndexCategory based on the Classification of a review.
     * This will be used for a boost on the corresponding files in the index
     * @param review
     * @return
     */
    private IndexCategory defineIndexCategory(Review review) {
        PreClassification preClassification = review.getPreClassification();
        String subclassification = review.getSubClassification();

        // Subclass UI ==> 'res'
        if(subclassification.equals(SUBCLASS_USAGE.UI.toString())) {
            return  IndexCategory.RESSOURCES;

        // Preclass 'RESSOURCES' ==> 'CONTENT_PROVIDER'
        } else if (preClassification == PreClassification.RESSOURCES){
            return IndexCategory.CONTENT_PROVIDER;

        // Preclass 'COMPATIBILITY' or Subclass 'PRIVACY' ===> 'ANDROID_MANIFEST'
        } else if (preClassification == PreClassification.COMPATIBILITY || subclassification.equals(SUBCLASS_PROTECTION.PRIVACY.toString()))  {
            return IndexCategory.ANDROID_MAINFEST;
        }

        else return IndexCategory.OTHER;
    }
}
