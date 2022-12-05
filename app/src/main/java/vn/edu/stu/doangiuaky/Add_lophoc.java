package vn.edu.stu.doangiuaky;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.stu.doangiuaky.Database.DataBase;

import java.util.ArrayList;

public class Add_lophoc extends AppCompatActivity {
    final String DATABASE_NAME = "btl_qly_sinhvien.sqlite";
    SQLiteDatabase database;
    Button btnLuu, btnHuy;
    EditText editMaLop, editTenNganh, editTenLop, editGVCN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lophoc);

        addControls();
        addEvents();

    }


    // even sự kiện click---------------------------------------------------------------
    private void addEvents() {
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }

    //ÁNH XẠ-----------------------------------------------------------------------
    private void addControls() {
        btnLuu = (Button) findViewById(R.id.btnLuu);
        btnHuy = (Button) findViewById(R.id.btnHuy);

        editMaLop = (EditText) findViewById(R.id.edt_t_malop);

        editTenNganh = (EditText) findViewById(R.id.edt_t_TenNganh);
        editTenLop = (EditText) findViewById(R.id.edt_t_TenLop);
        editGVCN = (EditText) findViewById(R.id.edt_t_GVCN);
    }

    private void insert(){
        String malop = editMaLop.getText().toString();
        String tennganh = editTenNganh.getText().toString();
        String tenlop = editTenLop.getText().toString();
        String gvcn = editGVCN.getText().toString();

        if(malop.equals("")|| tennganh.equals("") || tenlop.equals("") || gvcn.equals("")){
            Toast.makeText(this, R.string.empty_info, Toast.LENGTH_SHORT).show();
            return;
        } else {
            database = DataBase.initDatabase(this, DATABASE_NAME);
            Cursor cursor = database.rawQuery("SELECT * FROM tbl_lop WHERE malop =? ", new String[]{malop+""});
            if (cursor.moveToNext()){
                Toast.makeText(this, R.string.exists_class_id, Toast.LENGTH_SHORT).show();
                return;
            } else {
                ContentValues contentValues = new ContentValues();
                contentValues.put("malop", malop);
                contentValues.put("tennganh", tennganh);
                contentValues.put("tenlop", tenlop);
                contentValues.put("gvcn", gvcn);

                SQLiteDatabase database = DataBase.initDatabase(this, "btl_qly_sinhvien.sqlite");
                database.insert("tbl_lop", null, contentValues);
                Toast.makeText(this, R.string.added_new_class_success, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(this, MainActivity_Lop.class);
                Intent intent = new Intent(this, MainActivity_Lop.class);
                startActivity(intent);
            }
        }
    }

    private void cancel(){
        Intent intent = new Intent(this, MainActivity_Lop.class);
        startActivity(intent);
    }
}