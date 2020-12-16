import java.util.ArrayList;
import java.util.LinkedList;

public class Queue {

    private final int maxSize;
    public int waitingThreads;
    public LinkedList <URLDepthPair> in_process;
    private LinkedList<URLDepthPair> pairs;
    private LinkedList<URLDepthPair> checked;


    public Queue(int maxSize) {
        this.maxSize = maxSize;
        this.pairs = new LinkedList<>();
        this.checked = new LinkedList<>();
        this.in_process = new LinkedList<>();
        this.waitingThreads=0;
    }

    public synchronized void Add(URLDepthPair obj) {
        if (obj.GetDepth() < maxSize && !checked.contains(obj.GetURL())) {
            pairs.addLast(obj);
            checked.add(obj);
            if (waitingThreads > 0) waitingThreads--;
            this.notify();
        }
    }

    public synchronized URLDepthPair Get() {
        if (pairs.size() == 0) {
            waitingThreads++;
            try {
                this.wait();
            }
            catch (InterruptedException e) {
                //System.err.println("MalformedURLException: " + e.getMessage());
                return null;
            }
        }
        URLDepthPair myDepthPair = pairs.removeFirst();
        in_process.add(myDepthPair);
        return myDepthPair;
    }

    public synchronized boolean IsEmpty() {
        return this.pairs.isEmpty();//проверяем пуст ли текущий лист проверяемых пар
    }

    public synchronized void AddChecked(URLDepthPair obj) {
        checked.add(obj); //добавляем в список проверенных очредную пару
    }

    public synchronized LinkedList<URLDepthPair> GetChecked() {
        return this.checked;//получаем весь лист проверенных ссылок
    }
    public synchronized int getWaitThreads() {
        return waitingThreads;
    }

}