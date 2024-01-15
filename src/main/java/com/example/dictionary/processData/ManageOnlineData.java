package com.example.dictionary.processData;

import java.sql.*;
import java.util.ArrayList;

public class ManageOnlineData {
    public static void addNewWord(ArrayList<String> arrayList) {
        String url = DBConfigs.url;
        String username = DBConfigs.username;
        String password = DBConfigs.password;

        try {
            Connection  connection = DriverManager.getConnection(url, username, password);

            StringBuilder stringBuilder = new StringBuilder();
            
            stringBuilder.append("insert into `data` (`word`, `type`, `pronunciation`, `meaning`) values (\"");
            stringBuilder.append(arrayList.get(0) + "\", \"" + arrayList.get(1) + "\", \"" + arrayList.get(2));
            stringBuilder.append("\", \"" + arrayList.get(3) + "\");");

            // TRACK
            System.out.println("Word added succesfully: " + stringBuilder);

            Statement statement = connection.createStatement();
            statement.executeUpdate(stringBuilder.toString());

            LoadOnlineData.setListWords(new ArrayList<>());
            LoadOnlineData.getListWordFromDB();

            statement.close();
            connection.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static void deleteWord(String str) {
        String url = DBConfigs.url;
        String username = DBConfigs.username;
        String password = DBConfigs.password;

        try {

            Connection connection = DriverManager.getConnection(url, username, password);

            String sql = "delete from data where word = \"" + str + "\"";

            // TRACK
            System.out.println("Word deleted successfully: " + sql);

            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);

            LoadOnlineData.setListWords(new ArrayList<>());
            LoadOnlineData.getListWordFromDB();

            statement.close();
            connection.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void updateWord(ArrayList<String> wordMeaningDelete, ArrayList<String> wordMeaningReplace) {
        String url = DBConfigs.url;
        String username = DBConfigs.username;
        String password = DBConfigs.password;


        try {

            Connection connection = DriverManager.getConnection(url, username, password);

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("delete from `data` where `word` = '" + wordMeaningDelete.get(0) + "'");
            stringBuilder.append(" and `type` = '" + wordMeaningDelete.get(1) + "' and `pronunciation` = '");
            stringBuilder.append(wordMeaningDelete.get(2) + "' and `meaning` = '" + wordMeaningDelete.get(3) + "'");
            // TRACK
            System.out.println("Word deleted successfully: " + stringBuilder);

            Statement statement = connection.createStatement();
            statement.executeUpdate(stringBuilder.toString());

            LoadOnlineData.setListWords(new ArrayList<>());
            LoadOnlineData.getListWordFromDB();

            statement.close();
            connection.close();

        } catch (Exception e) {
            System.out.println(e);
        }

        // FUNCTION: Add word
        addNewWord(wordMeaningReplace);
    }
}
