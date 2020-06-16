package com.example.librarynavigator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class BookListView extends Activity {
    private String searchText;      // 검색어
    private String htmlPageUrl = "http://library.ajou.ac.kr/search/Search.Result.ax?" +
            "sid=1&q=ALL:%s&qf=%s&qt=%EC%A0%84%EC%B2%B4:%s&mf=true&item=&facet=&vid=0&tabID=&gr=&rl=&f=CLASSID:(1%20OR%202)";
    private LinearLayout book_list;
    private ArrayList<Book> book_array_list = new ArrayList<>();
    private ArrayList<String> page_array_list = new ArrayList<>();
    private String page = "1";
    private String pageUrl;
    private int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list_view);

        Intent in = getIntent();
        searchText = in.getStringExtra("searchText");
        pageUrl = in.getStringExtra("nexturl");

        if(pageUrl!=null&&!pageUrl.isEmpty()){
            htmlPageUrl = pageUrl;
            htmlPageUrl = htmlPageUrl.replaceAll("%s", searchText);
            page = in.getStringExtra("page");
        }else {
            htmlPageUrl = htmlPageUrl.replaceAll("%s", searchText);
        }
        book_list = (LinearLayout)findViewById(R.id.book_list);

        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
    }

    private void makeBook_list() {
        for (Book b : book_array_list) {
            String bookSign = b.getBooksign();
            String bookName = b.getBookName();

            LinearLayout l = new LinearLayout(this);
            l.setOrientation(LinearLayout.HORIZONTAL);

            ImageView imageView = new ImageView(this);
            imageView.setImageBitmap(b.getImgurl());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
            ));

            TextView tv = new TextView(this);
            tv.setText(bookName);
            tv.setOnClickListener(new TextOnClickListener(b.getCid()));
            tv.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    0.5f));

            Button btn = new Button(this);
            if (bookSign.equals("대출중")) {
                btn.setText("대출중");
                btn.setEnabled(false);
            } else {
                btn.setText("선택");
            }

            btn.setOnClickListener(new BtnOnclickListener(bookSign));
            btn.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
            ));

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
            );

            l.setLayoutParams(layoutParams);

            l.addView(imageView);
            l.addView(tv);
            l.addView(btn);
            book_list.addView(l);

        }
        LinearLayout pageSelect = new LinearLayout(this);
        pageSelect.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                3f
        );
        pageSelect.setLayoutParams(layoutParams);
        flag = Integer.parseInt(page);
        int k = 0;
         for (int i = 1; i <= page_array_list.size()+1; i++) {

             if(i == flag) {
                 Button currentPage = new Button(this);
                 currentPage.setText(""+i);
                 currentPage.setTextSize(8);
                 currentPage.setEnabled(false);
                 currentPage.setLayoutParams(new LinearLayout.LayoutParams(
                         ViewGroup.LayoutParams.WRAP_CONTENT,
                         ViewGroup.LayoutParams.WRAP_CONTENT,
                         1f
                 ));
                 pageSelect.addView(currentPage);
                 continue;
             }
             Button pageNum = new Button(this);
             if ((i == page_array_list.size()+1) && i == 12) {
                 pageNum.setText(">>");
                 pageNum.setTextSize(8);
             } else if ((i == page_array_list.size()) && i == 11) {
                 pageNum.setText(">");
                 pageNum.setTextSize(8);
             }else if(i == 10){
                 pageNum.setText("" + i);
                 pageNum.setTextSize(8);
             }else{
                 pageNum.setText("" + i);
                 pageNum.setTextSize(8);
             }

             pageNum.setOnClickListener(new PageOnClickListener(page_array_list.get(k),  String.valueOf(i)));
             pageNum.setLayoutParams(new LinearLayout.LayoutParams(
                     ViewGroup.LayoutParams.WRAP_CONTENT,
                     ViewGroup.LayoutParams.WRAP_CONTENT,
                     1f
             ));
             pageSelect.addView(pageNum);
             k++;
         }
         book_list.addView(pageSelect);

    }

    private class BtnOnclickListener implements View.OnClickListener {
        private String bsign;
        public BtnOnclickListener(String booksign) {
            bsign = booksign;
        }
        @Override
        public void onClick(View v) {
            Intent in = new Intent(BookListView.this, NavigationView.class);
            in.putExtra("booksign", bsign);
            startActivity(in);
        }
    }
    private class TextOnClickListener implements View.OnClickListener{
        private String cid;
        public TextOnClickListener(String bookdetail){ cid = bookdetail;}

        @Override
        public void onClick(View v){
            Intent in = new Intent(BookListView.this,BookInfoView.class);
            in.putExtra("cid", cid);
            startActivity(in);
        }
    }

    private class PageOnClickListener implements View.OnClickListener{
        private String nextUrl;
        private String pageN;
        public PageOnClickListener(String pageUrl,String pageNum){
            nextUrl = pageUrl;
            pageN = pageNum;
        }

        @Override
        public void onClick(View v){
            Intent in = getIntent();
            in.putExtra("nexturl",nextUrl);
            in.putExtra("page",pageN);
            finish();
            startActivity(in);
        }
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
        ArrayList<String> title_array_list = new ArrayList<>();
        ArrayList<String> booksign_array_list = new ArrayList<>();
        ArrayList<String> cid_array_list = new ArrayList<>();
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
                Elements pageUrls = doc.select("div.pager a");

                for (int i = 0; i < titlelinks.size(); i += 2) {
                    String title = titlelinks.get(i).text();
                    String cid = titlelinks.get(i).attr("abs:href");
                    cid = cid.substring(cid.indexOf("cid=") + 4);
                    cid_array_list.add(cid);
                    title_array_list.add(title);
                }

                for (int i = 0; i < imglinks.size(); i += 3) {
                    String imgurl = imglinks.get(i).attr("abs:src");
                    imgurl_array_list.add(BitmapFactory.decodeStream(new URL(imgurl).openConnection().getInputStream()));
                }

                for (int i = 0; i < booksigns.size(); i += 1) {
                    String booksign = booksigns.get(i).text();

                    booksign = booksign.replace("중앙도서관 ▼ ", "");
                    if (!booksign.equals("대출중")) {
                        if (booksign.length() > 6) {
                            booksign = booksign.substring(1, booksign.length() - 6);
                        }
                    }
                    booksign_array_list.add(booksign);
                }

                for (int i = 0; i < pageUrls.size(); i += 1) {
                    String pager = pageUrls.get(i).attr("abs:href");
                    page_array_list.add(pager);
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
                    b.setBooksign(booksign_array_list.get(i));
                    b.setCid(cid_array_list.get(i));
                    book_array_list.add(b);
                }
                makeBook_list();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
