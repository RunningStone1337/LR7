import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class URLDepthPair {
    private String path;
    private String host;
    private String URL;
    private int depth;


    public URLDepthPair(String URL, int depth) {
        this.URL = URL;
        this.depth = depth;
        try {
            java.net.URL myurl = new URL(URL);
            path = myurl.getPath();
            host = myurl.getHost();
            if(path.length() == 0 || path.charAt(path.length() - 1) != '/'){
                path += "/";
            }
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "Link - " + URL + " on depth " + depth;
    }

    public String GetURL() {
        return URL;
    }

    public int GetDepth() {
        return depth;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

}