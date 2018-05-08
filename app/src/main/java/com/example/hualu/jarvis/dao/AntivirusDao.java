package com.example.hualu.jarvis.dao;


import android.database.Cursor;;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;

public class AntivirusDao {
	SQLiteDatabase db;
	// 初始化
	public AntivirusDao() {
		// 数据库的位置
		String path = "/data/data/com.example.hualu.jarvis/files/antivirus.db";

		db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);

	}
	public String query(String md5){
		String result=null;
		Cursor cursor = db.rawQuery("select *  from datable where md5 = ?", new String[]{md5});
		if (cursor.moveToNext()) {
			result = cursor.getString(0);
			
		}
		return result;
	}
	/**
	 * 关闭数据库释放资源
	 */
	public void close(){
		if (db!=null) {
			db.close();
		}
	}
}
