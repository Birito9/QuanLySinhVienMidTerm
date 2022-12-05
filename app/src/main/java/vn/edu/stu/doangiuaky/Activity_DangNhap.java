package vn.edu.stu.doangiuaky;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import vn.edu.stu.doangiuaky.Database.DataBase;

public class Activity_DangNhap extends AppCompatActivity {
    final String DATABASE_NAME = "btl_qly_sinhvien.sqlite";
    SQLiteDatabase database;

    Button btnDN, btnDK;
    EditText editUser, editPassword;
    Button changLang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        addControls();
        addEvents();

        //change actionbar title if you dont change it will be according to your systems default language
        ActionBar ActionBar = getSupportActionBar();
        ActionBar.setTitle(getResources().getString(R.string.app_name));

        loadLocale();
        changLang = (Button) findViewById(R.id.btn_text_language);

        changLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Hiện danh sách list array để chọn ngôn ngữ
                showbtnChangeToViDialog();
            }
        });
    }

    private void showbtnChangeToViDialog() {
        //Mảng ngôn ngữ hiển thị trong hộp cảnh báo
        final String launguages[] = {"English" , "Vietnamese"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Choose Launguage");
        mBuilder.setSingleChoiceItems(launguages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (which == 0) {
                    setLocale("en");
                    recreate();
                }
                else if (which == 1) {
                    setLocale("vi");
                    recreate();
                }
            }
        });
        mBuilder.create();
        mBuilder.show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration , getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings" , MODE_PRIVATE).edit();
        editor.putString("app_lang" , lang);
        editor.apply();
    }

    //load language saved in shared preferences
    private void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String lang = prefs.getString("app_lang", "");
        setLocale(lang);
    }

    //ÁNH XẠ
    private void addControls() {
        btnDN = (Button) findViewById(R.id.btn_login);
        btnDK = (Button) findViewById(R.id.btn_register);

        editUser = (EditText) findViewById(R.id.edt_user);
        editPassword = (EditText) findViewById(R.id.edt_password);
    }

    //oclick đăng ký
    private void addEvents() {
        btnDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DangNhap();
            }
        });

        btnDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_DangNhap.this, Activity_DangKy.class);
                startActivity(intent);
            }
        });
    }

    private void DangNhap(){
        String user = editUser.getText().toString();
        String pass = editPassword.getText().toString();

        if(user.equals("") || pass.equals("")){
            Toast.makeText(this, R.string.empty_username_pass, Toast.LENGTH_SHORT).show();
            return;
        } else {
            database = DataBase.initDatabase(this, DATABASE_NAME);
            Cursor cursor = database.rawQuery("SELECT * FROM tbl_dangnhap WHERE username =? and password =? ", new String[]{user+"", pass});
            if (cursor.moveToNext()){
                Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show();
            }
        }
    }
}