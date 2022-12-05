package vn.edu.stu.doangiuaky.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import vn.edu.stu.doangiuaky.Database.DataBase;
import vn.edu.stu.doangiuaky.Model.SinhVien;
import vn.edu.stu.doangiuaky.R;
import vn.edu.stu.doangiuaky.Sua_SinhVien;
import vn.edu.stu.doangiuaky.View_SinhVien;

import java.util.ArrayList;

public class Adapter_SinhVien extends BaseAdapter implements Filterable {

    Activity context;
    ArrayList<SinhVien> list;
    ArrayList<SinhVien> listOld;

    public Adapter_SinhVien(Activity context, ArrayList<SinhVien> list) {
        this.context = context;
        this.list = list;
        this.listOld = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.show_sinhvien, null);
        ImageView imgHinhSV = (ImageView) row.findViewById(R.id.imgHinhSinhvien);
        TextView txtMaSV = (TextView) row.findViewById(R.id.txt_MaSV);
        TextView txtMalop = (TextView) row.findViewById(R.id.txt_MaLop);
        TextView txtHoTen = (TextView) row.findViewById(R.id.txt_HoTen);
        TextView txtGioiTinh = (TextView) row.findViewById(R.id.txt_GioiTinh);
        TextView txtNgaySinh = (TextView) row.findViewById(R.id.txt_NgaySinh);
        TextView txtDiaChi = (TextView) row.findViewById(R.id.txt_DiaChi);
        TextView txtSDT = (TextView) row.findViewById(R.id.txt_SDT);
        ImageView btnsua = (ImageView) row.findViewById(R.id.btnSua);
        ImageView btnxoa = (ImageView) row.findViewById(R.id.btnXoa);
        ImageView btnxem = (ImageView) row.findViewById(R.id.btnXem);

        SinhVien sinhVien = list.get(i);
        txtMaSV.setText(sinhVien.masv);
        txtMalop.setText(sinhVien.malop);
        txtHoTen.setText(sinhVien.hoten);
        txtGioiTinh.setText(sinhVien.gioitinh);
        txtNgaySinh.setText(sinhVien.ngaysinh);
        txtDiaChi.setText(sinhVien.diachi);
        txtSDT.setText(sinhVien.sdt);

        Bitmap bmHinhSV = BitmapFactory.decodeByteArray(sinhVien.anh,0,sinhVien.anh.length);
        imgHinhSV.setImageBitmap(bmHinhSV);

        btnxem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, View_SinhVien.class);
                intent.putExtra("masv", sinhVien.masv);
                context.startActivity(intent);
            }
        });
        //Button Sửa lấy id để sang main sửa để sửa
        btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Sua_SinhVien.class);
                intent.putExtra("masv", sinhVien.masv);
                context.startActivity(intent);
            }
        });

        //Button Xóa (thực hiện nút xóa)
        btnxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle(R.string.confirm_delete);
                builder.setMessage(R.string.confirm_delete_sub);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(sinhVien.masv);
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return row;
    }

    private void delete(String maSV){
        SQLiteDatabase database = DataBase.initDatabase(context, "btl_qly_sinhvien.sqlite");
        database.delete("tbl_sinhvien", "masv = ?",new String[]{maSV + ""});
        list.clear();

        Cursor cursor = database.rawQuery("SELECT * FROM tbl_sinhvien",null);
        while (cursor.moveToNext()){
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
        notifyDataSetChanged();
    }

    //tìm kiếm
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if (strSearch.isEmpty()){
                    list = listOld;
                } else {
                    ArrayList<SinhVien> mlist = new ArrayList<>();
                    for (SinhVien sinhVien : listOld){
                        // tên của sách chứa các ký tự mà search sẽ add vào list rồi hiển thị
                        if (sinhVien.getMasv().toLowerCase().contains(strSearch.toLowerCase())){
                            mlist.add(sinhVien);
                        }
                    }
                    list = mlist;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<SinhVien>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
