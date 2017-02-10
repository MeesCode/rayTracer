/**
 * Created by meesb on 2/8/2017.
 */
public class Vertex {
    private float x, y, z;
    public static Vertex NULL = new Vertex(0, 0, 0);
    Vertex vertexNormal = Vertex.NULL;

    public Vertex(float x, float y, float z) {

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex(Vertex v){
        this.x = v.getX();
        this.y = v.getY();
        this.z = v.getZ();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setVertexNormal(Vertex normal){
        vertexNormal = normal;
    }

    public Vertex getVertexNormal(){
        return vertexNormal;
    }

    public String toString(){
        return "x: " + x + ", y: " + y + ", z: " + z;
    }

    public Vertex subtract(Vertex that){
        return new Vertex(x - that.getX(), y - that.getY(), z - that.getZ());
    }

    public Vertex add(Vertex that){
        return new Vertex(x + that.getX(), y + that.getY(), z + that.getZ());
    }

    public Vertex add(float that){
        return new Vertex(x + that, y + that, z + that);
    }

    public float dotProduct(Vertex that){
        return (x * that.getX()) + (y * that.getY()) + (z * that.getZ());
    }

    public Vertex crossProduct(Vertex that){
        return new Vertex(((y * that.getZ()) - (z * that.getY())),
                          ((z * that.getX()) - (x * that.getZ())),
                          ((x * that.getY()) - (y * that.getX())));
    }

    public float getLength(){
        return (float) Math.sqrt(x*x + y*y + z*z);
    }

    public void normalize(){
        float mag = this.getLength();
        if(mag > 0) {
            x = x / mag;
            y = y / mag;
            z = z / mag;
        }
    }

    public Vertex divide(float f){
        return new Vertex(x/f, y/f, z/f);
    }

    public Vertex multiply(float f){
        return new Vertex(x*f, y*f, z*f);
    }

}
