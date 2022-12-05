package vn.edu.stu.doangiuaky;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import vn.edu.stu.doangiuaky.Adapter.Adapter_SinhVien;
import vn.edu.stu.doangiuaky.Database.DataBase;
import vn.edu.stu.doangiuaky.Model.Lop;
import vn.edu.stu.doangiuaky.Model.SinhVien;

public class View_SinhVien extends AppCompatActivity {
    final String DATABASE_NAME = "btl_qly_sinhvien.sqlite";
    SQLiteDatabase database;
    String masv = "";
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;
    private String gioitinh = "";

    Button btnSua, btnXoa, btnBack;
    EditText editMaSV, editHoTen, editNgaySinh, editDiaChi, editSDT;
    ImageView imgAnhChup;
    Spinner spinnerMaLop;
    RadioGroup rgGioiTinh;
    RadioButton rbNam, rbNu;
    Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sinh_vien);

        addControls();
        addEvents();
        laydulieu();
        Spinner_MaLop();
        spinnerMaLop.setEnabled(false);
        rgGioiTinh.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.rbNam) {
                    gioitinh ="Nam";
                } else {
                    gioitinh ="Nữ";
                }
            }
        });
    }

    // lấy nguồn combobox mã lop---------------------------------------------------------
    private void Spinner_MaLop() {
        ArrayList<String> arrayMaLop =new ArrayList<String>();
        database = DataBase.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT malop FROM tbl_lop ",null);
        arrayMaLop.clear();
        for (int i = 0; i <cursor.getCount(); i++){
            cursor.moveToPosition(i);
            String ten = cursor.getString(0);
            arrayMaLop.add(ten);
        }
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,arrayMaLop);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaLop.setAdapter(arrayAdapter);
    }

    private void addEvents(){
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(View_SinhVien.this, Sua_SinhVien.class);
                intent.putExtra("masv", masv);
                startActivity(intent);
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(View_SinhVien.this);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle(R.string.confirm_delete);
                builder.setMessage(R.string.confirm_delete_sub);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteDatabase database = DataBase.initDatabase(View_SinhVien.this, DATABASE_NAME);
                        database.delete("tbl_sinhvien", "masv = ?",new String[]{masv + ""});
                        Toast.makeText(View_SinhVien.this, R.string.remove_success, Toast.LENGTH_SHORT).show();
                        editMaSV.setText("");
                        editHoTen.setText("");
                        rbNam.setChecked(true);
                        editNgaySinh.setText("");
                        editDiaChi.setText("");
                        editSDT.setText("");
                        Intent intent = new Intent(View_SinhVien.this, MainActivity_SinhVien.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });

    }

    //ÁNH XẠ
    private void addControls() {
        btnSua = (Button) findViewById(R.id.btnSua);
        btnXoa = (Button) findViewById(R.id.btnXoa);
        btnBack = (Button) findViewById(R.id.btnBack);

        editMaSV = (EditText) findViewById(R.id.edt_s_MaSV);
        spinnerMaLop = (Spinner) findViewById(R.id.spinner_s_MaLop);
        editHoTen = (EditText) findViewById(R.id.edt_s_HoTen);
        rgGioiTinh = (RadioGroup) findViewById(R.id.rgGioiTinh_s);
        rbNam = (RadioButton) findViewById(R.id.rbNam);
        rbNu = (RadioButton) findViewById(R.id.rbNu);
        editNgaySinh = (EditText) findViewById(R.id.edt_s_NgaySinh);
        editDiaChi = (EditText) findViewById(R.id.edt_s_DiaChi);
        editSDT = (EditText) findViewById(R.id.edt_s_SDT);

        imgAnhChup =findViewById(R.id.img_AnhChup);
        editHoTen.setKeyListener(null);
        editDiaChi.setKeyListener(null);
        editNgaySinh.setKeyListener(null);
        editSDT.setKeyListener(null);
        editMaSV.setKeyListener(null);
    }

    private void laydulieu() {
        Intent intent = getIntent();
        masv = intent.getStringExtra("masv");
        SQLiteDatabase database = DataBase.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM tbl_sinhvien WHERE masv = ? ",new String[]{masv + ""});
        cursor.moveToFirst();
        String masv = cursor.getString(0);
        String malop = cursor.getString(1);
        String hoten = cursor.getString(2);
        String gioitinh_laynguon = cursor.getString(3);
        String ngaysinh = cursor.getString(4);
        String diachi = cursor.getString(5);
        String sdt = cursor.getString(6);
        byte[] anh = cursor.getBlob(7);

        Bitmap bitmap = BitmapFactory.decodeByteArray(anh, 0, anh.length);
        imgAnhChup.setImageBitmap(bitmap);
        editMaSV.setText(masv);
        editHoTen.setText(hoten);
        if(gioitinh_laynguon.equals("Nam")) {
            rbNam.setChecked(true);
            gioitinh = "Nam";
        } else {
            rbNu.setChecked(true);
            gioitinh = "Nữ";
        }
        editNgaySinh.setText(ngaysinh);
        editDiaChi.setText(diachi);
        editSDT.setText(sdt);
    }


    //chụp hình và chọn hình post lên
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CHOOSE_PHOTO) {
                try {
                    Uri imageUri = data.getData();//đường dẫn tới ảnh
                    InputStream is = getContentResolver().openInputStream(imageUri);//đọc đường dẫn
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imgAnhChup.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgAnhChup.setImageBitmap(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void cancel(){
        Intent intent = new Intent(this, MainActivity_SinhVien.class);
        startActivity(intent);
    }

    private byte [] getByteArrayFromImageView (ImageView imgv) {
        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    private void openAboutMe() {
        Intent intent = new Intent(View_SinhVien.this, AboutMeActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item_login) {
            Intent intent = new Intent(View_SinhVien.this, Activity_DangNhap.class);
            startActivity(intent);
        } else
            openAboutMe();
        return true;
    }
}