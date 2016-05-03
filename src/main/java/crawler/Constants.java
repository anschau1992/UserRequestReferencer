package crawler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public interface Constants {

    String DBNAME = "review";
    String COLLLECTIONNAME = "review";
    int REVIEWSPERAPPLIMIT = 500;

     AppInfo [] APP_INFOS =
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
            new AppInfo("Adblock Plus", "adblockplus.adblockplussbrowser", "org"),
            new AppInfo("A Comic Viewer", "androidcomics.acv", "net"),
            new AppInfo("Duck Duck GO", "duckduckgo.mobile.android", "com"),
            new AppInfo("Xabber", "xabber.android", "com"),
            new AppInfo("Marine Compass", "pierrox.mcompass", "net"),
            new AppInfo("Bankdroid", "liato.bankdroid", "com"),
            new AppInfo("Abstract Art", "georgewhiteside.android.abstractart", "net"),
            new AppInfo("MultiPicture Live Wallpaper", "tamanegi.wallpaper.multipicture", "org"),
            new AppInfo("Frozen Bubble", "jfedor.frozenbubble", "org"),
            new AppInfo("Pixel Dungeon", "watabou.pixeldungeon", "com"),
            new AppInfo("BatteryBot Battery Indicator", "darshancomputing.BatteryIndicator", "com"),
            new AppInfo("Autostarts", "elsdoerfer.android.autostarts", "com"),
            new AppInfo("Calculator", "android2.calculator3", "com"),
            new AppInfo("Bubble level", "androgames.level", "net"),
            new AppInfo("CatLog", "nolanlawson.logcat", "com"),
            new AppInfo("Network Log", "googlecode.networklog", "com"),
            new AppInfo("OctoDroid", "gh4a", "com"),

    };

    String ARDOC_SEARCH_METHOD = "NLP+TA+SA";
}
