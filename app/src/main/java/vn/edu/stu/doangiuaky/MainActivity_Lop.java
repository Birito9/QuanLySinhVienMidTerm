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
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import vn.edu.stu.doangiuaky.Adapter.Adapter_Lop;
import vn.edu.stu.doangiuaky.Database.DataBase;
import vn.edu.stu.doangiuaky.Model.Lop;

import java.util.ArrayList;

public class MainActivity_Lop extends AppCompatActivity {
    final String DATABASE_NAME = "btl_qly_sinhvien.sqlite";
    SQLiteDatabase database;

    ImageButton btnAdd;
    ImageButton btnHome;
    ListView listView;
    ArrayList<Lop> list;
    Adapter_Lop adapter_lop;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lop);

        listView = findViewById(R.id.listview_lophoc);
        list = new ArrayList<>();
        adapter_lop = new Adapter_Lop(this, list);
        listView.setAdapter(adapter_lop);

        Click_home();
        Chick_Them();
        redata();
    }

    private void Click_home() {
        btnHome = findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_Lop.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //sự kiện onclik nút thêm---------------------------------------
    private void Chick_Them() {
        btnAdd = findViewById(R.id.btn_themlophoc);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity_Lop.this, Add_lophoc.class);
                startActivity(intent);
            }
        });
    }

    //lấy dữ liệu từ database đổ lên listview
    private void redata(){
        database = DataBase.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM tbl_lop ",null);
        list.clear();
        for (int i = 0; i <cursor.getCount(); i++){
            cursor.moveToPosition(i);
            String malop = cursor.getString(0);
            String tennganh = cursor.getString(1);
            String tenlop = cursor.getString(2);
            String gvcn = cursor.getString(3);
            list.add(new Lop(malop, tennganh, tenlop, gvcn));
        }
        adapter_lop.notifyDataSetChanged(); //adapter vẽ lại giao diện
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item_login) {
            Intent intent = new Intent(MainActivity_Lop.this, Activity_DangNhap.class);
            startActivity(intent);
        } else if (id == R.id.item_aboutme)
        {
            Intent intent = new Intent(MainActivity_Lop.this, AboutMeActivity.class);
            startActivity(intent);
        } else
        {
            Intent intent = new Intent(MainActivity_Lop.this, MainActivity.class);
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
                adapter_lop.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter_lop.getFilter().filter(newText);
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