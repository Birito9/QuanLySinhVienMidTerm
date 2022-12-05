package vn.edu.stu.doangiuaky.Model;

public class Lop {
    public String malop;
    public String tennganh;
    public String tenlop;
    public String gvcn;

    public Lop(String malop, String tennganh, String tenlop, String gvcn) {
        this.malop = malop;
        this.tennganh = tennganh;
        this.tenlop = tenlop;
        this.gvcn = gvcn;
    }

    public Lop(String malop, String tenlop) {
    }

    public String getMalop() {
        return malop;
    }

    public void setMalop(String malop) {
        this.malop = malop;
    }

    public String getTennganh() {
        return tennganh;
    }

    public void setTennganh(String tennganh) {
        this.tennganh = tennganh;
    }

    public String getTenlop() {
        return tenlop;
    }

    public void setTenlop(String tenlop) {
        this.tenlop = tenlop;
    }

    public String getGvcn() {
        return gvcn;
    }

    public void setGvcn(String gvcn) {
        this.gvcn = gvcn;
    }

    @Override
    public String toString() {
        return malop + " - " + tenlop;
    }
}
