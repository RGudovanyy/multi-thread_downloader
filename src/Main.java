import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Simple multi-treating program used to download files from remote web resources.
 * In this class URLs places in the Queue and for each element in this Queues starts a new thread, up to 5 times.*
 * When the program started you can use some keywords to manage it:
 * -end - stops current downloads and closes the program
 * -info - print on the console information about current downloads
 * -help - print on the console information about useful keywords
 */

public class Main {
    public static final Queue<String> filesOrder = new ConcurrentLinkedQueue<>();
    public static final List<String> filesInDownload = new ArrayList<>();
    public static int threadCount = 0;
    public static void main(String[] args) {
        String url;
        String path;

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String inputString;
            while (!"-end".equals((inputString = reader.readLine()))){
                if(inputString.startsWith("-info")){
                    System.out.println("Количество работающих потоков: " + Main.threadCount + " из 5");
                    System.out.println("Количество файлов в очереди: " + filesOrder.size());
                }
                if(inputString.startsWith("-files")){
                    System.out.println("Файлы, находящиеся в очереди на загрузку: \n");
                    for(String f : filesOrder){
                        System.out.println(f.split(" ")[0]);
                    }
                }

                if(inputString.startsWith("-help")){
                    System.out.println("Чтобы посмотреть текущую статистику по загрузкам введите \"-info\"");
                    System.out.println("Чтобы выйти из программы и прервать все загрузки введите \"-end\" ");
                    System.out.println("Чтобы посмотреть файлы находящиеся в очереди введите \"-files\" ");
                }
                if(inputString.startsWith("htt")){
                    url = inputString.split(" ")[0];
                    path = inputString.split(" ")[1];
                    filesOrder.add(url + " " + path);
                    System.out.println("Файл поставлен в очередь");
                    while (!filesOrder.isEmpty() && threadCount < 5)
                        startNewThread();
                    if(!(threadCount <5))
                        System.out.println("Свободных потоков пока нет, подождите несколько минут");
                }
            }

            reader.close();
            System.out.println("Не удалось закачать следущие файлы: \n");
            filesInDownload.forEach(System.out::println);
            System.out.println("Не начата загрузка следующих файлов: \n");
            for(String ss : filesOrder){
                System.out.println(ss.split(" ")[0]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startNewThread(){
        while (!filesOrder.isEmpty()){
            if(threadCount <5){
                String fileToDownload = filesOrder.poll();
                String url = fileToDownload.split(" ")[0];
                String path = fileToDownload.split(" ")[1];
                Downloader downloader = new Downloader(url,path);
                Thread thread = new Thread(downloader);
                thread.start();
                threadCount++;

            }


        }


    }


}
