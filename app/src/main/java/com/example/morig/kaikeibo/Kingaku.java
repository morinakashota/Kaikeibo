package com.example.morig.kaikeibo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Kingaku extends AppCompatActivity {
    CreateProductHelper helper;
    EditText id;
    EditText name;
    EditText price;
    TextView msgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kingaku);

        Button button = (Button) findViewById(R.id.button36);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });

        id = (EditText)findViewById(R.id.id);
        name= (EditText)findViewById(R.id.name);
        price = (EditText)findViewById(R.id.price);
        msgView = (TextView)findViewById(R.id.message);
        helper = new CreateProductHelper(this);

        Button insert =(Button)findViewById(R.id.insert);
        insert.setOnClickListener(new ButtonInsert());

        Button update =(Button)findViewById(R.id.update);
        update.setOnClickListener(new ButtonUpdate());

        Button delete =(Button)findViewById(R.id.delete);
        delete.setOnClickListener(new ButtonDelete());

        Button show =(Button)findViewById(R.id.show);
        show.setOnClickListener(new ButtonShow());
    }

    //ヘルパークラス
    class CreateProductHelper extends SQLiteOpenHelper{
        public CreateProductHelper(Context con){
            super(con,"dbsample",null,1);
        }
        @Override
        public void onCreate(SQLiteDatabase db){
            String sql = "create table product("
                    +"_id integer primary key autoincrement,"
                    +"productid integer not null,"
                    +"name text not null,"
                    +"price integer default 0"
                    +")";
            db.execSQL(sql);
            Toast.makeText(Kingaku.this,"テーブル作成しました。",Toast.LENGTH_LONG).show();

        }
        @Override
        public void onUpgrade(SQLiteDatabase db,int oldvesion,int newversion){
            ;
        }
    }
    class ButtonInsert implements View.OnClickListener {
        public void onClick(View v) {
            String no = id.getText().toString();
            if (no.equals("")) {
                msgView.setText("日付を入力してください");
                return;
            }
            ContentValues val = new ContentValues();
            val.put("productid", no);
            val.put("name", name.getText().toString());
            String priceStr = price.getText().toString();
            if (priceStr.length() > 0) {
                val.put("price", priceStr);
            }
            SQLiteDatabase db = helper.getWritableDatabase();
            db.beginTransaction();
            try {
                db.insert("product", null, val);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            db.close();
            msgView.setText("データ登録しました");
        }
    }

          class ButtonUpdate implements View.OnClickListener {
        public void onClick(View v){
            String no=id.getText().toString();
            if(no.equals("")){
                msgView.setText("日付を入力してください");
                return;
            }
            ContentValues val = new ContentValues();
            val.put("name", name.getText().toString());
            String priceStr = price.getText().toString();
            if(priceStr.length()>0){
                val.put("price",priceStr);
            }

            SQLiteDatabase db =helper.getWritableDatabase();
            db.beginTransaction();
            int rtn;
            try{
                String condition = "productid ='"+no+"'";
                rtn = db.update("product",val,condition,null);
                db.setTransactionSuccessful();
            }finally {
                db.endTransaction();
            }
            db.close();
            msgView.setText(rtn+"件更新しました。");
        }
    }
    class ButtonDelete implements View.OnClickListener{
        public void onClick(View v){
            String no=id.getText().toString();
            if(no.equals("")){
                msgView.setText("日付を入力してください");
                return;
            }
            SQLiteDatabase db =helper.getWritableDatabase();
            db.beginTransaction();
            int rtn;
            try{
                String condition="productid = '"+no+"'";
                rtn = db.delete("product",condition,null);
                db.setTransactionSuccessful();
            }
        finally{
                db.endTransaction();
            }
            db.close();
            msgView.setText(rtn+"件削除しました");

        }
    }
    class ButtonShow implements View.OnClickListener {
        public void onClick(View v){
            SQLiteDatabase db = helper.getReadableDatabase();
            String columns[] = {"productid","name","price"};
            Cursor cur =db.query("product",columns,null,null,null,null,"productid");

            String data = "";
            while(cur.moveToNext()){
                data +=cur.getString(0)+","+cur.getString(1)+","+cur.getString(2)+"\n";
            }
            db.close();

            TextView view =(TextView)findViewById(R.id.data);
            view.setText(data);

            msgView.setText("データを取得しました。");
        }
    }
}
