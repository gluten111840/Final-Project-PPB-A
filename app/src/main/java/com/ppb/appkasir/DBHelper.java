package com.ppb.appkasir;
import android.database.sqlite.*;
import android.content.*;
import android.database.*;

public class DBHelper extends SQLiteOpenHelper
{
	SQLiteDatabase dbw;
	public static String namaTable="produk";
	public static String namaTransaksi = "transaksi";
	public DBHelper(Context ctx){
		super(ctx, namaTable+".db", null, 2);
		dbw=getWritableDatabase();
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table "+namaTable+" (id INTEGER primary key autoincrement not null, sn TEXT not NULL, nama TEXT not NULL,harga INTEGER not NULL, stok INTEGER not NULL)");
		db.execSQL("create table "+namaTransaksi+" (id INTEGER primary key autoincrement not null, total INTEGER not null)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int p2, int p3) {
		db.execSQL("DROP TABLE IF EXISTS "+namaTable);
		db.execSQL("DROP TABLE IF EXISTS "+namaTransaksi);
	}
	public void tambah(ContentValues val){
		dbw.insert(namaTable, null, val);
	}
	public void update(ContentValues val, String sn){
		dbw.update(namaTable, val, "sn="+sn, null);
	}
	public void delete(String sn){
		dbw.delete(namaTable, "sn="+sn, null);
	}

	public void tambahTransaksi(ContentValues val){
		dbw.insert(namaTransaksi, null, val);
	}
	public Cursor semuatTotal(){
		Cursor cur = dbw.rawQuery("SELECT * FROM "+namaTransaksi, null);
		return cur;
	}

	public Cursor semuaData() {
        Cursor cur = dbw.rawQuery("SELECT * FROM "+namaTable, null);
        return cur;
    }
	public Cursor baca(String sn) {
        Cursor cur = dbw.rawQuery("SELECT * FROM "+namaTable+" WHERE sn="+sn, null);
        return cur;
    }
}
