package com.ppb.appkasir.Model;
import java.util.*;
import android.database.*;

import com.ppb.appkasir.DBHelper;

import android.content.*;
import android.os.Parcel;
import android.os.Parcelable;

public class Produk implements Parcelable
{
	protected String nama;
	protected String sn;
	protected long harga;
	protected int stok;

	public Produk(String nama, String sn, long harga, int stok) {
		this.nama = nama;
		this.sn = sn;
		this.harga = harga;
		this.stok=stok;
	}

	protected Produk(Parcel in) {
		nama = in.readString();
		sn = in.readString();
		harga = in.readLong();
		stok = in.readInt();
	}

	public static final Creator<Produk> CREATOR = new Creator<Produk>() {
		@Override
		public Produk createFromParcel(Parcel in) {
			return new Produk(in);
		}

		@Override
		public Produk[] newArray(int size) {
			return new Produk[size];
		}
	};

	public void setNama(String nama) {
		this.nama = nama;
	}

	public int getStok() {
		return stok;
	}
	public void setStok(int stok) {
		this.stok = stok;
	}

	public String getNama() {
		return nama;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getSn() {
		return sn;
	}

	public void setHarga(long harga) {
		this.harga = harga;
	}

	public long getHarga() {
		return harga;
	}
	public static Produk getBySN(Context ctx, String SN){
		Cursor cur=new DBHelper(ctx).baca(SN);
		if(!cur.moveToFirst()) return null;
		if(cur.getCount()<1){
			return null;
		}
		String nama=cur.getString(cur.getColumnIndex("nama"));
		String sn=cur.getString(cur.getColumnIndex("sn"));
		long harga=cur.getLong(cur.getColumnIndex("harga"));
		int stok=cur.getInt(cur.getColumnIndex("stok"));
		return new Produk(nama, sn, harga, stok);
	}
	public static ArrayList<Produk> getInit(Context ctx){
		ArrayList<Produk> prod=new ArrayList<Produk>();
		Cursor cur=new DBHelper(ctx).semuaData();
		cur.moveToFirst();
		for(int i=0;i<cur.getCount();i++){
			cur.moveToPosition(i);
			String nama=cur.getString(cur.getColumnIndex("nama"));
			String sn=cur.getString(cur.getColumnIndex("sn"));
			long harga=cur.getLong(cur.getColumnIndex("harga"));
			int stok=cur.getInt(cur.getColumnIndex("stok"));
			prod.add(new Produk(nama, sn, harga, stok));
		}
		return prod;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(nama);
		dest.writeString(sn);
		dest.writeLong(harga);
		dest.writeInt(stok);
	}
}
