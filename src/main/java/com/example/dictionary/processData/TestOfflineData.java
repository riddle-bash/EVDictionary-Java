package com.example.dictionary.processData;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class TestOfflineData {
    public Word[] listWord = new Word[100000];
    int index_listWord = 0;

    public void GetDataFromTxt() {
        try {
            File file = new File("C:\\Users\\Admin\\Desktop\\Test_dict\\data\\databaseDict.txt");
            //Scanner scanner = new Scanner(file);

            FileInputStream inputStream = new FileInputStream(file);
            Scanner scanner = new Scanner(inputStream, "UTF-8");

            int index_type = 0;
            int index_meaning = 0;

            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                if (str.equals("")) {
                    continue;
                }
                if (index_listWord >= 100000) {
                    break;
                }
                //split line to two part2 : signal and document
                //use switch case to get 3 case:
                //case "@" : get face_word
                // split line to max parts
                //  merge arr[1]->size-2 -> face_word
                //  arr[size-1]-> pronunciation
                //case "*" : get type , maximum has 3 types for each word
                //case "-" : get meaning, maximum has 3*3 meanings
                char signal = str.charAt(0);
                switch (signal) {
                    case '@' :
                        try {
                            Word word = new Word();
                            str = str.substring(1, str.length() - 1);
                            String[] arr = str.split("/", -2);

                            word.face_word = arr[0];
                            word.pronunciation = arr[1];
                            listWord[index_listWord] = word;
                            index_listWord ++;
                            //reset index for type and meaning
                            index_type = 0;
                            index_meaning = 0;
                        } catch (Exception e) {
                            index_type = 4;
                        }
                        break;
                    case '*' :
                        try {
                            str = str.substring(3, str.length());
                            if (index_type < 3) {
                                listWord[index_listWord - 1].types[index_type] = str;
                                index_type++;
                                index_meaning = 0;
                            } else {
                                index_meaning = 4;
                            }
                        } catch (Exception e) {
                            index_meaning = 4;
                        }
                        break;

                    case '-' :
                        try {
                            str = str.substring(2, str.length());
                            if (index_meaning < 3) {
                                listWord[index_listWord - 1].meanings[index_type - 1][index_meaning] = str;
                                index_meaning++;
                            }
                        } catch (Exception e) {
                        }
                        break;
                }
            }
        } catch (Exception e) {
            System.out.print("" + e);
        }
    }

    public void printOut() {
        System.out.printf("" + index_listWord + '\n');
        for (int i = 0; i < index_listWord; i++) {
            Word temp = listWord[i];
            System.out.printf(temp.face_word + "\n" + temp.pronunciation + "\n");
            for (int j = 0; j < 3; j++) {
                if (temp.types[j] != null) {
                    System.out.printf(temp.types[j] + "\n");
                    for (int k = 0; k < 3; k++) {
                        if (temp.meanings[j][k] != null) {
                            System.out.printf(temp.meanings[j][k] + "\n");
                        } else {
                            break;
                        }
                    }
                }
            }
            System.out.printf("\n");
        }

    }
    public static void main(String[] args) {
        TestOfflineData t = new TestOfflineData();
        t.GetDataFromTxt();

        //t.printOut();

        String url = "jdbc:mysql://localhost:3307/dictionary";
        String username = "root";
        String password = "aptx4869";

        try {
            Connection connection = DriverManager.getConnection(url, username, password);

            String sql = "INSERT INTO data (word, pronunciation, type, meaning) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            int range = t.index_listWord;
            String x, y, z, m;
            for (int i = 0; i < range; i++) {
                Word temp = t.listWord[i];
                //System.out.printf(temp.face_word + "\n" + temp.pronunciation + "\n");

                x = temp.face_word;
                y = temp.pronunciation;
                for (int j = 0; j < 3; j++) {
                    if (temp.types[j] != null) {
                        z = temp.types[j];
                        for (int k = 0; k < 3; k++) {
                            if (temp.meanings[j][k] != null) {
                                m = temp.meanings[j][k];

                                statement.setString(1, x);
                                statement.setString(2, y);
                                statement.setString(3, z);
                                statement.setString(4, m);
                                statement.executeUpdate();
                            } else {
                                break;
                            }
                        }
                    }
                }
            }

            statement.close();
            connection.close();

        } catch (Exception e) {
            System.out.println("Error to connect!");
            e.printStackTrace();
        }
    }
}
