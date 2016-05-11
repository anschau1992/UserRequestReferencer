package crawler;

public class AppInfo {
    private final String name;
    private final String id;
    private final String linkName;

    public AppInfo(String name, String linkName, String id) {
        this.name = name;
        this.linkName = linkName;
        this.id = id;
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getLinkName() {
        return linkName;
    }
}
