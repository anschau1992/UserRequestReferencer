package subclassification;

import helper.Constants;
import org.apache.commons.math3.linear.RealVector;
import preclassification.PreClassification;
import helper.Review;
import subclassification.stanfordNLP.NLPType;
import subclassification.subclasses.*;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.*;
import weka.filters.unsupervised.instance.NonSparseToSparse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WekaCreator implements Constants {

    private int termsSize;
    private PreClassification preClassification;
    List<Review> reviews;

    Instances trainingSet;
    Instances testingSet;
    Classifier cModel;

    public WekaCreator(List<String> matrixterms, List<Review> reviews,
                       List<Review> trainingSetReviews, PreClassification preClassification) throws Exception {
        this.reviews = reviews;
        this.preClassification = preClassification;
        this.termsSize = matrixterms.size();

        int reviewInfosSize = reviews.size();
        int trainingSetSize = trainingSetReviews.size();

        FastVector fvWekaAttributes = createAttributes(matrixterms);

        trainingSet = createReviewSet(fvWekaAttributes, trainingSetSize, trainingSetReviews);
        testingSet = createReviewSet(fvWekaAttributes, reviewInfosSize, reviews);

        //create NoneSparseToSparse
        NonSparseToSparse sp = new NonSparseToSparse();
        sp.setInputFormat(trainingSet);
        sp.setInputFormat(testingSet);

        //add classifier-mode
        cModel = (Classifier) new J48();
        cModel.buildClassifier(trainingSet);

        //use classifier
        System.out.println("--------use classifier---------");
        for (int i = 0; i < testingSet.numInstances(); i++) {
            Instance testInstance = testingSet.instance(i);
            testInstance.setDataset(trainingSet);

            System.out.println(testInstance.classAttribute().toString());
            double[] fDistribution = cModel.distributionForInstance(testInstance);

            System.out.println("FDistribution for Instance #" + (i + 1) + ": " + fDistribution[0] + "|" + fDistribution[1]);
        }

        


    }

    private FastVector createAttributes(List<String> matrixterms) {
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

        return fvWekaAttributes;
    }

    private FastVector addTermsAsAttr(List<String> matrixterms) {
        FastVector fvWekaAttributes = new FastVector();
        for (String term : matrixterms) {
            Attribute attribute = new Attribute(term);
            fvWekaAttributes.addElement(attribute);
        }
        return fvWekaAttributes;
    }

    private Attribute nlpTypeAttribute() {
        FastVector fvnlpType = new FastVector(NLPType.values().length);
        for (NLPType type : NLPType.values()) {
            fvnlpType.addElement(type.toString());
        }
        return new Attribute("NLPType", fvnlpType);
    }

    private Attribute subclassificationAttr(PreClassification preclassification) {
        FastVector fvSubClasses = new FastVector();
        switch (preclassification) {
            case USAGE: {

                for (SUBCLASS_USAGE type : SUBCLASS_USAGE.values()) {
                    fvSubClasses.addElement(type.toString());
                }
                break;
            }
            case COMPATIBILITY: {
                for (SUBCLASS_COMPATIBILITY type : SUBCLASS_COMPATIBILITY.values()) {
                    fvSubClasses.addElement(type.toString());
                }
                break;
            }
            case RESSOURCES: {
                for (SUBCLASS_RESSOURCES type : SUBCLASS_RESSOURCES.values()) {
                    fvSubClasses.addElement(type.toString());
                }
                break;
            }
            case PROTECTION: {
                for (SUBCLASS_PROTECTION type : SUBCLASS_PROTECTION.values()) {
                    fvSubClasses.addElement(type.toString());
                }
                break;
            }
            case PRICING: {
                for (SUBCLASS_PRICING type : SUBCLASS_PRICING.values()) {
                    fvSubClasses.addElement(type.toString());
                }
                break;
            }
        }
        return new Attribute("subclassification", fvSubClasses);
    }

    private Instances createReviewSet(FastVector fvWekaAttributes, int reviewSetSize, List<Review> reviews) {
        Instances reviewSet = new Instances(preClassification.toString(), fvWekaAttributes, reviewSetSize);
        reviewSet = fillReviewSet(reviewSet, reviews, fvWekaAttributes);
        reviewSet.setClassIndex(termsSize + 3);

        return reviewSet;
    }

    private Instances fillReviewSet(Instances trainingSet, List<Review> reviewsSet, FastVector fvWekaAttributes) {
        for (Review review : reviewsSet) {
            Instance trainingInstance = new Instance(trainingSet.numAttributes());

            RealVector terms = review.getTermsVector();
            //fill in attributes for a review
            for (int i = 0; i < termsSize; i++) {
                trainingInstance.setValue((Attribute) fvWekaAttributes.elementAt(i), terms.getEntry(i));
            }
            trainingInstance.setValue((Attribute) fvWekaAttributes.elementAt(termsSize), review.getSentimentScore());
            trainingInstance.setValue((Attribute) fvWekaAttributes.elementAt(termsSize + 1), review.getNlpType().toString());
            trainingInstance.setValue((Attribute) fvWekaAttributes.elementAt(termsSize + 2), review.getRatingStars());

            //copy subClassification or define it missing if not set
            String subClass = review.getSubClassification();
            if (subClass != null && !subClass.equals(ARFF_EMPTY_SIGN)) {
                trainingInstance.setValue((Attribute) fvWekaAttributes.elementAt(termsSize + 3), subClass);
            } else {
                trainingInstance.setValue((Attribute) fvWekaAttributes.elementAt(termsSize + 3), Instance.missingValue());
            }


            trainingSet.add(trainingInstance);
        }
        return trainingSet;
    }

    public List<Review> fillDistributionValues() throws Exception {
        //use classifier on testSet

        for (int i = 0; i < testingSet.numInstances(); i++) {
            Instance testInstance = testingSet.instance(i);
            testInstance.setDataset(trainingSet);

            double[] fDistribution = cModel.distributionForInstance(testInstance);
            Map fDistributionMap = new HashMap();
            for(int j=0; j < fDistribution.length; j++) {
                fDistributionMap.put(testInstance.classAttribute().value(j),fDistribution[j]);
            }
            reviews.get(i).setSubClassFDistribution(fDistributionMap);
        }
        return reviews;
    }
}
