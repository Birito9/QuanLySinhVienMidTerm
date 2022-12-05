package vn.edu.stu.doangiuaky;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import vn.edu.stu.doangiuaky.Adapter.Adapter_SinhVien;
import vn.edu.stu.doangiuaky.Database.DataBase;
import vn.edu.stu.doangiuaky.Model.SinhVien;

import java.util.ArrayList;

public class MainActivity_SinhVien extends AppCompatActivity {
    final String DATABASE_NAME = "btl_qly_sinhvien.sqlite";
    SQLiteDatabase database;

    ImageButton btnAdd;
    ImageButton btnHome;
    ListView listView;
    ArrayList<SinhVien> list;
    Adapter_SinhVien adapter_sinhVien;
    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sinh_vien);
        listView = findViewById(R.id.listview_sinhvien);
        list = new ArrayList<>();
        adapter_sinhVien = new Adapter_SinhVien(this, list);
        listView.setAdapter(adapter_sinhVien);

        Click_home();
        Chick_Them();
        redata();
    }



    private void Click_home() {
        btnHome = findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_SinhVien.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //sự kiện onclik nút thêm---------------------------------------
    private void Chick_Them() {
        btnAdd = findViewById(R.id.btn_themSinhVien);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_SinhVien.this, Add_SinhVien.class);
                startActivity(intent);
            }
        });
    }

    //lấy dữ liệu từ database đổ lên listview
    private void redata(){
        database = DataBase.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM tbl_sinhvien ",null);
        list.clear();
        for (int i = 0; i <cursor.getCount(); i++){
            cursor.moveToPosition(i);
            String masv = cursor.getString(0);
            String malop = cursor.getString(1);
            String hoten = cursor.getString(2);
            String gioitinh = cursor.getString(3);
            String ngaysinh = cursor.getString(4);
            String diachi = cursor.getString(5);
            String sdt = cursor.getString(6);
            byte[] anh = cursor.getBlob(7);
            list.add(new SinhVien(masv, malop, hoten, gioitinh, ngaysinh, diachi, sdt, anh));
        }
        adapter_sinhVien.notifyDataSetChanged(); //adapter vẽ lại giao diện
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item_login) {
            Intent intent = new Intent(MainActivity_SinhVien.this, Activity_DangNhap.class);
            startActivity(intent);
        } else if (id == R.id.item_aboutme)
        {
            Intent intent = new Intent(MainActivity_SinhVien.this, AboutMeActivity.class);
            startActivity(intent);
        } else
        {
            Intent intent = new Intent(MainActivity_SinhVien.this, MainActivity.class);
            startActivity(intent);
        }
        return true;
    }
    //thay menu cũ thành menu search
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.btn_search).getActionView();
        //căn chỉnh menu cho đẹp (thanh search ôm chọn máy)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter_sinhVien.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter_sinhVien.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if(!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

}