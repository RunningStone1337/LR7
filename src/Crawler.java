import com.sun.tools.javac.Main;

import java.io.*;
import java.util.*;
import java.net.*;

public class Crawler {

        public static final String HTTP_0 = "a href=\"http://";
        public static final String HTTPS_0 = "a href=\"https://";
        public static final String PREF_0 = "a href=";
        public static Queue queue;
        public static  int depth;
        public static  int streamsMax;
        public static  int streamsNow=0;
        public static Object obj = new Object();

        public static void main(String[] args) throws IOException, InterruptedException {
            Scanner in = new Scanner(System.in);
            System.out.print("Enter link: ");
            String URL = in.nextLine();
            System.out.print("Enter depth: ");
            depth = in.nextInt();
            System.out.print("Enter num of streams: ");
            streamsMax = in.nextInt();
            in.close();
            queue = new Queue(depth);
            queue.Add(new URLDepthPair(URL, 0));
            while (!queue.IsEmpty() || streamsNow != 0) {
                    synchronized (obj) {
                        while (streamsNow >= streamsMax) {
                            obj.wait(1000);
                        }
                        if (!queue.IsEmpty()) {//лист непроверенных url не пуст -> начинаем новый поток
                            StartThread(queue.Get());
                        } else obj.wait(1000);//иначе спим
            }
            }
        }

    public static void StartThread (URLDepthPair pair){
        streamsNow++;
        new Thread(new CrawlerTask(pair), "thread №" + streamsNow).start();
    }
    public static void PrintResults()
    {
            for (URLDepthPair checked : queue.GetChecked())
            {
                System.out.println(checked.toString());
            }
    }

    public static void FindLinks(URLDepthPair pair) throws IOException {
            try {
                    URLConnection socket = new URL(pair.GetURL()).openConnection();//открываем новое соединение по полученной ссылке
                    socket.setConnectTimeout(7000);//и определяем время принудительного прерывания
                    InputStream stream = socket.getInputStream();//получаем поток ввода
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));//считываем его в буфер
                    String str;//для записи получаемой строки из потока и дальнейшей обработки её
                    if (reader != null) {//если в буфере не пусто
                        while ((str = reader.readLine()) != null) {//пока очередная строка буфера не окажется пустой
                            if (pair.GetDepth() < depth) {//если глубина текущей пары ЮРЛ-глубина не больше максимальной
                                while (str.length() > 0) {//пока длина строки больше 0
                                    String newURL;//строка для возможной новой ссылки из потока
                                    if (str.contains(HTTP_0)) {//если в строке есть http
                                        newURL = str.substring(str.indexOf(HTTP_0) + PREF_0.length() + 1);
                                        newURL = newURL.substring(0, newURL.indexOf("\""));
                                    } else if (str.contains(HTTPS_0)) {//если в строке есть https
                                        newURL = str.substring(str.indexOf(HTTPS_0) + PREF_0.length() + 1);
                                        newURL = newURL.substring(0, newURL.indexOf("\""));
                                    }
                                    else break;
                                    str = str.substring(str.indexOf(newURL) + newURL.length() + 1);//обновлем строку, вырезая всё до начала ссылки в ней
                                    URLDepthPair new_pair = new URLDepthPair(newURL, pair.GetDepth() + 1);//добавляем новую пару ссылка-глуюина в коллекцию
                                    if (!queue.GetChecked().contains(new_pair) && !queue.GetPairs().contains(new_pair)) {//если найденного адреса ещё нет, добавляем его в очередь проверки
                                        queue.Add(new_pair);
                                    }
                                }
                            } else break;
                        }
                    }
                    reader.close();
                    stream.close();
                    socket.getInputStream().close();
                    //если в текущей паре глубина меньше максимально заданной и в проверенных парах её нет, то добавляем её в проверенные
                    if (pair.GetDepth() < depth && !queue.GetChecked().contains(pair)) queue.AddChecked(pair);
                    PrintResults();
            }
            catch (Exception ex)
            {
                System.out.println("RuntimeException");
            }
        }
}
