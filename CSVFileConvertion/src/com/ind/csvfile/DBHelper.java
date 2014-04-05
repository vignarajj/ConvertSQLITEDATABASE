package com.ind.csvfile;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "basic_db";
	private static final String TABLE_BASIC = "basic_table";
	private static final String KEY_ID = "_id";
	private static final String KEY_NAME = "name";
	private static final String KEY_AGE = "age";
	private static final String KEY_ADDRESS = "addr";
	private static final String KEY_BLOODGROUP = "bloodgroup";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_BASICTABLE = "CREATE TABLE " + TABLE_BASIC + "( "
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_AGE + " TEXT," + KEY_ADDRESS + " TEXT," + KEY_BLOODGROUP
				+ " TEXT" + ")";
		db.execSQL(CREATE_BASICTABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BASIC);
		onCreate(db);
	}

	public void insertValues(BasicItems basicItems) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NAME, basicItems.getName());
		values.put(KEY_AGE, basicItems.getAge());
		values.put(KEY_ADDRESS, basicItems.getAddress());
		values.put(KEY_BLOODGROUP, basicItems.getBloodGroup());
		System.out.println("Datas are "+basicItems.getName() +" "+ basicItems.getAge()
				+ " "+basicItems.getAddress() + " "+basicItems.getBloodGroup());
		
		db.insert(TABLE_BASIC, null, values);
		db.close();
	}

	public List<BasicItems> getAllItems() {
		List<BasicItems> basicItems = new ArrayList<BasicItems>();
		String selectQuery = "SELECT * FROM " + TABLE_BASIC;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				BasicItems bi = new BasicItems();
				bi.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
				bi.setAge(cursor.getString(cursor.getColumnIndex(KEY_AGE)));
				bi.setAddress(cursor.getString(cursor
						.getColumnIndex(KEY_ADDRESS)));
				bi.setBloodGroup(cursor.getString(cursor
						.getColumnIndex(KEY_BLOODGROUP)));
				basicItems.add(bi);
			} while (cursor.moveToNext());
		}
		return basicItems;
	}

	public int getProfilesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_BASIC;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int cnt = cursor.getCount();
		cursor.close();
		return cnt;
	}
}
