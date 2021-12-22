package com.ppb.appkasir.Adapter;
import de.codecrafters.tableview.*;
import android.view.*;
import java.util.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ppb.appkasir.MainActivity;
import com.ppb.appkasir.Model.*;
import android.content.*;
import android.widget.*;

import androidx.annotation.NonNull;

import java.text.*;

import com.ppb.appkasir.DBHelper;
import com.ppb.appkasir.Model.Produk;

public class ProdukDataAdapter extends TableDataAdapter
{
	FirebaseDatabase firebaseDatabase;
	DatabaseReference databaseReference;
	public static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
	public ProdukDataAdapter(Context ctx, ArrayList<Produk> prod){
		super(ctx, prod);
	}
	@Override
	public View getCellView(int row, int column, ViewGroup p3) {
		Produk produk = (Produk) getRowData(row);
		View render=null;
		switch(column){
			case 0:
				render=renderString(produk.getNama());
				break;
			case 1:
				render=renderString("Rp. "+PRICE_FORMATTER.format(produk.getHarga()));
				break;
			case 2:
				render=renderString(""+produk.getStok());
				break;
		}
		return render;
	}
	private int getpos(Produk p){
		int pos = -1;
		for(Object pp:getData()){
			Produk ppp=(Produk)pp;
			if(ppp.getSn().equals(p.getSn())){
				pos=getData().indexOf(pp);
				break;
			}
		}
		return pos;
	}
	public void tambah(ContentValues val){
		Produk product = new Produk(val.getAsString("nama"), val.getAsString("sn"), val.getAsLong("harga"), val.getAsInteger("stok"));
		String sn = val.getAsString("sn");
		getData().add(new Produk(val.getAsString("nama"), val.getAsString("sn"), val.getAsLong("harga"), val.getAsInteger("stok")));
		new DBHelper(getContext()).tambah(val);
		firebaseDatabase = FirebaseDatabase.getInstance();
		// on below line creating our database reference.
		databaseReference = firebaseDatabase.getReference("Produk");
		databaseReference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot snapshot) {
				// on below line we are setting data in our firebase database.
				databaseReference.child(sn).setValue(product);

			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {
				// TODO
			}
		});

		notifyDataSetChanged();
	}
	public void hapus(Produk produk){
		getData().remove(produk);
		String sn = produk.getSn();
		firebaseDatabase = FirebaseDatabase.getInstance();
		// on below line we are initialing our database reference and we are adding a child as our course id.
		databaseReference = firebaseDatabase.getReference("Produk").child(sn);
		// on below line calling a method to delete the course.
		databaseReference.removeValue();
		new DBHelper(getContext()).delete(produk.getSn());
		notifyDataSetChanged();
	}
	public void perbarui(Produk produk, ContentValues newdata){
		Produk product = new Produk(newdata.getAsString("nama"), newdata.getAsString("sn"), newdata.getAsLong("harga"), newdata.getAsInteger("stok"));
		int idx=getpos(produk);//getData().indexOf(produk);
		if(idx>=0){
			
		}else{
			return;
		}
		getData().set(idx, new Produk(newdata.getAsString("nama"), newdata.getAsString("sn"), newdata.getAsLong("harga"), newdata.getAsInteger("stok")));
		new DBHelper(getContext()).update(newdata, produk.getSn());
		notifyDataSetChanged();
	}
	private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(14);
        return textView;
    }
	
}
