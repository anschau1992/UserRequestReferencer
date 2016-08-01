package subclassification;

import preclassification.PreClassification;
import subclassification.stanfordNLP.NLPType;
import subclassification.subclasses.*;
import weka.core.Attribute;
import weka.core.FastVector;

import java.util.List;

import static preclassification.PreClassification.*;

public class WekaCreator{

    List<String> matrixterms;
    List<ReviewSubClassInfo> reviewInfos;
    List <ReviewSubClassInfo> trainingSet;

    int termsSize;
    int reviewInfosSize;
    int trainingSetSize;


    public WekaCreator(List<String> matrixterms, List<ReviewSubClassInfo> reviewInfos,
                       List <ReviewSubClassInfo> trainingSet, PreClassification preClassification) {
        this.matrixterms = matrixterms;
        this.reviewInfos = reviewInfos;
        this.trainingSet = trainingSet;

        this.termsSize = matrixterms.size();
        this.reviewInfosSize = reviewInfos.size();
        this.trainingSetSize = trainingSet.size();
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


}
