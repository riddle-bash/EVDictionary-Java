package com.example.dictionary.processData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Translator {

//    public static void main(String[] args) throws IOException {
//        System.out.printf(translateOnline("how are you?"));
//    }


  public static String translateOnline(String inputLang, String outputLang, String text) {
    String result = "";
    try {
      result = translate(inputLang, outputLang, text);
    } catch (Exception e) {
      System.out.printf("error File");
    }
    return result;
  }

  /**
   * Call api form google translate
   * @param langFrom is language of word need to be translated
   * @param langTo   is  language of word after trans
   * @param text     is input
   * @return text after translate
   * @throws IOException is exception input
   */
  private static String translate(String langFrom, String langTo, String text) throws IOException {
    String urlStr = "https://script.google.com/macros/s/AKfycbzHQ4BIilB2CHuTx0biV0wXgQ1hf4L-20jRCS4PEvBgNU9v1kyxI7HtklNfJnNT6F7C/exec" +
        "?q=" + URLEncoder.encode(text, "UTF-8") +
        "&target=" + langTo +
        "&source=" + langFrom;
    URL url = new URL(urlStr);
    StringBuilder response = new StringBuilder();
    HttpURLConnection connect = (HttpURLConnection) url.openConnection();
    connect.setRequestProperty("User-Agent", "Mozilla/5.0");
    BufferedReader in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
    String inputLine;
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();
    return response.toString();
  }

}
