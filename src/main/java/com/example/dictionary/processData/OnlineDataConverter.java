package com.example.dictionary.processData;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class OnlineDataConverter {
    public static void main (String[] args) {
        try {
            File input = new File("C:\\Users\\NamPC\\Desktop\\Workspace\\oop-dictionary-project-main3\\anhviet109K.txt");
            FileWriter output = new FileWriter("C:\\Users\\NamPC\\Desktop\\Workspace\\oop-dictionary-project-main3\\sql.txt");
            Scanner scanner = new Scanner(input);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isBlank()) {
                    continue;
                }
                switch (line.charAt(0)) {
                    case '@':
                        output.write("'),\n('");
                        for (int i = 1 ; i < line.length()-1 ; i ++) {
                            if (line.charAt(i) == ' ' && line.charAt(i+1) == '/') {
                                break;
                            }
                            if (line.charAt(i) == '\'') {
                                output.write("'");
                            }
                            output.write(line.charAt(i));
                        }
                        output.write("', '");
                        int count = 0;
                        for (int i = 1 ; i < line.length() ; i ++) {
                            if (line.charAt(i) == '/') {
                                count = 1;
                            }
                            if (count == 1) {
                                if (line.charAt(i) == '\'') {
                                    output.write("'");
                                }
                                output.write(line.charAt(i));
                            }
                        }
                        if (count == 0) {
                            output.write("/chưa có phát âm/");
                        }
                        output.write("', '");
                        output.write("danh từ', '");
                        break;
                    case '-':
                        for (int i = 2 ; i < line.length() ; i ++) {
                            if (line.charAt(i) == '\'') {
                                output.write("'");
                            }
                            output.write(line.charAt(i));
                        }
                        output.write(" ");
                        break;
                }
            }
            scanner.close();
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}