package utils;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {


    public static void deleteFile(String filename){
        try {
            Files.deleteIfExists(Paths.get(filename));
        }catch(IOException ioE){
            ioE.printStackTrace();
        }
    }

    public static void writeMultipleLinesToFile(List<String> lines, String filename){
        try {
            FileOutputStream fos = new FileOutputStream(filename, true);

            for(String line: lines) {
                IOUtils.write(line, fos, "UTF-8");
            }
            fos.flush();
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public static String getStringFromFile(File file){
        String fileString = null;
        try{
            FileInputStream fis = new FileInputStream(file);
            fileString = IOUtils.toString(fis, "UTF-8");
        }catch(IOException ioE){
            ioE.printStackTrace();
        }


        return fileString;
    }


    public static List<String> getFileAsStringList(String filepath){
        List<String> lines = new ArrayList<>();


        try{
            BufferedReader br = new BufferedReader(new FileReader(filepath));
            String line;
            while((line = br.readLine()) != null){
                lines.add(line);
            }

        }catch(IOException ioE){
            ioE.printStackTrace();
        }


        return lines;
    }

}
