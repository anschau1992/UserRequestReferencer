package codeLinking;

import helper.Constants;
import helper.Review;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.Before;
import org.junit.Test;
import preclassification.PreClassification;
import subclassification.subclasses.SUBCLASS_RESSOURCES;
import subclassification.subclasses.SUBCLASS_USAGE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by Andy on 05.08.16.
 */
public class SourceCodeLinkerTest implements Constants {

    SourceCodeLinker scl;
    String dbName = DBNAME_TEST;

    List<Review> reviews = new ArrayList<Review>();
    @Before
    public void setUpReviews() throws IOException, ParseException {

        //real reviews example +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        Review review1 = new Review("57291d8c507c69054da8ee23", "Crashes I use this so as my default wallpaper app," +
                " but lately it has been crashing like crazy, and I have been reporting it every time it crashes," +
                " please fix it, I hate my static wallpaper now."
                , PreClassification.RESSOURCES, 3);
        review1.setApp("Muzei Live Wallpaper");
        review1.setSubClassification(SUBCLASS_RESSOURCES.PERFORMANCE.toString());
        review1.setWekaCorrectClassified(true);

        Review review2 = new Review("57291dac507c69054da8ee4a", "High Memory Usage Great app, but uses 85-120mb of ram," +
                " even with blur and dim off. Needs to always be in memory, so really not ideal.",
                PreClassification.RESSOURCES, 2);
        review2.setApp("Muzei Live Wallpaper");
        review2.setSubClassification(SUBCLASS_RESSOURCES.MEMORY.toString());
        review2.setWekaCorrectClassified(true);

        Review review3 = new Review("57291dd2507c69054da8ee7b", "Uses waaaayy too much ram than I thought" +
                " I thought this was just a light live wallpaper but someone pointed out it was using 70mb idle wtf.",
                PreClassification.RESSOURCES, 1);
        review3.setApp("Muzei Live Wallpaper");
        review3.setSubClassification(SUBCLASS_RESSOURCES.MEMORY.toString());
        review3.setWekaCorrectClassified(true);

        Review review4 = new Review("57291016507c69054da8e0a6","Display is not updated" +
                " on home screen Recently I have noticed that the home screen widget is not" +
                " updating with the proper battery % after it has been charged. I would have " +
                "50% batt, but it would say 15% on the widget. I need to open the app itself" +
                " and then close it, in order for the widget to be updated",
                PreClassification.USAGE, 3);
        review4.setApp("BatteryBot Battery Indicator");
        review4.setSubClassification(SUBCLASS_USAGE.UI.toString());
        review4.setWekaCorrectClassified(true);

        Review review5 = new Review("57291d53507c69054da8ee07", "I am glad the developer" +
                " listened to the feedback of myself and others. No more forced purple widget." +
                " We now have an option for a dark widget theme that can match most homescreen themes.",
                PreClassification.USAGE, 3);
        review5.setApp("BatteryBot Battery Indicator");
        review5.setSubClassification(SUBCLASS_USAGE.UI.toString());
        review5.setWekaCorrectClassified(true);

        //fake reviews example +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        Review review6 = new Review("57291d53507c69054da8ee07", "background, settings,toolbar, framelayout , layout",
                PreClassification.USAGE, 3);
        review6.setApp("Muzei Live Wallpaper");
        review6.setSubClassification(SUBCLASS_USAGE.UI.toString());
        review6.setWekaCorrectClassified(true);


        reviews.add(review1);
        reviews.add(review2);
        reviews.add(review3);
        reviews.add(review4);
        reviews.add(review5);
        reviews.add(review6);


        scl = new SourceCodeLinker(reviews, dbName);
    }

    @Test
    public void testReviewHashMapping() {
        Map<String, List<Review>>  hashedReviews = scl.getLinkingReviews();
//        assertEquals("Hashmap size is two, cause there are two different apps", 2, hashedReviews.size());
//        assertEquals("List 'Muzei Live Wallpaper' contains 4 reviews", 4, hashedReviews.get("Muzei Live Wallpaper").size());
//        assertEquals("List 'A Comic Viewer' contains 2 reviews", 2, hashedReviews.get("A Comic Viewer").size());
    }

//    @Test
//    public void testAppNames() {
//        Map<String, List<Review>>  hashedReviews = scl.getLinkingReviews();
//        List<String> appNames = scl.getAppNames();
//        assertEquals("List-size is two, cause there are two different apps", 2, appNames.size());
//        assertEquals("First app is 'Muzei Live Wallpaper' ", "Muzei Live Wallpaper", appNames.get(0));
//        assertEquals("First app is 'A Comic Viewer' ", "A Comic Viewer", appNames.get(1));
//    }

}
