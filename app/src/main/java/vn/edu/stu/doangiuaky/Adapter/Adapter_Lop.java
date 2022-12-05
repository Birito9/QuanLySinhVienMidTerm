package vn.edu.stu.doangiuaky.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import vn.edu.stu.doangiuaky.Database.DataBase;
import vn.edu.stu.doangiuaky.Model.Lop;
import vn.edu.stu.doangiuaky.Model.SinhVien;
import vn.edu.stu.doangiuaky.R;
import vn.edu.stu.doangiuaky.Sua_lophoc;

import java.util.ArrayList;

public class Adapter_Lop extends BaseAdapter implements Filterable {
    final String DATABASE_NAME = "btl_qly_sinhvien.sqlite";
    SQLiteDatabase database;

    Activity context;
    ArrayList<Lop> list;
    ArrayList<Lop> listOld;

    ArrayList<SinhVien> dsSinhViens;

    public Adapter_Lop(Activity context, ArrayList<Lop> list) {
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
        // Lấy thông tin từ database vừa lấy bên manin đổ vào text hiển thị lên
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.show_lophoc, null);
        TextView txt_MaLop = (TextView) row.findViewById(R.id.txtMaLop);
        TextView txt_TenNganh = (TextView) row.findViewById(R.id.txtTenNganh);
        TextView txt_TenLop = (TextView) row.findViewById(R.id.txtTenLop);
        TextView txt_GVCN = (TextView) row.findViewById(R.id.txtGVCN);
        Button btnsua = (Button) row.findViewById(R.id.btnSua);
        Button btnxoa = (Button) row.findViewById(R.id.btnXoa);

        Lop mlop = list.get(i);
        txt_MaLop.setText(mlop.malop);
        txt_TenNganh.setText(mlop.tennganh);
        txt_TenLop.setText(mlop.tenlop);
        txt_GVCN.setText(mlop.gvcn);

        dsSinhViens = new ArrayList<>();
        redata();
        //Button Sửa lấy id để sang main sửa để sửa
        btnsua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Sua_lophoc.class);
                intent.putExtra("malop", mlop.malop);
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
                builder.setMessage(R.string.confirm_delete_sub_class);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Boolean checkXoaDuoc = checkSinhVienTrongLop(mlop.malop);
                        if(checkXoaDuoc!=true) {
                            delete(mlop.malop);
                        } else {
                            AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                            builder2.setTitle("ALERT");
                            builder2.setMessage(R.string.no_delete_class);
                            builder2.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int j) {

                                }
                            });
                            AlertDialog dialog = builder2.create();
                            dialog.show();
                        }
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

    private void delete(String idmalop){
        SQLiteDatabase database = DataBase.initDatabase(context, "btl_qly_sinhvien.sqlite");
        database.delete("tbl_lop", "malop = ?",new String[]{idmalop + ""});
        list.clear();
        //xóa xong load lại dữ liệu
        Cursor cursor = database.rawQuery("SELECT * FROM tbl_lop",null);
        while (cursor.moveToNext()){
            String malop = cursor.getString(0);
            String manganh = cursor.getString(1);
            String tenlop = cursor.getString(2);
            String gvcn = cursor.getString(3);

            list.add(new Lop(malop, manganh, tenlop, gvcn));
        }
        notifyDataSetChanged();
    }

    private void redata(){
        database = DataBase.initDatabase(context, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM tbl_sinhvien ",null);
        dsSinhViens.clear();
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
            dsSinhViens.add(new SinhVien(masv, malop, hoten, gioitinh, ngaysinh, diachi, sdt, anh));
        }
        notifyDataSetChanged(); //adapter vẽ lại giao diện
    }
    private Boolean checkSinhVienTrongLop(String malop) {
        for(SinhVien sv : dsSinhViens){
            if (sv.getMalop().equalsIgnoreCase(malop)) {
                return true; //nếu có sinh viên trong lớp thì trả về true
            }
        }
        return false;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if (strSearch.isEmpty()){
                    list = listOld;
                } else {
                    ArrayList<Lop> mlist = new ArrayList<>();
                    for (Lop lop : listOld){
                        // mã của lop chứa các ký tự mà search sẽ add vào list rồi hiển thị
                        if (lop.getMalop().toLowerCase().contains(strSearch.toLowerCase())){
                            mlist.add(lop);
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
                list = (ArrayList<Lop>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
