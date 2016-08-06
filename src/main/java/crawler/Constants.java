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
    String ARFF_EMPTY_SIGN = "?";

    //TODO: GIVE PROPER NAME FOR REAL REVIEWS
    String DBNAME = "review";
    String REVIEW_COLLLECTION = "review";
    String APP_INFOS_COLLLECTION = "appinfos";

    String TRAININGSET_COLLECTION = "trainingset";

    // Preclassification
    String PRECLASSIFICATION_EXPORT_PATH = "./docs/preclassification.csv";
    String PRECLASSIFICATION_EXPORT_PATH_TEST = "./docs/preclassification_test.csv";
    String PRECLASSIFICATION_IMPORT_PATH = "./docs/preclassification_with_classes.csv";
    String PRECLASSIFICATION_IMPORT_PATH_TEST = "./docs/preclassification_test.csv";
    String CSV_SPLIT =";";

    //Stanford NLP
    String ARDOC_SEARCH_METHOD = "NLP+TA+SA";
    String [] RELEVANT_TYPED_DEPENDENCIES = {"nsubj", "dobj", "aux", "xcomp"};

    //VectorSpaceModel
    String TRAININGSET_CSV_PATH = "./docs/trainingset.csv";
    String REVIEW_TEXT = "review_text";
    String INDEXER_DIRECTORY = "./docs/vsm/index";
    String INDEXER_DIRECTORY_TEST = "./docs/vsm/index_test";

    //SourceCodeLinker
    String GITHUBLINK_START = "https://github.com/";
    String GITHUB_ZIP = "/archive/master.zip";
    String ZIP_FOLDER_PATH = "zip";
    String INDEX_FOLDER_PATH = "lucene_index";
}
