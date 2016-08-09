package subclassification.vectorSpaceModel;

import helper.Constants;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Searcher implements Constants{
    IndexReader indexReader = null;

    public Searcher() {
    }

    public List<String> search(String indexDir, String q, int maxScore) throws IOException, ParseException {
        //iitialize searcher
        Path path = Paths.get(indexDir);
        Directory dir = FSDirectory.open(path);
        indexReader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        QueryParser parser = new QueryParser(REVIEW_TEXT, new CombinedAnalyzer());
        Query query = parser.parse(q);
        long start = System.currentTimeMillis();
        TopDocs hits = indexSearcher.search(query, maxScore);
        long end = System.currentTimeMillis();

        System.out.println("Found " + hits.totalHits +
                " document(s) (in " + (end - start) +
                " milliseconds) that matched query '" +
                q + "':");

        return getScoreReviews(hits, indexSearcher);
    }

    private List<String> getScoreReviews(TopDocs hits, IndexSearcher indexSearcher) throws IOException {
        List<String> scoreReviews = new ArrayList<String>();

        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = indexSearcher.doc(scoreDoc.doc);
            scoreReviews.add(doc.get(REVIEW_TEXT));
        }
        return scoreReviews;
    }

    public void close() throws IOException {

        indexReader.close();
    }
}
