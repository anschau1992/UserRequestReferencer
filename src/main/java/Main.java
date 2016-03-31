import crawler.Crawler;

//Start of the program
public class Main {

    public  static void main(String[] args){
        Crawler crawler = new Crawler();

        crawler.saveAllReviewsOfAppsIntoDB();
    }
}
