package subclassification.stanfordNLP;

import helper.Constants;
import edu.stanford.nlp.trees.TypedDependency;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StanfordNLPTest implements Constants {
    StanfordNLP stanfordNLP;

    @Before
    public void setupStanfordNLP() {
        stanfordNLP = new StanfordNLP();
    }

    @Test
    public void testSentimentScore() {
        String verySadSentence = "I am feeling very sad and frustrated.";
        assertEquals("Very negative sentence return sentiment score 1",1, stanfordNLP.getSentimentScore(verySadSentence));

        String bestSentence = "The best movie ever existed";
        assertEquals("Positive sentence returning a 4",4, stanfordNLP.getSentimentScore(bestSentence));
    }

    @Test
    public void testStanfordNLPTypedDependencies() {
        String testString = "Firefox isn't able to install its own updates on Ubuntu";
        TypedDependency[] typedDependencies = stanfordNLP.getRelevantTypedDependencies(RELEVANT_TYPED_DEPENDENCIES, testString);

        assertEquals("StanfordNLP typed dependencies is returning correct nominal subject", "nsubj(install-6, Firefox-1)", typedDependencies[0].toString());
        assertEquals("StanfordNLP typed dependencies is returning correct auxiliary", "aux(install-6, to-5)", typedDependencies[1].toString());

    }

    @Test
    public void testgetRelevantTypedDependenciesInWordOrder() {
        String testString = "Firefox isn't able to install its own updates on Ubuntu";
        TypedDependency [] orderedRelevantTypedDependencies = stanfordNLP.getRelevantTypedDependencies(RELEVANT_TYPED_DEPENDENCIES,testString);

        if(orderedRelevantTypedDependencies.length >= 2) {
            for (int i = 1; i < orderedRelevantTypedDependencies.length; i++) {
                assertEquals("TypeDependency " + (i - 1) + " appears before TypeDependency " + i, orderedRelevantTypedDependencies[i-1].compareTo(orderedRelevantTypedDependencies[i]), -1);

            }
        }
    }

}
