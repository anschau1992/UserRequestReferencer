package subclassification;

import helper.Constants;
import edu.stanford.nlp.trees.TypedDependency;
import preclassification.PreClassification;
import helper.Review;
import subclassification.stanfordNLP.NLPType;
import subclassification.stanfordNLP.NLPTypeDecider;
import subclassification.stanfordNLP.StanfordNLP;
import subclassification.vectorSpaceModel.VectorSpaceModel;

import java.util.List;

public class SubClassifier implements Constants {

    String dbName;
    String collectionName;
    PreClassification preClassification;

    public SubClassifier(String dbName, PreClassification preClassification) {
        this.dbName = dbName;
        this.collectionName = REVIEW_COLLLECTION;
        this.preClassification = preClassification;
    }

    public List<Review> subClassify() throws Exception {
        List<String> matrixTerms;
        List<Review> reviews;
        List<Review> trainingSet;

        //1. Read out reviews with preClassification-filter of DB
        ReviewExportSubClass exporter = new ReviewExportSubClass(MONGODB_PORT, DBNAME,
                REVIEW_COLLLECTION, preClassification);
        reviews = exporter.createReviewSubClassInfo();
        //2. Read out trainingset with preClassification-filter of DB
        ReviewExportSubClass trainingSetExporter = new ReviewExportSubClass(MONGODB_PORT, DBNAME,
                TRAININGSET_COLLECTION, preClassification);
        trainingSet = trainingSetExporter.createReviewSubClassInfo();

        //3. SentimentScore & NLP
        reviews = setSentimentScoreAndNLPType(reviews);
        trainingSet = setSentimentScoreAndNLPType(trainingSet);

        //4. VSM: TermsVector
        VectorSpaceModel vsm = new VectorSpaceModel(reviews, trainingSet);
        matrixTerms = vsm.getMatrixTerms();
        reviews = vsm.getReviewTermVector();
        trainingSet = vsm.getTrainingSetTermVector();

        //5. Weka: Subclassification
        WekaCreator wekaCreator = new WekaCreator(matrixTerms, reviews, trainingSet, preClassification);
        reviews = wekaCreator.fillDistributionValues();
        SubClassComparer subClassComparer = new SubClassComparer();
        reviews = subClassComparer.compareWekaSubClass(reviews);
        System.out.println("Subclassification completed");

        return reviews;
    }

    private static List<Review> setSentimentScoreAndNLPType(List<Review> review) {
        StanfordNLP stanfordNLP = new StanfordNLP();
        for (Review reviewInfo : review) {
            String reviewText = reviewInfo.getReviewText();

            //set Sentiment-Score
            int sentimentScore = stanfordNLP.getSentimentScore(reviewText);
            reviewInfo.setSentimentScore(sentimentScore);

            //set NLPType
            TypedDependency[] typedDependencies = stanfordNLP.getRelevantTypedDependencies(RELEVANT_TYPED_DEPENDENCIES, reviewText);
            NLPTypeDecider nlpTypeDecider = new NLPTypeDecider();
            NLPType nlpType = nlpTypeDecider.getNLPType(typedDependencies);
            reviewInfo.setNlpType(nlpType);
        }

        return review;
    }
}
