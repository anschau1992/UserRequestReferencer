package subclassification.vectorSpaceModel;


import crawler.Constants;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TermDocumentMatrixBuilder implements Constants {

    IndexReader indexReader;
    IndexSearcher indexSearcher;
    Map<String, Integer> termIdMap;
    List<String> matrixTerms = new ArrayList<String>();

    public TermDocumentMatrixBuilder(String indexDir) throws IOException {
        Path path = Paths.get(indexDir);
        indexReader = DirectoryReader.open(FSDirectory.open(path));
        indexSearcher = new IndexSearcher(indexReader);
        termIdMap = computeTermIdMap(indexReader);
    }

    /**
     *  Map term to a fix integer so that we can build document matrix later.
     *  It's used to assign term to specific row in Term-Document matrix
     */
    private Map<String, Integer> computeTermIdMap(IndexReader reader) throws IOException {
        Map<String, Integer> termIdMap = new HashMap<String, Integer>();
        int id = 0;
        Fields fields = MultiFields.getFields(reader);
        Terms terms = fields.terms(REVIEW_TEXT);
        TermsEnum itr = terms.iterator();
        BytesRef term = null;

        while ((term = itr.next()) != null) {
            String termText = term.utf8ToString();
            if (termIdMap.containsKey(termText)) {
                continue;
            }
            termIdMap.put(termText, id++);
            matrixTerms.add(termText);
        }
        return termIdMap;
    }


    public List<String> getMatrixTerms() {
        return matrixTerms;
    }

    /**
     * build term-document matrix for the given directory
     */
    public RealMatrix buildTermDocumentMatrix() throws IOException {
        //iterate through directory to work with each doc
        int col = 0;
        int numDocs = indexReader.numDocs();
        int numTerms = termIdMap.size();
        RealMatrix tdMatrix = new Array2DRowRealMatrix(numTerms, numDocs);

        for (int i = 0; i < numDocs; i++) {
            Terms termVector = indexReader.getTermVector(i, REVIEW_TEXT);
            TermsEnum itr = termVector.iterator();
            BytesRef term = null;

            //compute term weight
            while ((term = itr.next()) != null) {
                String termText = term.utf8ToString();

                int row = termIdMap.get(termText);
                long termFreq = itr.totalTermFreq();
                long docCount = indexReader.docFreq(new Term(REVIEW_TEXT,term));
                double weight = computeTfIdfWeight(termFreq, docCount, numDocs);
                tdMatrix.setEntry(row, col, weight);
            }
            col++;
        }
        return tdMatrix;
    }

    private double computeTfIdfWeight(long termFreq, long docCount, int numDocs) {
        return termFreq * Math.log10((float) numDocs/docCount);
    }

    public void closeReader() throws IOException {
        indexReader.close();
    }
}
