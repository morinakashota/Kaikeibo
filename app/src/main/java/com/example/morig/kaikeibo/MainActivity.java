package com.example.morig.kaikeibo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //金額入力画面遷移
        Button kingaku1 = (Button) findViewById(R.id.button1);
        kingaku1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Kingaku.class);
                startActivity(intent);
            }
        });

        //グラフ画面遷移
        Button gurafu = (Button) findViewById(R.id.button);
        gurafu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Gurafu.class);
                startActivity(intent);
            }
        });


// 日時のフォーマットオブジェクト作成
        Date now = new Date(System.currentTimeMillis());
        DateFormat formatter = new SimpleDateFormat("yyyy年MM月");
        String nowText = formatter.format(now);

        TextView textView = (TextView) findViewById(R.id.header_month_text);
        textView.setText(nowText);
    }


    private class CalendarInfo {
        private int year;//対象の西暦年
        private int month;//対象の月
        private int startDay;//先頭曜日
        private int lastDate;//月末日付
        public int[][] calendarMatrix = new int[5][7];    //カレンダー情報テーブル

        /**
         * 　* カレンダー表オブジェクトを作成します。
         * 　* @param year 西暦年(..., 2005, 2006, 2007, ...)
         * 　* @param month 月(1, 2, 3, ..., 10, 11, 12)
         */
        public CalendarInfo(int year, int month) {
            this.year = year;
            this.month = month;
            this.createFields();

        }

        private void createFields() {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();

            // 月の初めの曜日を求めます。
            calendar.set(year, month - 1, 1); // 引数: 1月: 0, 2月: 1, ...
            this.startDay = calendar.get(Calendar.DAY_OF_WEEK);//曜日を取得

            // 月末の日付を求めます。
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            this.lastDate = calendar.get(Calendar.DATE);//日を取得
            int dayCount = 1;
            boolean isStart = false;
            boolean isEnd = false;

            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 7; j++) {
                    //初期値セット
                    this.calendarMatrix[i][j] = 0;

                    //先頭曜日確認
                    // startDay: 日曜日 = 1, 月曜日 = 2, ...
                    if (isStart == false && (this.startDay - 1) == i) {
                        //日にちセット開始
                        isStart = true;
                    }

                    if (isStart) {
                        //終了日まで行ったか
                        if (!isEnd) {
                            this.calendarMatrix[i][j] = dayCount;
                        }

                        //カウント＋１
                        dayCount++;

                        //終了確認
                        if (dayCount > this.lastDate) {
                            isEnd = true;
                        }
                    }
                }
            }
        }

    }
}

