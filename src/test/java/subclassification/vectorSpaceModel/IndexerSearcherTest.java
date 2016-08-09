package subclassification.vectorSpaceModel;

import helper.Constants;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class IndexerSearcherTest implements Constants{

    Indexer indexer;
    Searcher searcher;
    @Before
    public void setupIndexer() throws IOException {
        searcher = new Searcher();
        indexer = new Indexer(INDEXER_DIRECTORY_TEST);
        indexer.deleteAll();

    }

    @Test
    public void testIndexing() throws IOException, ParseException {
        String [] reviews = {"I really love this app", "This app change is the worst I've ever seen"};
        indexer.index(reviews);
        assertEquals(2, indexer.getNumberOfDocuments());
        indexer.close();
    }

    @Test
    public void testSearcher() throws IOException, ParseException {
        List<String> scoringReviews = searcher.search(INDEXER_DIRECTORY_TEST, "+app", 20);
        assertEquals("ScoringReviews has length 2",2, scoringReviews.size());
        assertEquals("ScoringReviews.get(0) contains proper sentence","I really love this app", scoringReviews.get(0));
        assertEquals("ScoringReviews.get(1) contains proper sentence", "This app change is the worst I've ever seen", scoringReviews.get(1));

        List<String> scoringReviews2 = searcher.search(INDEXER_DIRECTORY_TEST, "+change", 20);
        assertEquals(1, scoringReviews2.size());

        searcher.close();
    }
}
