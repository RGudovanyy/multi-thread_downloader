import java.io.*;
import java.net.URL;

/**
 * This class do all work to download the file.
 */

public class Downloader implements Runnable {
    String url;
    String path;
    InputStream in;
    FileOutputStream out;

    public Downloader(String url, String path) {
        this.url = url;
        this.path = path;
    }
    @Override
    public void run() {
        try{

            int fileIndex1 = url.lastIndexOf('/')+1;
            int fileIndex2 = url.lastIndexOf('.');

            String newFileName = url.substring(fileIndex1, fileIndex2);
            String extension = url.substring(fileIndex2);
            File outputFile = new File(path + "/" + newFileName + extension);
            if(outputFile.exists()) {
                outputFile.delete();
            }
            outputFile.createNewFile();
            System.out.println("Начинаем скачивание файла " + newFileName);
            Main.filesInDownload.add(newFileName);
            byte[] buffer = new byte[8192];
            in = new URL(url).openStream();
            out = new FileOutputStream(outputFile);

            int len;
            while ((len = in.read(buffer)) > 0){
                out.write(buffer, 0, len);
            }

            in.close();
            out.close();
            System.out.println("Скачивание файла " + newFileName + " завершено");
            Main.threadCount--;
            Main.filesInDownload.remove(newFileName);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
        }finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
