package crawler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Takes a csv-file with AppInfos and transforms them into AppInfo's Objects
 *
 * csv must be in format:
 *  appName;playstoreLink;sourceCodeLink
 */
public class CSVReader implements Constants {
    BufferedReader br;
    String line;
    String csvSplitBy = ";";

    public CSVReader() {
        line = "";
    }

    public List<AppInfo> transformToAppInfos (String csvFile) {
        List <AppInfo> appInfos = new ArrayList<AppInfo>();
        try{
            br = new BufferedReader(new FileReader(csvFile));
            line = br.readLine();
            int lineCount = 0;
            while (line != null) {
                lineCount++;
                try {
                    //use separator
                    String [] appElement = line.split(CSV_SPLIT);
                    AppInfo appInfo = createAppInfo(appElement);
                    appInfos.add(appInfo);
                } catch (Exception e) {
                    System.err.println("CSVReader: could not read in line #"
                            + lineCount + "\t"+ e);
                }
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appInfos;
    }

    private AppInfo createAppInfo(String[] appElement) {
        String appName = appElement[0];
        String category = appElement[1];
        String playStoreLink = appElement[2];
        String sourceCodeLink = appElement[3];

        return new AppInfo(appName, category, playStoreLink, sourceCodeLink);
    }
}
