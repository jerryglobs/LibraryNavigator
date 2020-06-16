package com.example.librarynavigator;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private String htmlPageUrl = "http://library.ajou.ac.kr/search/Search.Result.ax?" +
            "sid=1&q=ALL:%s&qf=%s&qt=%EC%A0%84%EC%B2%B4:%s&mf=true&item=&facet=&vid=0&tabID=&gr=&rl=&f=CLASSID:(1%20OR%202)";
    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
        ArrayList<String> title_array_list = new ArrayList<>();
        ArrayList<String> booksign_array_list = new ArrayList<>();
        ArrayList<Bitmap> imgurl_array_list = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(htmlPageUrl).get();
                Elements titlelinks = doc.select("div.textArea2Inner div.body a.title");
                Elements imglinks = doc.select("div.textArea2Inner div.contentImage img");
                Elements booksigns = doc.select("div.textArea2Inner div.body p.tag");

                for (int i = 0; i < titlelinks.size(); i += 2) {
                    String title = titlelinks.get(i).text();
                    title_array_list.add(title);
                }

                for (int i = 0; i < imglinks.size(); i += 3) {
                    String imgurl = imglinks.get(i).attr("abs:src");
                    imgurl_array_list.add(BitmapFactory.decodeStream(new URL(imgurl).openConnection().getInputStream()));
                }

                for (int i = 0; i < booksigns.size(); i += 1) {
                    String booksign = imglinks.get(i).text();
                    System.out.println(booksign);
                    booksign_array_list.add(booksign);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // book을 만들어서 넣는다.
            try {
                for (int i = 0; i < title_array_list.size(); i++) {
                    Book b = new Book();

                    b.setBookName(title_array_list.get(i));
                    b.setImgurl(imgurl_array_list.get(i));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void mingitest() {
        htmlPageUrl = htmlPageUrl.replaceAll("%s", "러닝");
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }
}