package com.example.hapusplant.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.hapusplant.LoginActivity;

import java.util.Date;

public class HapusPlantLiteDb extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "HapusPlantLite.db";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS user ("+
                    "jwtUser TEXT NOT NULL)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS user";

    private final Context context;

    public HapusPlantLiteDb(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
    }

    public String getJwtIfExists(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor c = sqLiteDatabase.query("user", null, null, null, null, null, null);
        String token = "";
        if(c.getCount() > 0){
            c.moveToFirst();
            token = c.getString(0);
            DecodedJWT jwt = JWT.decode(token);
            if(jwt.getExpiresAt().before(new Date()))
                token = "";
            token = "X-Access-Token=" + token;
        }

        if(token.equals("X-Access-Token=") || token.isEmpty()){
            sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
            sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
            if (!(context instanceof LoginActivity)){
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        }
        return token;
    }

    public void insertJwt(String token){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor c = sqLiteDatabase.query("user", null, null, null, null, null, null);
        if(c.getCount() > 0){
            sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
            sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
            ContentValues cv = new ContentValues();
            cv.put("jwtUser", token);
            sqLiteDatabase.insert("user", null, cv);
        }else{
            ContentValues cv = new ContentValues();
            cv.put("jwtUser", token);
            sqLiteDatabase.insert("user", null, cv);
        }
    }

    public void deleteJwt(){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }
}
