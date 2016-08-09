package subclassification.vectorSpaceModel;

import helper.Constants;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class TermDocumentMatrixBuilderTest implements Constants {
    Indexer indexer;
    Searcher searcher;

    @Before
    public void setupIndex() throws IOException {
        searcher = new Searcher();
        indexer = new Indexer(INDEXER_DIRECTORY_TEST);
        indexer.deleteAll();

        //load index
        String[] reviews = {"I really love this app", "This app change is the best I've ever seen", "The best app ever", "Love this app for ever and ever"};
        indexer.index(reviews);
        indexer.close();
    }

    @Test
    public void testTermDocumentMatrix() throws IOException {
        TermDocumentMatrixBuilder termDocumentMatrixBuilder = new TermDocumentMatrixBuilder(INDEXER_DIRECTORY_TEST);

        RealMatrix matrix = termDocumentMatrixBuilder.buildTermDocumentMatrix();
        List<String> matrixTerms = termDocumentMatrixBuilder.getMatrixTerms();

        termDocumentMatrixBuilder.closeReader();


        printTermDocumentMatrix(matrix, matrixTerms);
    }

    private void printTermDocumentMatrix(RealMatrix matrix, List<String> matrixTerms) {

        List<String> matrixTermsWithHead = matrixTerms;
        matrixTermsWithHead.add(0, "X");

        Object[][] table = new Object[matrix.getColumnDimension()+1][matrix.getRowDimension()+1];
        String[] terms = matrixTermsWithHead.toArray(new String[matrixTermsWithHead.size()]);


        table[0] = matrixTermsWithHead.toArray(new String[matrixTermsWithHead.size()]);

        for(int i = 0; i < matrix.getColumnDimension(); i++) {
            String docName = "Doc" + i;
            table[i+1][0] = docName;
            double [] column = matrix.getColumnVector(i).toArray();
            for(int j = 0; j < column.length; j++) {
                DecimalFormat df = new DecimalFormat("#.000000");
                table[i+1][j+1] = df.format(column[j]);
            }
        }

        String format = "";
        for (String term: terms) {
            format = format.concat("%-15s");
        }
        format = format.concat("\n");

        for (final Object[] row : table) {

            System.out.format(format, row);
        }
    }
}
