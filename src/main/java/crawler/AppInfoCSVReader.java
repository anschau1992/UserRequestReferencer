package crawler;

import helper.Constants;

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
 *  appName;category;playstoreLink;sourceCodeLink
 */
public class AppInfoCSVReader implements Constants {
    BufferedReader br;
    String line;
    String csvSplitBy = ";";

    public AppInfoCSVReader() {
        line = "";
    }

    /**
     * Transform each line of a given csv into a AppInfos object
     * @param csvFile path to file
     * @return List of AppInfo's object
     */
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
