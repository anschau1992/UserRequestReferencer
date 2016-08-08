package codeLinking;

public class IndexCategorisator {


    /**
     * Returns a category for Indexing based on heuristics of android-appstructures
     * @param id the path of the file
     * @param content whole text in the file
     * @return IndexCategory
     */
    public IndexCategory categorise(String id, String content) {
        if(id.contains("/res/") || id.contains("/ressources/")) {
            return IndexCategory.RESSOURCES;
        }else if (id.contains("AndroidManifest")) {
            return IndexCategory.ANDROID_MAINFEST;
        } else if (content.contains("service") || id.contains("Service")) {
            return IndexCategory.SERVICE;
        }  else if (content.contains("content") && content.contains("provider")
                || content.contains("ContentProvider")) {
            return IndexCategory.CONTENT_PROVIDER;
        }

        else {
            return IndexCategory.OTHER;
        }
    }
}
