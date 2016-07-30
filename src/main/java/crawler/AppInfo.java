package crawler;

public class AppInfo {
    final String name;
    final String category;
    final String playStoreLinkID;
    final String sourceCodeLink;

    public AppInfo(String name, String category, String linkName, String sourceCodeLink) {
        this.name = name;
        this.category = category;
        this.playStoreLinkID = linkName;
        this.sourceCodeLink = sourceCodeLink;
    }

    public String getSourceCodeLink() {
        return sourceCodeLink;
    }
    public String getName() {
        return name;
    }
    public String getPlayStoreLinkID() {
        return playStoreLinkID;
    }
    public String getCategory() {
        return category;
    }
}
