package com.ppb.appkasir;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
//import android.support.design.widget.*;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.view.*;
//import android.support.v4.widget.*;
//import android.support.v7.app.*;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.widget.*;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ppb.appkasir.Adapter.BelanjaanDataAdapter;
import com.ppb.appkasir.Adapter.ProdukDataAdapter;
import com.ppb.appkasir.Fragment.belanjaFragment;
import com.ppb.appkasir.Fragment.productFragment;
import com.ppb.appkasir.Fragment.riwayatFragment;
import com.ppb.appkasir.Model.Produk;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class MainActivity extends AppCompatActivity
{
	DrawerLayout mDrawer;
	NavigationView nvDrawer;
	ActionBarDrawerToggle drawerToggle;
	Toolbar toolbar;
	public static ProdukDataAdapter dataproduk;
	public static BelanjaanDataAdapter dataBalanjaan;
//	public static LoginActivity buatlogout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		toolbar=(Toolbar) findViewById(R.id.mytoolbar);
		setSupportActionBar(toolbar);
		mDrawer=(DrawerLayout) findViewById(R.id.drawer_layout);
		nvDrawer=(NavigationView) findViewById(R.id.naView);
		setupDrawer(nvDrawer);
		drawerToggle= setupDrawerToggle();
		mDrawer.addDrawerListener(drawerToggle);
		getSupportFragmentManager().beginTransaction().replace(R.id.konten, new belanjaFragment()).commit();
		nvDrawer.getMenu().getItem(0).setChecked(true);
		setTitle("Belanja");
		reqPerms();
		dataproduk=new ProdukDataAdapter(this, Produk.getInit(this));
		dataBalanjaan=new BelanjaanDataAdapter(this);
//		buatlogout = new LoginActivity();
    }
	
	private void reqPerms(){
		PermissionGen.with(MainActivity.this)
			.addRequestCode(100)
			.permissions(
			Manifest.permission.CAMERA,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE)
			.request();
	}
	private void setupDrawer(NavigationView nview){
		nview.setNavigationItemSelectedListener(
			new NavigationView.OnNavigationItemSelectedListener(){
				public boolean onNavigationItemSelected(MenuItem menu){
					Terpilih(menu);
					return true;
				}
			});
	}
	private ActionBarDrawerToggle setupDrawerToggle(){
		return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_buka, R.string.drawer_tutup);
	}
	private void Terpilih(MenuItem menu){
		Class fragclass=null;
		Fragment frag = null;
		switch(menu.getItemId()){
			case R.id.frag1:
				fragclass = belanjaFragment.class;
				break;
			case R.id.frag2:
				fragclass= productFragment.class;
				break;

			case R.id.frag3:
				fragclass= riwayatFragment.class;
				break;

			case R.id.fragOcr:
				bukaocr();
				break;

			case R.id.fragLogout:
				profile();
				break;

			case R.id.logout:
				logout();
				break;

			default:
				fragclass=belanjaFragment.class;
		}
		try{
			frag=(Fragment) fragclass.newInstance();
		}catch(Exception e){}
		FragmentManager fm=getSupportFragmentManager();
		fm.beginTransaction().replace(R.id.konten, frag).commit();
		menu.setChecked(true);
		setTitle(menu.getTitle());
		mDrawer.closeDrawers();
	}

	private void logout() {
		Intent intentku = new Intent(getBaseContext(), LoginActivity.class);
		FirebaseAuth.getInstance().signOut();
		startActivityForResult(intentku,0);
	}

	private void bukaocr(){
		Intent intentku = new Intent(getBaseContext(), OcrActivity.class);
		startActivityForResult(intentku,0);
	}

	private void profile() {
//		new LogoutDialog(LogoutDialog.getCtx()).logout_dialog();
		Intent intentku = new Intent(getBaseContext(), ProfileActivity.class);
		startActivityForResult(intentku,0);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO: Implement this method
		switch (item.getItemId()){
			case android.R.id.home:
				mDrawer.openDrawer(GravityCompat.START);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mainmenu, menu);
		return true;
	}
	@PermissionSuccess(requestCode = 100)
	public void doSomething(){
		// Lakukan sesuatu disini
	}
	@PermissionFail(requestCode = 100)
	public void doFailSomething(){
		AlertDialog.Builder dlg=new AlertDialog.Builder(this);
		dlg.setTitle("Perijinan ditolak");
		dlg.setCancelable(false);
		dlg.setMessage("Untuk menggunakan Aplikasi ini kamu perlu membolehkan beberapa perijinan yang diajukan. Atau Aplikasi ini tidak bisa digunakan");
		dlg.setNegativeButton("Keluar", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface p1, int p2) {
					MainActivity.this.finish();
				}
			});
		dlg.show();
	}
	@Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
													 int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
	}
	@Override
	protected void onDestroy() {
		
		dataBalanjaan.total=0;

		super.onDestroy();
	}
}
