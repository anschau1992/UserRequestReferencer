package codeLinking;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Iterates recursively through the given folder and list all files for Indexing
 */
public class RecursiveFileReader {
    SourceCodeIndexer sci;

    List<File> javaFiles = new ArrayList<File>();
    List<File> xmlFiles = new ArrayList<File>();


    public RecursiveFileReader () throws IOException {
        sci = new SourceCodeIndexer();
    }

    public void readeFiles(File headFolder, String fileName) throws IOException {

        //clean old index before indexing new
        sci.cleanFolder(fileName);

        File [] files = headFolder.listFiles();
        filter(files);

        sci.indexFile(javaFiles, fileName, true);
        sci.indexFile(xmlFiles, fileName, false);
    }

    private void filter(File[] files) throws IOException {
        for (File file : files) {
            if(file.isDirectory()) {
                filter(file.listFiles());
            } else {
                checkForIndexing(file);
            }
        }
    }

    private void checkForIndexing(File file) throws IOException {
        String ext = FilenameUtils.getExtension(String.valueOf(file));

        //filtering by extension
        if(ext.equals("java")) {
            //TODO index file with corresponding analyzer
            javaFiles.add(file);

        } else if (ext.equals("xml")) {
            xmlFiles.add(file);
        }
    }
}
