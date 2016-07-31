package subclassification.stanfordNLP;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.CoreMap;

import java.io.StringReader;
import java.util.*;

public class StanfordNLP {
    Annotation annotation;
    Properties props;
    StanfordCoreNLP pipeline;

    String parserModel;
    LexicalizedParser lp;

    public StanfordNLP() {
        props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
        pipeline = new StanfordCoreNLP(props);

        parserModel = "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
        lp = LexicalizedParser.loadModel(parserModel);
    }

    public int getSentimentScore(String text) {
        annotation = pipeline.process(text);

        int sentimentScore = -1;
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
            sentimentScore = RNNCoreAnnotations.getPredictedClass(tree);
            break;
        }
        return sentimentScore;
    }

    /**
     * Extracts the relevant Stanford Typed dependencies ordered by the word appearance in the sentence
     *
     * @param relevantTypedDependencyNames : names of all chosen dependencies
     * @param sentence                     : the sentence to exam
     * @return list of the relevant dependencies, ordered by word-order
     */
    public TypedDependency[] getRelevantTypedDependencies(String[] relevantTypedDependencyNames, String sentence) {
        HashMap<String, List<TypedDependency>> allDependencies = createTypedDependenciesHashMap(sentence);
        List<TypedDependency> relevantDependencyList = new ArrayList<TypedDependency>();

        for (String dependencyName : relevantTypedDependencyNames) {
            List<TypedDependency> dependencies = allDependencies.get(dependencyName);
            if (dependencies != null) {
                relevantDependencyList.add(dependencies.get(0));
            }
        }

        TypedDependency[] relevantArray = new TypedDependency[relevantDependencyList.size()];
        for (int i = 0; i < relevantArray.length; i++) {
            relevantArray[i] = relevantDependencyList.get(i);
        }
        //sort by word appearance
        Arrays.sort(relevantArray);

        return relevantArray;
    }

    private HashMap<String, List<TypedDependency>> createTypedDependenciesHashMap(String sentence) {
        TokenizerFactory<CoreLabel> tokenizerFactory =
                PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
        Tokenizer<CoreLabel> tok =
                tokenizerFactory.getTokenizer(new StringReader(sentence));
        List<CoreLabel> rawWords2 = tok.tokenize();
        Tree parse = lp.apply(rawWords2);

        TreebankLanguagePack tlp = lp.treebankLanguagePack(); // PennTreebankLanguagePack for English
        GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
        GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
        List<TypedDependency> tdl = gs.typedDependenciesCCprocessed();

        HashMap<String, List<TypedDependency>> typedDependencyHash = new HashMap<String, List<TypedDependency>>();
        for (TypedDependency typedDependency : tdl) {
            if (!typedDependencyHash.containsKey(typedDependency.reln())) {
                List<TypedDependency> list = new ArrayList<TypedDependency>();
                list.add(typedDependency);

                typedDependencyHash.put(typedDependency.reln().toString(), list);
            } else {
                typedDependencyHash.get(typedDependency.toString()).add(typedDependency);
            }
        }
        return typedDependencyHash;
    }
}
