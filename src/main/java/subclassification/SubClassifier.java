package subclassification;

import crawler.Constants;
import edu.stanford.nlp.trees.TypedDependency;
import preclassification.PreClassification;
import subclassification.stanfordNLP.NLPType;
import subclassification.stanfordNLP.NLPTypeDecider;
import subclassification.stanfordNLP.StanfordNLP;
import subclassification.vectorSpaceModel.VectorSpaceModel;

import java.io.IOException;
import java.util.List;

public class SubClassifier implements Constants{


    public static void main( String [] args) throws IOException {
        PreClassification choosenPreClassification;
        try{
            choosenPreClassification = PreClassification.valueOf(args[0]);
        }catch (IllegalArgumentException e) {
            throw new RuntimeException(
                    "Invalid value for enum " + args[0]);
        }

        List<String> matrixTerms;
        List<ReviewSubClassInfo> reviewInfos;
        List <ReviewSubClassInfo> trainingSet;

        //1. Read out reviews with preClassification-filter of DB
        ReviewExportSubClass exporter = new ReviewExportSubClass(MONGODB_PORT, DBNAME_TEST,
                REVIEW_COLLLECTION_TEST, choosenPreClassification);
        reviewInfos = exporter.createReviewSubClassInfo();
        //2. Read out trainingset with preClassification-filter of DB
        ReviewExportSubClass trainingSetExporter = new ReviewExportSubClass(MONGODB_PORT, DBNAME_TEST,
                TRAININGSET_COLLECTION, choosenPreClassification);
        trainingSet = trainingSetExporter.createReviewSubClassInfo();

        //3. SentimentScore & NLP
        reviewInfos = setSentimentScoreAndNLPType(reviewInfos);
        trainingSet = setSentimentScoreAndNLPType(trainingSet);

        //4. VSM: TermsVector
        VectorSpaceModel vsm = new VectorSpaceModel(reviewInfos, trainingSet);
        matrixTerms = vsm.getMatrixTerms();
        reviewInfos = vsm.getReviewTermVector();
        trainingSet = vsm.getTrainingSetTermVector();




        System.out.println(reviewInfos);
    }

    public static List<ReviewSubClassInfo> setSentimentScoreAndNLPType(List<ReviewSubClassInfo> reviewInfos) {
        StanfordNLP stanfordNLP = new StanfordNLP();
        for (ReviewSubClassInfo reviewInfo: reviewInfos) {
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

        return reviewInfos;
    }
}
