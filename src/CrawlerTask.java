import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class CrawlerTask implements Runnable{
    private URLDepthPair urlDepthPair;
    public CrawlerTask (URLDepthPair urlDepthPair){
        this.urlDepthPair = urlDepthPair;
    }

    @Override
    public void run() {
        try {
            Crawler.FindLinks(this.urlDepthPair);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Crawler.streamsNow -=1;
    }
}
