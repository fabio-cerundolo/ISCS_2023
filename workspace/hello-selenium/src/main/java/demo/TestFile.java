package demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestFile {
    public static void main(String[] args) {
        String filepath="utenti.txt";
        try {
            Scanner reader = new Scanner(new File(filepath));
            while(reader.hasNext()) {
                String name = reader.next();
                System.out.println("Testo letto: " + name);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
}
