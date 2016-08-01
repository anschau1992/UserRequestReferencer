package subclassification.vectorSpaceModel;

import crawler.Constants;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Indexer implements Constants{

    private IndexWriter writer;

    public Indexer (String indexDir) throws IOException {
        Path path = Paths.get(indexDir);
        Directory dir = FSDirectory.open(path);
        IndexWriterConfig config = new IndexWriterConfig(new CombinedAnalyzer());
        writer = new IndexWriter(dir, config);
    }

    public int index(String [] reviews) throws IOException {
        for (String review : reviews) {
            indexReview(review);
        }
        return writer.numDocs();
    }

    private void indexReview(String review) throws IOException {
        //System.out.println("Indexing Review: " + review);
        Document doc = getDocument(review);
        writer.addDocument(doc);
    }

    private Document getDocument(String review) {
        Document doc = new Document();
        doc.add(new Field(REVIEW_TEXT, review, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS));
        return doc;
    }

    public void deleteAll() throws IOException {
        writer.deleteAll();
    }

    public void close() throws IOException {
        writer.close();
    }

    public int getNumberOfDocuments() {
        return writer.numDocs();
    }
}
