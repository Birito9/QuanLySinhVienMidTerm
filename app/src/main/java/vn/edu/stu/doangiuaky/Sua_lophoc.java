package vn.edu.stu.doangiuaky;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import vn.edu.stu.doangiuaky.Database.DataBase;

import java.util.ArrayList;

public class Sua_lophoc extends AppCompatActivity {
    final String DATABASE_NAME = "btl_qly_sinhvien.sqlite";
    SQLiteDatabase database;
    String malop = "";

    Button btnLuu, btnHuy;
    EditText editMaLop, editTenNganh, editTenLop, editGVCN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_lophoc);

        addControls();
        addEvents();
        laydulieu();

    }

    private void addEvents(){
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }

    //ÁNH XẠ
    private void addControls() {
        btnLuu = (Button) findViewById(R.id.btnLuu);
        btnHuy = (Button) findViewById(R.id.btnHuy);

        editMaLop = (EditText) findViewById(R.id.edt_s_malop);
        editTenNganh = (EditText) findViewById(R.id.edt_s_TenNganh);
        editTenLop = (EditText) findViewById(R.id.edt_s_TenLop);
        editGVCN = (EditText) findViewById(R.id.edt_s_GVCN);
    }

    //lấy dữ liệu đổ vào texr để sửa
    private void laydulieu() {
        Intent intent = getIntent();
        malop = intent.getStringExtra("malop");
        SQLiteDatabase database = DataBase.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM tbl_lop WHERE malop = ? ",new String[]{malop+""});
        cursor.moveToFirst();
        String malop = cursor.getString(0);
        String tennganh = cursor.getString(1);
        String tenlop = cursor.getString(2);
        String gvcn = cursor. getString(3);
        editTenNganh.setText(tennganh);
        editMaLop.setText(malop);
        editTenLop.setText(tenlop);
        editGVCN.setText(gvcn);
    }


    private void update(){
        String tennganh = editTenNganh.getText().toString();
        String tenlop = editTenLop.getText().toString();
        String gvcn = editGVCN.getText().toString();

        if(tenlop.equals("") || gvcn.equals("")){
            Toast.makeText(this, R.string.empty_info, Toast.LENGTH_SHORT).show();
            return;
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("tennganh", tennganh);
            contentValues.put("tenlop", tenlop);
            contentValues.put("gvcn", gvcn);

            SQLiteDatabase database = DataBase.initDatabase(this, "btl_qly_sinhvien.sqlite");
            database.update("tbl_lop", contentValues, "malop=?", new String[]{malop + ""});
            Toast.makeText(this, R.string.edit_success, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity_Lop.class);
            startActivity(intent);
        }
    }

    private void cancel(){
        Intent intent = new Intent(this, MainActivity_Lop.class);
        startActivity(intent);
    }
}