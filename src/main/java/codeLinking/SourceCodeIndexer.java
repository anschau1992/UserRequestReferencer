package codeLinking;

import helper.Constants;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Creates a lucene index of the source-code of the given app
 */
public class SourceCodeIndexer implements Constants {

    public SourceCodeIndexer() throws IOException {

    }


    public void indexFile(List<File> files, String fileName,  boolean indexJava) throws IOException {
        //create folder if not exists
        if (!new File(INDEX_FOLDER_PATH + "/" + fileName).exists()) {
            System.out.println("Creating folder '/" + INDEX_FOLDER_PATH + "'/" + fileName);
            new File(ZIP_FOLDER_PATH + "/" + fileName).mkdirs();
        }
        System.out.println("Indexing " + fileName);
        Path path = Paths.get(INDEX_FOLDER_PATH + "/" + fileName);
        Directory directory = new SimpleFSDirectory(path);

        IndexWriter writer;
        if (indexJava) {
            writer = getJavaWriter(directory);
        } else {
            writer = getXMLWriter(directory);
        }

        IndexCategorisator categorisator = new IndexCategorisator();
        for (File file : files) {
            String content = new String(Files.readAllBytes(Paths.get(file.getPath())));
            String id = file.getPath().substring(SOURCE_CODE_PATH.length());

            //split camelCase if java-files
            if(indexJava) {
                content = splitCamelCase(content);
            }

            Document doc = new Document();
            doc.add(new Field("id", id,
                    Field.Store.YES, Field.Index.NOT_ANALYZED));
            doc.add(new Field("content", content,
                    Field.Store.YES, Field.Index.ANALYZED));

            //add indexCategory for narrowing the result at searching
            IndexCategory category = categorisator.categorise(id, content);
            Field categoryField = new Field("category", category.toString(),
                    Field.Store.YES, Field.Index.NOT_ANALYZED);
            categoryField.setBoost(CATEGORY_BOOST);
            doc.add(categoryField);

            writer.addDocument(doc);
        }

        writer.close();
    }

    public void cleanFolder(String fileName) throws IOException {
        Path path = Paths.get(INDEX_FOLDER_PATH + "/" + fileName);
        Directory directory = new SimpleFSDirectory(path);

        IndexWriter writer = getJavaWriter(directory);
        writer.deleteAll();
        writer.close();

        writer = getXMLWriter(directory);
        writer.deleteAll();
        writer.close();
    }

    private IndexWriter getJavaWriter(Directory directory) throws IOException {
        CharArraySet stopWords = getStopWords("./src/main/java/codeLinking/javaStopList.csv");
        IndexWriterConfig config = new IndexWriterConfig(new JavaCodeAnalyzer(stopWords));
        return new IndexWriter(directory, config);
    }

    private IndexWriter getXMLWriter(Directory directory) throws IOException {
        CharArraySet stopWords = getStopWords("./src/main/java/codeLinking/xmlStopList.csv");
        IndexWriterConfig config = new IndexWriterConfig(new XMLCodeAnalyzer());
        return new IndexWriter(directory, config);
    }

    /**
     * Loads all words given by a csv into a CharArraySet
     * @param path to the csv-file
     * @return stopWords
     */
    private static CharArraySet getStopWords(String path) {
        CharArraySet wordAsCharArray = new CharArraySet(1000, true);
        int lineCount = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));


            String line = br.readLine();

            while (line != null) {
                lineCount++;
                try {
                    //use separator
                    String[] stopWords = line.split(";");
                    for (String word : stopWords) {
                        char[] charArray = word.toCharArray();
                        wordAsCharArray.add(charArray);
                    }

                } catch (Exception e) {
                    System.err.println("CSVReader: could not read in line #"
                            + lineCount + "\t" + e);
                }
                line = br.readLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return wordAsCharArray;
    }

    static String splitCamelCase(String s) {
        return s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }

}
