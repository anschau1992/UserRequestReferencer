package crawler;

import crawler.AppInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public interface Constants {
    int MONGODB_PORT = 27017;
    int REVIEWSPERAPPLIMIT = 500;

    String DBNAME_TEST = "testreviews";
    String REVIEW_COLLLECTION_TEST = "reviews";
    String APP_INFOS_COLLLECTION_TEST = "appInfos";

    //TODO: GIVE PROPER NAME FOR REAL REVIEWS
    String DBNAME = "testreviews";
    String REVIEW_COLLLECTION = "reviews";
    String APP_INFOS_COLLLECTION = "appInfos";

    String TRAININGSET_COLLECTION = "trainingset";

    // Preclassification
    String PRECLASSIFICATION_EXPORT_PATH = "./docs/preclassification.csv";
    String CSV_SPLIT =";";

    //Stanford NLP
    String ARDOC_SEARCH_METHOD = "NLP+TA+SA";
    String [] RELEVANT_TYPED_DEPENDENCIES = {"nsubj", "dobj", "aux", "xcomp"};

    //VectorSpaceModel
    String TRAININGSET_CSV_PATH = "./docs/trainingset.csv";
    String REVIEW_TEXT = "review_text";
    String INDEXER_DIRECTORY = "./docs/vsm/index";
    String INDEXER_DIRECTORY_TEST = "./docs/vsm/index_test";




    //TODO: remove when all copied
    AppInfo[] APP_INFOS =
            {
            //Testing apps
//            new AppInfo("Ketch App", "ketchapp.stack", "com"),
//            new AppInfo("Whats App", "whatsapp", "com"),
//            new AppInfo("Snapchat", "snapchat.android", "com"),

            //Testing apps , few reviews
//                new AppInfo("Blue floppy bird", "appanios.bluefloppybird", "com"),
//                new AppInfo("Monkey Banana Cake", "appanios.monkeybananacake", "com"),
//                new AppInfo("Cooking Burger Chef Games 2", "EnJoyDev.Burgerrestaurant", "com"),
//                new AppInfo("TopChrétien ", "subsplash.thechurchapp.topmissionfrance", "com"),

            //OpenSource Apps
//            new AppInfo("Adblock Plus", "adblockplus.adblockplussbrowser", "org"),
//            new AppInfo("A Comic Viewer", "androidcomics.acv", "net"),
//            new AppInfo("Duck Duck GO", "duckduckgo.mobile.android", "com"),
//            new AppInfo("Xabber", "xabber.android", "com"),
//            new AppInfo("Marine Compass", "pierrox.mcompass", "net"),
//            new AppInfo("Bankdroid", "liato.bankdroid", "com"),
//            new AppInfo("Abstract Art", "georgewhiteside.android.abstractart", "net"),
//            new AppInfo("MultiPicture Live Wallpaper", "tamanegi.wallpaper.multipicture", "org"),
//            new AppInfo("Pixel Dungeon", "watabou.pixeldungeon", "com"),
//            new AppInfo("BatteryBot Battery Indicator", "darshancomputing.BatteryIndicator", "com"),
//            new AppInfo("Autostarts", "elsdoerfer.android.autostarts", "com"),
//            new AppInfo("Calculator", "android2.calculator3", "com"),
//            new AppInfo("Bubble level", "androgames.level", "net"),
//            new AppInfo("CatLog", "nolanlawson.logcat", "com"),
//            new AppInfo("Network Log", "googlecode.networklog", "com"),
//            new AppInfo("OctoDroid", "gh4a", "com"),
//
//
//            //OpenSource Apps Google Category
//            new AppInfo("QKSMS - Open Source SMS & MMS", "moez.QKSMS", "com"),
//            new AppInfo("Signal Private Messenger", "thoughtcrime.securesms", "org"),
//            new AppInfo("AnkiDroid Flashcards", "ichi2.anki", "com"),
//            new AppInfo("Financius - Expense Manager", "code44.finance", "com"),
//            new AppInfo("2048", "uberspot.a2048", "com"),
//            new AppInfo("Simon Tatham's Puzzles", "boyle.chris.sgtpuzzles", "name"),
//            new AppInfo("Last.fm", "last.android", "fm"),
//            new AppInfo("SeriesGuide", "battlelancer.seriesguide", "com"),
//            new AppInfo("Muzei Live Wallpaper", "nurik.roman.muzei", "net"),
//            new AppInfo("Wally", "musenkishi.wally", "com"),
//            new AppInfo("Turbo Editor ( Text Editor )", "maskyn.fileeditor", "com"),
//            new AppInfo("Tweet Lanes", "tweetlanes.android", "com"),
//            new AppInfo("Twidere for Twitter", "mariotaku.twidere", "org"),
//            new AppInfo("AcDisplay", "achep.acdisplay", "com"),
//            new AppInfo("Amaze File Manager", "amaze.filemanager", "com"),
//            new AppInfo("Clip Stack ✓ Clipboard Manager", "catchingnow.tinyclipboardmanager", "com"),
//            new AppInfo("ConnectBot", "connectbot", "org"),
//            new AppInfo("DashClock Widget", "nurik.roman.dashclock", "net"),
//            new AppInfo("Device Control [root]", "namelessrom.devicecontrol", "org"),
//            new AppInfo("OS Monitor", "eolwral.osmonitor", "com"),
//            new AppInfo("Terminal Emulator for Android", "androidterm", "jackpal"),
//            new AppInfo("c:geo", "geocaching", "cgeo"),


    };


}
