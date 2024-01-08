package com.example.dictionary.processData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class LoadOnlineData {
    private static ArrayList<String> listWords = new ArrayList<String>();

    public static void setListWords(ArrayList<String> listWords) {
        LoadOnlineData.listWords = listWords;
    }

    public static ArrayList<String> getListWords() {
        return listWords;
    }

    /**
     * Method to get all words from database to build a list
     */
    public static void getListWordFromDB() {

            //call data from database
            String url = DBConfigs.url;
            String username = DBConfigs.username;
            String password = DBConfigs.password;

            try {
                //connection to database from local
                Connection connection = DriverManager.getConnection(url, username, password);

                //statement in mysql, order by asc
                String sql = "SELECT word FROM data ORDER BY CASE WHEN word REGEXP '^[A-Za-z]' THEN 1 ELSE 2 END, word;";
                Statement statement = connection.createStatement();

                //call data and store it in result
                ResultSet result = statement.executeQuery(sql);

                String wordPrev = "";
                String wordCurr;
                while (result.next()) {
                    wordCurr = result.getString("word");
                    if (!wordCurr.equals(wordPrev)) {
                        listWords.add(wordCurr);
                        wordPrev = wordCurr;
                    }
                }

                statement.close();
                connection.close();
            } catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }
    }
}
