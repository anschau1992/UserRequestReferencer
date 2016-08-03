package subclassification;

import crawler.Constants;
import edu.stanford.nlp.trees.TypedDependency;
import preclassification.PreClassification;
import subclassification.stanfordNLP.NLPType;
import subclassification.stanfordNLP.NLPTypeDecider;
import subclassification.stanfordNLP.StanfordNLP;
import subclassification.vectorSpaceModel.VectorSpaceModel;

import java.util.List;
import java.util.Map;

public class SubClassifier implements Constants {


    public static void main(String[] args) throws Exception {
        PreClassification choosenPreClassification;
        try {
            choosenPreClassification = PreClassification.valueOf(args[0]);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(
                    "Invalid value for enum " + args[0]);
        }

        List<String> matrixTerms;
        List<ReviewSubClassInfo> reviewInfos;
        List<ReviewSubClassInfo> trainingSet;

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

        //5. Weka: Subclassification
        WekaCreator wekaCreator = new WekaCreator(matrixTerms, reviewInfos, trainingSet, choosenPreClassification);
        reviewInfos = wekaCreator.fillDistributionValues();
        reviewInfos = setSubClass(reviewInfos);
        System.out.println(reviewInfos);
    }

    public static List<ReviewSubClassInfo> setSentimentScoreAndNLPType(List<ReviewSubClassInfo> reviewInfos) {
        StanfordNLP stanfordNLP = new StanfordNLP();
        for (ReviewSubClassInfo reviewInfo : reviewInfos) {
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

    private static List<ReviewSubClassInfo> setSubClass(List<ReviewSubClassInfo> reviewInfos) {
        double toClassify = 0;
        double correctClassified = 0;

        for (ReviewSubClassInfo reviewInfo : reviewInfos) {
            Map.Entry<String, Double> highestDistributionClass = getHighestNumbPosition(reviewInfo.getSubClassFDistribution());
            //subClass not defined
            if (reviewInfo.getSubClassification().equals(ARFF_EMPTY_SIGN) || reviewInfo.getSubClassification() == null) {
                reviewInfo.setSubClassification(highestDistributionClass.getKey());
                printNewSubclass(highestDistributionClass, reviewInfo);
            } else {
                toClassify++;
                if(reviewInfo.getSubClassification().equals(highestDistributionClass.getKey())) {
                    correctClassified++;
                    System.out.println("====================================================================================================================");
                    System.out.println("Review: \""+ reviewInfo.getReviewText() +"\"");
                    System.out.println("Correctly classified into subclass: "+ reviewInfo.getSubClassification());
                    System.out.println("====================================================================================================================");
                } else {
                    System.out.println("====================================================================================================================");
                    System.out.println("Review: \""+ reviewInfo.getReviewText() +"\"");
                    System.out.println("Wrong classification:   Weka: "+ highestDistributionClass.getKey()+ "\t" + "DB:" + reviewInfo.getSubClassification());
                    System.out.println("====================================================================================================================");
                }
            }
        }

        if(toClassify > 0.0) {
            System.out.println("\n*****************************************************************************************************************************");
            System.out.println("Correct classified: "+ (int) correctClassified + "\t Wrong classified: "+ (int) (toClassify-correctClassified));
            System.out.println("Classification-Precision: "+ (correctClassified/toClassify));
            System.out.println("*****************************************************************************************************************************\n");
        }
        return reviewInfos;
    }

    private static Map.Entry<String, Double> getHighestNumbPosition(Map<String, Double> fDistribution) {
        Map.Entry<String, Double> maxEntry = null;
        for (Map.Entry<String, Double> entry : fDistribution.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        return maxEntry;
    }

    private static void printNewSubclass(Map.Entry<String, Double> highestDistributionClass, ReviewSubClassInfo reviewInfo) {
        System.out.println("====================================================================================================================");
        System.out.println("Review: \""+ reviewInfo.getReviewText() +"\"");
        System.out.println("new Subclassification: "+ highestDistributionClass.getKey() +" with precision of "+ highestDistributionClass.getValue());
        System.out.println("====================================================================================================================");
    }
}
