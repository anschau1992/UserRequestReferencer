package subclassification;

import org.apache.commons.math3.linear.RealVector;
import preclassification.PreClassification;
import subclassification.stanfordNLP.NLPType;
import subclassification.subclasses.*;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.*;

import java.util.List;

public class WekaCreator{

    List<String> matrixterms;
    List<ReviewSubClassInfo> reviewInfos;
    List <ReviewSubClassInfo> trainingSetReviews;

    int termsSize;
    int reviewInfosSize;
    int trainingSetSize;


    public WekaCreator(List<String> matrixterms, List<ReviewSubClassInfo> reviewInfos,
                       List <ReviewSubClassInfo> trainingSetReviews, PreClassification preClassification) throws Exception {
        this.matrixterms = matrixterms;
        this.reviewInfos = reviewInfos;
        this.trainingSetReviews = trainingSetReviews;

        this.termsSize = matrixterms.size();
        this.reviewInfosSize = reviewInfos.size();
        this.trainingSetSize = trainingSetReviews.size();
        FastVector fvWekaAttributes = addTermsAsAttr(matrixterms);

        //add all other attributes
        Attribute sentimentAttr = new Attribute("sentiment");
        Attribute nlpTypeAttr = nlpTypeAttribute();
        Attribute ratingAttr = new Attribute("ratingStars");
        Attribute subClassAttr = subclassificationAttr(preClassification);

        fvWekaAttributes.addElement(sentimentAttr);
        fvWekaAttributes.addElement(nlpTypeAttr);
        fvWekaAttributes.addElement(ratingAttr);
        fvWekaAttributes.addElement(subClassAttr);

        //create training set
        Instances trainingSet = new Instances(preClassification.toString() + " training-set", fvWekaAttributes, trainingSetSize);
        trainingSet = fillReviewSet(trainingSet, trainingSetReviews, fvWekaAttributes);

        trainingSet.setClassIndex(termsSize + 3);

        //add classifier-mode
        Classifier cModel = (Classifier) new J48();
        cModel.buildClassifier(trainingSet);

        //create testing set
        Instances testingSet = new Instances(preClassification.toString() + " testing.set", fvWekaAttributes, reviewInfosSize);
        testingSet = fillReviewSet(testingSet, reviewInfos, fvWekaAttributes);

        //set class attribute
        testingSet.setClassIndex(termsSize + 3);

        //Test the model
        Evaluation eTest = new Evaluation(trainingSet);
        eTest.evaluateModel(cModel, testingSet);
        //print in Weka-Exlporer:
        String strSummary = eTest.toSummaryString();
        System.out.println(strSummary);

        //get confusionmatrix
        double [][] cmMatrix = eTest.confusionMatrix();
    }

    private FastVector addTermsAsAttr(List<String> matrixterms) {
        FastVector fvWekaAttributes = new FastVector();
        for (String term: matrixterms) {
            Attribute attribute = new Attribute(term);
            fvWekaAttributes.addElement(attribute);
        }
        return fvWekaAttributes;
    }

    private Attribute nlpTypeAttribute () {
        FastVector fvnlpType = new FastVector(NLPType.values().length);
        for(NLPType type: NLPType.values()) {
            fvnlpType.addElement(type.toString());
        }
        return new Attribute("NLPType", fvnlpType);
    }

    private Attribute subclassificationAttr(PreClassification preclassification) {
        FastVector fvSubClasses = new FastVector();
        switch (preclassification) {
            case USAGE: {

                for(SUBCLASS_USAGE type : SUBCLASS_USAGE.values()) {
                    fvSubClasses.addElement(type.toString());
                }
            }
            case COMPATIBILITY: {
                for(SUBCLASS_COMPATIBILITY type : SUBCLASS_COMPATIBILITY.values()) {
                    fvSubClasses.addElement(type.toString());
                }
            }
            case RESSOURCES: {
                for(SUBCLASS_RESSOURCES type : SUBCLASS_RESSOURCES.values()) {
                    fvSubClasses.addElement(type.toString());
                }
            }
            case PROTECTION: {
                for(SUBCLASS_PROTECTION type : SUBCLASS_PROTECTION.values()) {
                    fvSubClasses.addElement(type.toString());
                }
            }
            case PRICING: {
                for(SUBCLASS_PRICING type : SUBCLASS_PRICING.values()) {
                    fvSubClasses.addElement(type.toString());
                }
            }
        }
        return new Attribute("subclassification", fvSubClasses);
    }

    private Instances fillReviewSet(Instances trainingSet, List<ReviewSubClassInfo> reviewsSet, FastVector fvWekaAttributes) {
        for (ReviewSubClassInfo review: reviewsSet) {
            Instance trainingInstance = new Instance(trainingSet.numAttributes());

            RealVector terms = review.getTermsVector();
            //fill in attributes for a review
            for(int i = 0; i < termsSize; i++) {
                trainingInstance.setValue((Attribute)fvWekaAttributes.elementAt(i), terms.getEntry(i));
            }
            trainingInstance.setValue((Attribute)fvWekaAttributes.elementAt(termsSize), review.getSentimentScore());
            trainingInstance.setValue((Attribute)fvWekaAttributes.elementAt(termsSize +1), review.getNlpType().toString());
            trainingInstance.setValue((Attribute)fvWekaAttributes.elementAt(termsSize +2), review.getRatingStars());
            trainingInstance.setValue((Attribute)fvWekaAttributes.elementAt(termsSize +3), review.getSubClassification());

            trainingSet.add(trainingInstance);
        }
        return trainingSet;
    }


}
