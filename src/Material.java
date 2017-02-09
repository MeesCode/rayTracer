import java.awt.*;

/**
 * Created by meesb on 2/9/2017.
 */
public class Material {
    private String name;
    private float Ns;
    private Color Ka;
    private Color Kd;
    private Color Ks;
    private Color Ke;
    private float Ni;
    private float d;
    private int illum;

    public Material(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getNs() {
        return Ns;
    }

    public void setNs(float ns) {
        Ns = ns;
    }

    public Color getKa() {
        return Ka;
    }

    public void setKa(Color ka) {
        Ka = ka;
    }

    public Color getKd() {
        return Kd;
    }

    public void setKd(Color kd) {
        Kd = kd;
    }

    public Color getKs() {
        return Ks;
    }

    public void setKs(Color ks) {
        Ks = ks;
    }

    public Color getKe() {
        return Ke;
    }

    public void setKe(Color ke) {
        Ke = ke;
    }

    public float getNi() {
        return Ni;
    }

    public void setNi(float ni) {
        Ni = ni;
    }

    public float getD() {
        return d;
    }

    public void setD(float d) {
        this.d = d;
    }

    public int getIllum() {
        return illum;
    }

    public void setIllum(int illum) {
        this.illum = illum;
    }

}
