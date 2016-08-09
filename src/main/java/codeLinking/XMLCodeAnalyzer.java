package codeLinking;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public final class XMLCodeAnalyzer extends StopwordAnalyzerBase {
    public static final CharArraySet ENGLISH_STOP_WORDS_SET;

    public XMLCodeAnalyzer() {
        this(ENGLISH_STOP_WORDS_SET);
    }

    public XMLCodeAnalyzer(CharArraySet stopWords) {
        super(stopWords);
    }

    public XMLCodeAnalyzer(Path stopwordsFile) throws IOException {
        this(loadStopwordSet(stopwordsFile));
    }

    public XMLCodeAnalyzer(Reader stopwords) throws IOException {
        this(loadStopwordSet(stopwords));
    }

    protected TokenStreamComponents createComponents(String fieldName) {
        StandardTokenizer source = new StandardTokenizer();

        StandardFilter tok2 = new StandardFilter((TokenStream)source);
        LowerCaseFilter tok3 = new LowerCaseFilter(tok2);
        StopFilter tok4 = new StopFilter(tok3, this.stopwords);
        PorterStemFilter tok5 = new PorterStemFilter(tok4);
        //TODO: abbreviations -> possibly with synonyms?

        return new TokenStreamComponents((Tokenizer)source, tok5);
    }

    static {
        List stopWords = Arrays.asList(new String[]{"a", "an", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "such", "that", "the", "their", "then", "there", "these", "they", "this", "to", "was", "will", "with"});
        CharArraySet stopSet = new CharArraySet(stopWords, false);
        ENGLISH_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet);
    }
}
