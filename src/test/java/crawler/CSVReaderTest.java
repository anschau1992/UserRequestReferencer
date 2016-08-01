package crawler;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Testing the Class CSVReader
 */
public class CSVReaderTest {

    String csvPath = "./docs/testAppInfos.csv";
    AppInfoCSVReader csvReader;
    List<AppInfo> appInfos;

    @Before
    public void setupCSVReader() {
        csvReader = new AppInfoCSVReader();
        appInfos = csvReader.transformToAppInfos(csvPath);
    }

    @Test
    public void testCSVReaderLength() {
        assertEquals("appInfos-Length is 3", 3, appInfos.size());

    }

    @Test
    public void testFirstAppInfo() {
        assertEquals("First appInfo' name is Duck Duck GO", "Duck Duck GO", appInfos.get(0).getName());
        assertEquals("First appInfo' category is Internet", "Internet", appInfos.get(0).getCategory());
        assertEquals("First appInfo' Link is correct",
                "https://play.google.com/store/apps/details?id=com.duckduckgo.mobile.android&hl=en", appInfos.get(0).getPlayStoreLinkID());
        assertEquals("First appInfo' Link is correct",
                "https://github.com/johanhil/ddg-android", appInfos.get(0).getSourceCodeLink());
    }


}
