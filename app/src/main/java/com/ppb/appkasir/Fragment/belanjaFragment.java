package com.ppb.appkasir.Fragment;
import android.support.v4.app.*;
import android.os.*;
import android.view.*;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ppb.appkasir.R;
import com.ppb.appkasir.Adapter.BelanjaanDataAdapter;

import android.widget.*;

import androidx.fragment.app.Fragment;

import de.codecrafters.tableview.listeners.*;

import com.ppb.appkasir.MainActivity;
import com.ppb.appkasir.Model.Belanjaan;
import com.ppb.appkasir.Widget.EditorDialog;
import com.ppb.appkasir.Widget.inputProdukScanner;
import com.ppb.appkasir.tabelBelanjaan;

public class belanjaFragment extends Fragment
{
	public static TextView totaljum;
	public static BottomSheetBehavior bottomSheetBehavior;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.belanja, container, false);
		tabelBelanjaan velanjaan=(tabelBelanjaan) v.findViewById(R.id.belanjaan);
		velanjaan.setDataAdapter(MainActivity.dataBalanjaan);
		velanjaan.addDataClickListener(new DtaClickListener());
		velanjaan.addDataLongClickListener(new DataLongClickListener());
		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		FloatingActionButton fabshop=(FloatingActionButton) view.findViewById(R.id.fab_shopping);
		totaljum=(TextView) view.findViewById(R.id.totaljumlah);
		bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottomSheet));
		
		fabshop.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View p1) {
					new inputProdukScanner(p1.getContext()).shoping();
				}
			});
		
		if(BelanjaanDataAdapter.total!=0){
			totaljum.setText("Rp. "+BelanjaanDataAdapter.PRICE_FORMATTER.format(BelanjaanDataAdapter.total));
			bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
		}else{
			bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
		}
	}
	private class DtaClickListener implements TableDataClickListener<Belanjaan> {
		@Override
		public void onDataClicked(int p1, Belanjaan belanjaan) {
			
			//new inputProdukScanner(getActivity()).shoping();
			new EditorDialog(getActivity(), belanjaan, totaljum);
		}
    }
	private class DataLongClickListener implements TableDataLongClickListener<Belanjaan> {
        @Override
        public boolean onDataLongClicked(int rowIndex, Belanjaan belanjaan) {
            //showdlg(belanjaan.getProduk().getNama(), belanjaan.getProduk().getHarga(), belanjaan.getQuantity());
			return true;
        }
    }
	
}
