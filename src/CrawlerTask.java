import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class CrawlerTask implements Runnable{
    public URLDepthPair pair;
    public Queue queue;

    CrawlerTask(Queue queue) {
        this.queue = queue;
    }
    @Override
    public void run() {
        this.pair = queue.Get();
        LinkedList<URLDepthPair> links = new LinkedList<>();
        try {
            links = Crawler.FindLinks(pair.GetDepth());
        } catch (IOException e) {
            //e.printStackTrace();
        }
        for (var link : links) {
            URLDepthPair newPair = new URLDepthPair(link.GetURL(), pair.GetDepth() + 1);
            if (!queue.GetChecked().contains(link)) {
                queue.Add(newPair);
            }
        }
    }
}
