import java.util.LinkedList;

public class Queue {

    private final int maxSize;
    private LinkedList<URLDepthPair> pairs;
    private LinkedList<URLDepthPair> checked;

    public Queue(int maxSize) {
        this.maxSize = maxSize;
        this.pairs = new LinkedList<>();
        this.checked = new LinkedList<>();
    }

    public void Add(URLDepthPair obj) {
        if (pairs.size() < maxSize)
        {
            pairs.addLast(obj);
        }
    }

    public URLDepthPair Get() {
        if (pairs.size() > 0) return pairs.removeFirst(); // получаем объект из начала списка
        else return null;
    }

    public boolean IsEmpty() {
        return this.pairs.isEmpty();//проверяем пуст ли текущий лист проверяемых пар
    }

    public void AddChecked(URLDepthPair obj) {
        checked.add(obj); //добавляем в список проверенных очредную пару
    }

    public LinkedList<URLDepthPair> GetChecked() {
        return this.checked;//получаем весь лист проверенных ссылок
    }
}