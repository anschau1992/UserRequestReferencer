package codeLinking;

import com.google.common.io.Files;
import crawler.Constants;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;


public class SourceCodeCrawler implements Constants {
    public SourceCodeCrawler() {

    }

    public void crawl(List<String> appNames, Map<String, String> sourceCodeLinks) throws IOException {
        for (String appName: appNames) {
            String sourceCodeLink = sourceCodeLinks.get(appName);
            String zipPath = downloadZIPFile(appName, sourceCodeLink);

            //unzip folder
            UnzipUtility unzipUtility = new UnzipUtility();
            String fileName = appName.replaceAll(" ", "_").toLowerCase();
            //create sourceCode-folder if not exists
            String sourceCodefolder = "sourceCode";
            if (!new File(sourceCodefolder).exists()) {
                System.out.println("Creating folder '/sourceCode'");
                new File(sourceCodefolder).mkdirs();
            }
            unzipUtility.unzip(zipPath,sourceCodefolder + "/" + fileName);
        }
    }

    /**
     * Downloads the sourceCode into /zip/appName, if not yet downloaded
     * @param sourceCodeLink
     * @throws IOException
     */
    private String downloadZIPFile(String appName, String sourceCodeLink) throws IOException {
        String zipPath = "zip";
        String filePath = null;
        if(sourceCodeLink.startsWith(GITHUBLINK_START)) {
            URL url = new URL(sourceCodeLink + GITHUB_ZIP);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream in = connection.getInputStream();

            String fileName = appName.replaceAll(" ", "_").toLowerCase() + ".zip";

            //create folder if not exists
            if (!new File(zipPath).exists()) {
                System.out.println("Creating folder '/zip'");
                new File(zipPath).mkdirs();
            }

            filePath = zipPath +"/"+ fileName;
            File zipFile = new File(filePath);
            if(!zipFile.exists()) {
                System.out.println("Download source file of app: \"" + appName+"\"");

                //Files.createParentDirs(zipFile);
                zipFile.createNewFile();
                FileOutputStream out = new FileOutputStream(zipFile);
                copy(in, out, 1024);
                out.close();
            } else {
                System.out.println("Source file of app: \"" + appName+"\" is already downloaded");
            }
        } else {
            //TODO handle no Github-SourceCodeLinks
            System.out.println("This source code is not referencing to a GitHubFile");
        }
        return filePath;
    }

    public static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        byte[] buf = new byte[bufferSize];
        int n = input.read(buf);
        while (n >= 0) {
            output.write(buf, 0, n);
            n = input.read(buf);
        }
        output.flush();
    }
}
