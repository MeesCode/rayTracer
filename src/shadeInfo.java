/**
 * Created by meesb on 2/10/2017.
 */
public class shadeInfo {
    private Face face;
    private Material material;
    private Vertex hitpoint;
    private Vertex bCord;
    private float distance;

    public Face getFace() {
        return face;
    }

    public void setFace(Face face) {
        this.face = face;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Vertex getHitpoint() {
        return hitpoint;
    }

    public void setHitpoint(Vertex hitpoint) {
        this.hitpoint = hitpoint;
    }

    public Vertex getbCord() {
        return bCord;
    }

    public void setbCord(Vertex bCord) {
        this.bCord = bCord;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
