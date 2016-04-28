package crawler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public interface Constants {

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
            new AppInfo("OpenDocument Reader", "tomtasche.reader", "at"),
            new AppInfo("WiFi Automatic", "j4velin.wifiAutoOff", "de")

    };

    String ARDOC_SEARCH_METHOD = "NLP+TA+SA";
}
