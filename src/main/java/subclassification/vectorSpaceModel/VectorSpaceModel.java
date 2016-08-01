package subclassification.vectorSpaceModel;

import crawler.Constants;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import subclassification.ReviewSubClassInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VectorSpaceModel implements Constants {
    TermDocumentMatrixBuilder termDocumentMatrixBuilder;
    int reviewsNumber;
    int trainingSetNumber;
    List<ReviewSubClassInfo> reviewInfos;
    List<ReviewSubClassInfo> trainingSet;

    RealMatrix termMatrix;

    public VectorSpaceModel(List<ReviewSubClassInfo> reviewInfos, List <ReviewSubClassInfo> trainingSet) throws IOException {
        this.reviewInfos = reviewInfos;
        this.reviewsNumber = reviewInfos.size();
        this.trainingSet = trainingSet;
        this.trainingSetNumber = trainingSet.size();
        String[] reviewTexts = new String[reviewsNumber + trainingSetNumber];

        for(int i = 0; i < reviewsNumber; i++) {
            reviewTexts[i] = reviewInfos.get(i).getReviewText();
        }
        for(int j= 0; j < trainingSetNumber; j++) {
            reviewTexts[j + reviewsNumber] = trainingSet.get(j).getReviewText();
        }

        //index reviews in lucene-filetree
        Indexer indexer = new Indexer(INDEXER_DIRECTORY);
        indexer.deleteAll();
        indexer.index(reviewTexts);
        indexer.close();
        this.termDocumentMatrixBuilder = new TermDocumentMatrixBuilder(INDEXER_DIRECTORY);
        termMatrix = termDocumentMatrixBuilder.buildTermDocumentMatrix();
    }

    public  List<String>  getMatrixTerms() {
        List<String> matrixTerms =  termDocumentMatrixBuilder.getMatrixTerms();
        return matrixTerms;
    }

    public List<ReviewSubClassInfo> getReviewTermVector() throws IOException {

        if((reviewsNumber + trainingSetNumber) == termMatrix.getColumnDimension()) {
            for(int i = 0; i < reviewsNumber; i++) {
                RealVector termsVector = termMatrix.getColumnVector(i);
                reviewInfos.get(i).setTermsVector(termsVector);
            }
        } else {
            System.err.println("The reviewInfos and Realmatrix has not same dimensionlength!");
        }
        return reviewInfos;
    }

    public List<ReviewSubClassInfo> getTrainingSetTermVector() throws IOException {


        if((reviewsNumber + trainingSetNumber) == termMatrix.getColumnDimension()) {
            for(int i = 0; i < trainingSetNumber; i++) {
                RealVector termsVector = termMatrix.getColumnVector(i+ reviewsNumber);
                trainingSet.get(i).setTermsVector(termsVector);
            }
        } else {
            System.err.println("The reviewInfos and Realmatrix has not same dimensionlength!");
        }
        return trainingSet;
    }
}
