import java.io.*;
import java.util.*;
import java.net.*;

public class Crawler {

    public static final String HTTP_0 = "a href=\"http://";
    //public static final String HTTP_1 = "href=\"http://";
    public static final String HTTPS_0 = "a href=\"https://";
    //public static final String HTTPS_1 = "href=\"https://";
    public static final String PREF_0 = "a href=";
    //public static final String PREF_1 = "href=";
    public static final Queue queue = new Queue(100);


    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter link: ");
        String URL = in.nextLine();
        System.out.print("Enter depth: ");
        int depth = in.nextInt();
        in.close();
        queue.Add(new URLDepthPair(URL, 0));
        FindLinks(depth);
        PrintResults();
    }
    public static void PrintResults() {
        for (URLDepthPair checked : queue.GetChecked())
        {
            System.out.println(checked.toString());
        }
    }

    public static void FindLinks(int depth) throws IOException {
        while(!queue.IsEmpty()) {
            URLDepthPair temp = queue.Get();//получаем текущую ссылку в очереди обработки
            URLConnection socket = new URL(temp.GetURL()).openConnection();//открываем новое соединение по полученной ссылке
            socket.setConnectTimeout(7000);//и определяем время принудительного прерывания
            InputStream stream = socket.getInputStream();//получаем поток ввода
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));//считываем его в буфер
            String str;//для записи получаемой строки из потока и дальнейшей обработки её
            if (reader != null) {//если в буфере не пусто
                while ((str = reader.readLine()) != null) {//пока очередная строка буфера не окажется пустой
                    if (temp.GetDepth() < depth) {//если глубина текущей пары ЮРЛ-глубина не больше максимальной
                        while(str.length() > 0) {//пока длина строки больше 0
                            String newURL;//строка для возможной новой ссылки из потока
                            if (str.contains(HTTP_0)) {//если в строке есть http
                                newURL = str.substring(str.indexOf(HTTP_0) + PREF_0.length() + 1);
                                newURL = newURL.substring(0, newURL.indexOf("\""));
                            }
                            else if (str.contains(HTTPS_0)) {//если в строке есть https
                                newURL = str.substring(str.indexOf(HTTPS_0) + PREF_0.length() + 1);
                                newURL = newURL.substring(0, newURL.indexOf("\""));
                            }
                           /* else if (str.contains(HTTP_1)) {//если в строке есть http
                                newURL = str.substring(str.indexOf(HTTP_1) + PREF_1.length() + 1);
                                newURL = newURL.substring(0, newURL.indexOf("\""));
                            }
                            else if (str.contains(HTTPS_1)) {//если в строке есть https
                                newURL = str.substring(str.indexOf(HTTPS_1) + PREF_1.length() + 1);
                                newURL = newURL.substring(0, newURL.indexOf("\""));
                            }*/
                            else break;
                            str = str.substring(str.indexOf(newURL) + newURL.length() + 1);//обновлем строку, вырезая всё до начала ссылки в ней
                            URLDepthPair new_pair = new URLDepthPair(newURL, temp.GetDepth() + 1);//добавляем новую пару ссылка-глуюина в коллекцию
                            if (!queue.GetChecked().contains(new_pair)) {//если найденного адреса ещё нет среди проверенных, добавляем его в очередь проверки
                                queue.Add(new_pair);
                            }
                        }
                    }
                    else break;
                }
            }
            reader.close();
            stream.close();
            socket.getInputStream().close();
            //если в текущей паре глубина меньше максимально заданной и в проверенных парах её нет, то добавляем её в проверенные
            if (temp.GetDepth() < depth && !queue.GetChecked().contains(temp)) queue.AddChecked(temp);
        }
    }
}