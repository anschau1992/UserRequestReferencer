package subclassification.vectorSpaceModel;

import helper.Constants;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import helper.Review;

import java.io.IOException;
import java.util.List;

public class VectorSpaceModel implements Constants {
    TermDocumentMatrixBuilder termDocumentMatrixBuilder;
    int reviewsNumber;
    int trainingSetNumber;
    List<Review> reviews;
    List<Review> trainingSet;

    RealMatrix termMatrix;

    public VectorSpaceModel(List<Review> reviews, List <Review> trainingSet) throws IOException {
        this.reviews = reviews;
        this.reviewsNumber = reviews.size();
        this.trainingSet = trainingSet;
        this.trainingSetNumber = trainingSet.size();
        String[] reviewTexts = new String[reviewsNumber + trainingSetNumber];

        for(int i = 0; i < reviewsNumber; i++) {
            reviewTexts[i] = reviews.get(i).getReviewText();
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

    public List<Review> getReviewTermVector() throws IOException {

        if((reviewsNumber + trainingSetNumber) == termMatrix.getColumnDimension()) {
            for(int i = 0; i < reviewsNumber; i++) {
                RealVector termsVector = termMatrix.getColumnVector(i);
                reviews.get(i).setTermsVector(termsVector);
            }
        } else {
            System.err.println("The reviews and Realmatrix has not same dimensionlength!");
        }
        return reviews;
    }

    public List<Review> getTrainingSetTermVector() throws IOException {


        if((reviewsNumber + trainingSetNumber) == termMatrix.getColumnDimension()) {
            for(int i = 0; i < trainingSetNumber; i++) {
                RealVector termsVector = termMatrix.getColumnVector(i+ reviewsNumber);
                trainingSet.get(i).setTermsVector(termsVector);
            }
        } else {
            System.err.println("The reviews and Realmatrix has not same dimensionlength!");
        }
        return trainingSet;
    }
}
