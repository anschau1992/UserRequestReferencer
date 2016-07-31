package subclassification;

import crawler.Constants;
import edu.stanford.nlp.trees.TypedDependency;
import subclassification.stanfordNLP.NLPType;
import subclassification.stanfordNLP.NLPTypeDecider;
import subclassification.stanfordNLP.StanfordNLP;

import java.util.List;

public class SubClassifier implements Constants{


    public static void main( String [] args) {
        List<ReviewSubClassInfo> reviewInfos;

        //Read in reviews with preClassification of DB
        ReviewExportSubClass exporter = new ReviewExportSubClass(MONGODB_PORT, DBNAME_TEST, REVIEW_COLLLECTION_TEST);
        reviewInfos = exporter.createReviewSubClassInfo();

        reviewInfos = setSentimentScoreAndNLPType(reviewInfos);





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
