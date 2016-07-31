package subclassification.stanfordNLP;

import crawler.Constants;
import edu.stanford.nlp.trees.TypedDependency;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NLPTypeDeciderTest implements Constants{
    StanfordNLP stanfordNLP;
    NLPTypeDecider nlpTypeDecider;

    @Before
    public void setupNLPDecider() {
        stanfordNLP = new StanfordNLP();
        nlpTypeDecider = new NLPTypeDecider();
    }

    @Test
    public void testEmptySentence() {
        String emptyString = "e";
        TypedDependency[] typedDependencies = stanfordNLP.getRelevantTypedDependencies(RELEVANT_TYPED_DEPENDENCIES, emptyString);

        assertEquals("Empty String returns NLPType Nothing", NLPType.NOTHING, nlpTypeDecider.getNLPType(typedDependencies));
    }

    @Test
    public void testFullSentence() {
        String fullString = "Firefox isn't able to install its own updates on Ubuntu";
        TypedDependency[] typedDependencies = stanfordNLP.getRelevantTypedDependencies(RELEVANT_TYPED_DEPENDENCIES, fullString);

        assertEquals("Full String returns NLPType AUX_DOBJ_NSUBJ_XCOMP", NLPType.AUX_DOBJ_NSUBJ_XCOMP, nlpTypeDecider.getNLPType(typedDependencies));
    }

    @Test
    public void testHalfSentence() {
        String fullString = "He eats a cake";
        TypedDependency[] typedDependencies = stanfordNLP.getRelevantTypedDependencies(RELEVANT_TYPED_DEPENDENCIES, fullString);

        assertEquals("Half String returns NLPType DOBJ_NSUBJ ", NLPType.DOBJ_NSUBJ, nlpTypeDecider.getNLPType(typedDependencies));
    }
}
