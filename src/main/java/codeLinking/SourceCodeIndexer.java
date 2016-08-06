package codeLinking;

import crawler.Constants;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Creates a lucene index of the source-code of the given app
 */
public class SourceCodeIndexer implements Constants {
    private Directory directory;
    public SourceCodeIndexer() {

    }

    public void indexApp(String appName) throws IOException {
        //create folder if not exists
        if (!new File(INDEX_FOLDER_PATH).exists()) {
            System.out.println("Creating folder '/"+INDEX_FOLDER_PATH + "'");
            new File(ZIP_FOLDER_PATH).mkdirs();
        }
        //create directory
        String fileName = appName.replaceAll(" ", "_").toLowerCase();
        Path path = Paths.get(INDEX_FOLDER_PATH + "/" + fileName);
        directory = new SimpleFSDirectory(path);

        IndexWriter writer = getWriter();

    }

    private IndexWriter getWriter() {
        return new
    }

}
