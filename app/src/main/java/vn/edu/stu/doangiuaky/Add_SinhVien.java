package vn.edu.stu.doangiuaky;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import vn.edu.stu.doangiuaky.Database.DataBase;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class Add_SinhVien extends AppCompatActivity {
    final String DATABASE_NAME = "btl_qly_sinhvien.sqlite";
    SQLiteDatabase database;//hằng số
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;
    private String gioitinh = "Nam";


    Button btnChupH, btnChonH, btnLuu, btnHuy;
    EditText editMaSV, editHoTen, editNgaySinh, editDiaChi, editSDT;
    ImageView imgAnhChup;
    Spinner spinnerMaLop;
    RadioGroup rgGioiTinh;
    Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sinh_vien);

        addControls();
        addEvents();

        Spinner_MaLop();
        rgGioiTinh.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.rbNam) {
                    gioitinh = "Nam";
                } else {
                    gioitinh = "Nữ";
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

    // even sự kiện click---------------------------------------------------------------
    private void addEvents(){
        btnChupH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
        btnChonH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChoosePhoto();
            }
        });
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

    //ÁNH XẠ
    private void addControls() {
        btnChonH = (Button) findViewById(R.id.btnChonH);
        btnChupH = (Button) findViewById(R.id.btnChupH);
        btnLuu = (Button) findViewById(R.id.btnLuu);
        btnHuy = (Button) findViewById(R.id.btnHuy);

        editMaSV = (EditText) findViewById(R.id.edt_t_MaSV);
        spinnerMaLop = (Spinner) findViewById(R.id.spinner_t_Malop);
        editHoTen = (EditText) findViewById(R.id.edt_t_HoTen);
        rgGioiTinh = (RadioGroup) findViewById(R.id.rgGioiTinh_t);
        editNgaySinh = (EditText) findViewById(R.id.edt_t_NgaySinh);
        editDiaChi = (EditText) findViewById(R.id.edt_t_DiaChi);
        editSDT = (EditText) findViewById(R.id.edt_t_SDT);

        imgAnhChup =findViewById(R.id.img_AnhChup);
    }

    //chụp ảnh
    private void takePicture(){
        Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }
    //chọn ảnh
    private void ChoosePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
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

    //thêm -------------------------------------
    private void insert(){
        String masv = editMaSV.getText().toString();
        String spin_malop = spinnerMaLop.getSelectedItem().toString();
        String hoten = editHoTen.getText().toString();
        String ngaysinh = editNgaySinh.getText().toString();
        String diachi = editDiaChi.getText().toString();
        String sdt = editSDT.getText().toString();

        byte[] anh = getByteArrayFromImageView(imgAnhChup);

        if(masv.equals("") || hoten.equals("") || ngaysinh.equals("") || diachi.equals("") || sdt.equals("")){
            Toast.makeText(this, R.string.empty_info, Toast.LENGTH_SHORT).show();
            return;
        } else {
            database = DataBase.initDatabase(this, DATABASE_NAME);
            Cursor cursor = database.rawQuery("SELECT * FROM tbl_sinhvien WHERE masv =? ", new String[]{masv+""});
            if (cursor.moveToNext()){
                Toast.makeText(this, R.string.exists_student_id, Toast.LENGTH_SHORT).show();
                return;
            } else {
                ContentValues contentValues = new ContentValues();//chèn 1 hàng mới vào bảng trong csdl
                contentValues.put("masv", masv);
                contentValues.put("malop", spin_malop);
                contentValues.put("hoten", hoten);
                contentValues.put("gioitinh", gioitinh);
                contentValues.put("ngaysinh", ngaysinh);
                contentValues.put("diachi", diachi);
                contentValues.put("sdt", sdt);

                contentValues.put("anh", anh);

                SQLiteDatabase database = DataBase.initDatabase(this, "btl_qly_sinhvien.sqlite");
                database.insert("tbl_sinhvien", null, contentValues);
                Toast.makeText(this, R.string.add_new_student_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity_SinhVien.class);
                startActivity(intent);
            }
        }
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
}