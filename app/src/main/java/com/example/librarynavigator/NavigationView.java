package com.example.librarynavigator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class NavigationView extends AppCompatActivity {
    public static int id = R.drawable.minimap;
    public String booksign;
    private ArrayList<String> visit_path;
    private int dest_dot_num;

    private int flag;
    ArrayList<Bookshelf> bookshelves = new ArrayList<>();
    ArrayList<Dot> dots = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        booksign = in.getStringExtra("booksign");
        flag = in.getIntExtra("flag",0);
        Log.d("mingidev", booksign);

        make_floor();
        int bookshelf_num = getbookshelf_num(booksign);
        dest_dot_num = getDotNum(bookshelf_num);

        findPath(17, dest_dot_num);

        int resid = getResources().getIdentifier("bs0_"+String.valueOf(bookshelf_num), "drawable", getApplicationContext().getPackageName());
        id = resid;

        int dot_num = getDotNum(bookshelf_num);
        if(flag == 1) {
            resid = getResources().getIdentifier("bs0_" + String.valueOf(bookshelf_num), "drawable", getApplicationContext().getPackageName());
            id = resid;
        }else if(flag == 2){
            resid = getResources().getIdentifier("bs1_" + String.valueOf(bookshelf_num), "drawable", getApplicationContext().getPackageName());
            id = resid;
        }else{
            id = R.drawable.minimap;
        }
        findPath(17, dot_num);
        setContentView(R.layout.navigation_view);
    }

    public void onClick1(View v){
        Intent in = getIntent();
        in.putExtra("flag",1);
        finish();
        startActivity(in);
    }

    public void onClick2(View v){
        Intent in = getIntent();
        in.putExtra("flag",2);
        finish();
        startActivity(in);
    }

    private void findPath(int start_dot, int dest_dot) {
        Dot dot = null;
        for (int i = 0; i < dots.size(); i++) {
            dot = dots.get(i);
            if (dot.getNum() == start_dot) { break; }
        }

        int visit[] = new int[dots.size()];
        Queue<Dot> queue = new ArrayDeque<>();
        queue.offer(dot);

        boolean find = false;
        visit[start_dot-1] = 1;
        while(!find) {
            Dot d = queue.poll();
            if (d.getNum() == dest_dot) { break; }

            for (int i = 0; i < 4; i++) {
                ArrayList<Dot> sideds = d.getSideDots(i);
                for (int j = 0; j < sideds.size(); j++) {
                    Dot side_d = sideds.get(j);
                    if (visit[side_d.getNum()-1] == 0) {
                        queue.offer(side_d);
                        visit[side_d.getNum()-1] = d.getNum();
                    }
                }
            }
        }

        ArrayList<String> imgurls = new ArrayList<String>();
        int imgurlIdx = 0;

        int path[] = new int[visit.length];
        int mm = 0;
        for (int i = dest_dot; i != 1; i = visit[i-1], mm++) {
            path[mm]= i;
        }

        for (int i = mm-1; i >= 1; i--) {
            int idx = path[i];
            Dot d = dots.get(idx-1);
            ArrayList<Dot> side_ds;
            for (int j = 0; j < 4; j++) {
                side_ds = d.getSideDots(j);
                for (int k = 0; k < side_ds.size(); k++) {
                    Dot side_d = side_ds.get(k);
                    if (side_d.getNum() == path[i-1]) { // 찾는 점이 있으면
                        if (j == 0) { // top
                            imgurls.add(""+path[i] + "_T.jpg");
                        } else if (j == 1) { // bottom
                            imgurls.add(""+path[i]+ "_B.jpg");
                        }else if (j == 2) { // left
                            imgurls.add(""+path[i]+ "_L.jpg");
                        }else if (j == 3) { // right
                            imgurls.add(""+path[i]+ "_R.jpg");
                        }
                    }

                }
            }
        }
        imgurls.add(""+dest_dot+"_R_0.png");
        imgurls.add("last_0.jpg");
        visit_path = imgurls;
    }

    private int getDotNum(int bookshelf_num) {
        int dot_num = 0;
        for (int i = 0; i< bookshelves.size(); i++) {
            Bookshelf bookshelf = bookshelves.get(i);
            if (bookshelf.getBookshelf_num() == bookshelf_num) {
                dot_num = bookshelf.getDot_num();
            }
        }
        return dot_num;
    }

    private int getbookshelf_num(String booksign) {
        int result = 0;

        Bookshelf bookshelf;
        ArrayList<String> bs0;
        ArrayList<String> bs1;

        String item_l;
        String item_r;
        for (int i = 0; i < bookshelves.size(); i++) {
            bookshelf = bookshelves.get(i);
            bs0 = bookshelf.getSide0();
            bs1 = bookshelf.getSide1();

            for (int j = 1; j < bs0.size(); j++) {
                item_l = bs0.get(j-1);
                item_r = bs0.get(j);
                int comp_l = item_l.compareTo(booksign);
                int comp_r = item_r.compareTo(booksign);
                if (comp_l <= 0 /*&& comp_r > 0*/) {
                    return bookshelf.getBookshelf_num();
                }
            }

            for (int j = 1; j < bs1.size(); j++) {
                item_l = bs1.get(j-1);
                item_r = bs1.get(j);
                int comp_l = item_l.compareTo(booksign);
                int comp_r = item_r.compareTo(booksign);
                if (comp_l <= 0 /*&& comp_r > 0*/) {
                    return bookshelf.getBookshelf_num();
                }
            }
        }
        return result;
    }

    private void make_floor() {
        connect_dots();
        make_bookshelves(bookshelves);
        set_dot_num(bookshelves);
    }

    private void connect_dots() {
        ArrayList<String> dot_conn = read_csv_as_line(R.raw.connection);
        for (int i = 1; i < dot_conn.size(); i++) {
            dots.add(new Dot(i));
        }
        Dot dot;
        for (int i = 1; i < dot_conn.size(); i++) {
            String s[] = dot_conn.get(i).split(",");
            if (s.length < 2) { continue; }
            dot = dots.get(i-1);
            for (int side = 0; side < 4 && side + 1 < s.length; side++) {
                if (s[side] != null) {
                    String t[] = s[side+1].split(";");
                    ArrayList<Dot> side_dots = dot.getSideDots(side);
                    for (int j = 0; j < t.length; j++) {
                        if (t[j].equals("")) { continue; }
                        int d_idx = Integer.parseInt(t[j]) - 1;
                        side_dots.add(dots.get(d_idx));
                    }
                }
            }
        }
    }

    private void set_dot_num(ArrayList<Bookshelf> bookshelves) {
        ArrayList<String> dot_bs = read_csv_as_line(R.raw.dot_bookshelf);
        for (int i = 1; i < dot_bs.size(); i++) {
            String s[] = dot_bs.get(i).split(",");
            int dot_num = Integer.parseInt(s[0]);

            if (s.length < 2) {continue;}
            s = s[1].split(";");

            for (int j = 0; j < bookshelves.size(); j++) {
                Bookshelf bs = bookshelves.get(j);
                // set dest_dot_num
                for (int k = 0; k < s.length; k++) {
                    if (bs.getBookshelf_num() == Integer.parseInt(s[k])) {
                        bs.setDot_num(dot_num);
                    }
                }
            }
        }
    }

    private void make_bookshelves(ArrayList<Bookshelf> bookshelves) {
        ArrayList<String> sb = read_csv_as_line(R.raw.lib_book);
        for (int i = 1; i < sb.size(); i+=2) {
            lib_book_one_line(bookshelves, sb, i);
            lib_book_one_line(bookshelves, sb, i+1);
        }
    }

    private void lib_book_one_line(ArrayList<Bookshelf> bookshelves, ArrayList<String> sb, int i) {
        String line = sb.get(i);
        String s[] = line.split(",");

        int bookShelfNum = Integer.parseInt(s[0]);
        int side = Integer.parseInt(s[1]);

        Bookshelf bs = getBookshelf(bookshelves, i, bookShelfNum);
        bs.setBookshelf_num(bookShelfNum);

        ArrayList<String> booksigns = new ArrayList<>();
        for (int j = 2; j < s.length; j++) {
            booksigns.add(s[j]);
        }
        if (side == 0) {
            bs.setSide0(booksigns);
        } else if (side == 1) {
            bs.setSide1(booksigns);
        }


    }

    private Bookshelf getBookshelf(ArrayList<Bookshelf> bookshelves, int i, int bookShelfNum) {
        Bookshelf bs = null;
        Boolean find = false;
        for (int j = 0; j < bookshelves.size(); j++) {
            bs = bookshelves.get(j);
            if (bs.getBookshelf_num() == bookShelfNum) {
                find = true;
                break;
            }
        }
        if (find == false) {
            bs = new Bookshelf();
            bookshelves.add(bs);
        }
        return bs;
    }

    private ArrayList<String> read_csv_as_line(int resourceId) {
        InputStreamReader is = new InputStreamReader(getResources().openRawResource(resourceId));
        BufferedReader reader = new BufferedReader(is);

        ArrayList<String> result = new ArrayList<>();
        try {
            String line;
            while ((line = reader.readLine()) != null){
                result.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void onGopicturebtnClick(View v) {
        Intent in = new Intent(NavigationView.this, PictureView.class);
        in.putStringArrayListExtra("visit_path", visit_path);
        startActivity(in);
    }
}

