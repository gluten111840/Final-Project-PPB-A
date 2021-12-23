package com.ppb.appkasir.Adapter;
import de.codecrafters.tableview.*;

import android.os.Build;
import android.view.*;
import java.util.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.ppb.appkasir.MainActivity;
import com.ppb.appkasir.Model.*;
import android.content.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.text.*;

import com.ppb.appkasir.DBHelper;
import com.ppb.appkasir.Model.Produk;

public class ProdukDataAdapter extends TableDataAdapter
{
	FirebaseFirestore myDB;
	List<String> list = new ArrayList<>();
	public static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
	public ProdukDataAdapter(Context ctx, ArrayList<Produk> prod){
		super(ctx, prod);
	}
	@Override
	public View getCellView(int row, int column, ViewGroup p3) {

		Produk produk = (Produk) getRowData(row);
		View render=null;

		myDB = FirebaseFirestore.getInstance();
		myDB.collection("Produk").addSnapshotListener(new EventListener<QuerySnapshot>() {
			@RequiresApi(api = Build.VERSION_CODES.N)
			@Override
			public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
				list.clear();
				for(DocumentSnapshot doc : queryDocumentSnapshots) {
					list.add(doc.getString("nama"));
					list.add(doc.getLong("harga").toString());
					list.add(String.valueOf(doc.get("stok")));
				}
				for(int i=0;i< list.size();i++) {
					System.out.println(i +" adalah " + list.get(i));
				}
			}
		});

		switch(column){
			case 0:
				render = renderString(produk.getNama());
				break;
			case 1:
				render = renderString("Rp. " + PRICE_FORMATTER.format(produk.getHarga()));
				break;
			case 2:
				render = renderString("" + produk.getStok());

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
		String sn = val.getAsString("sn");
		getData().add(new Produk(val.getAsString("nama"), val.getAsString("sn"), val.getAsLong("harga"), val.getAsInteger("stok")));
		Map<String, Object> data = new HashMap<>();
		data.put("nama", val.getAsString("nama"));
		data.put("sn", val.getAsString("sn"));
		data.put("harga", val.getAsLong("harga"));
		data.put("stok", val.getAsInteger("stok"));
		new DBHelper(getContext()).tambah(val);
		myDB = FirebaseFirestore.getInstance();
		myDB.collection("Produk")
				.document(sn)
				.set(data);

		notifyDataSetChanged();
	}
	public void hapus(Produk produk){
		getData().remove(produk);
		new DBHelper(getContext()).delete(produk.getSn());
		myDB = FirebaseFirestore.getInstance();
		myDB.collection("Produk").document(produk.getSn()).delete()
				.addOnSuccessListener(new OnSuccessListener<Void>() {
					@Override
					public void onSuccess(Void aVoid) {
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
					}
				});
		notifyDataSetChanged();
	}
	public void perbarui(Produk produk, ContentValues newdata){
		int idx=getpos(produk);//getData().indexOf(produk);
		if(idx>=0){
			
		}else{
			return;
		}
		getData().set(idx, new Produk(newdata.getAsString("nama"), newdata.getAsString("sn"), newdata.getAsLong("harga"), newdata.getAsInteger("stok")));
		new DBHelper(getContext()).update(newdata, produk.getSn());
		myDB = FirebaseFirestore.getInstance();
		Map<String, Object> data = new HashMap<>();
		data.put("nama", newdata.getAsString("nama"));
		data.put("sn", newdata.getAsString("sn"));
		data.put("harga", newdata.getAsLong("harga"));
		data.put("stok", newdata.getAsInteger("stok"));
		myDB.collection("Produk").document(produk.getSn()).update(data);
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
