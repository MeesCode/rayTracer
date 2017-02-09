import java.util.ArrayList;

/**
 * Created by meesb on 2/8/2017.
 */
public class Object3D {
    String name;
    String material;
    ArrayList<Vertex> vertices = new ArrayList<>();
    ArrayList<Vertex> normals = new ArrayList<>();
    ArrayList<Face> faces = new ArrayList<>();

    public Object3D(String name) {
        this.name = name;
    }

    public Object3D(String name, String material, ArrayList<Vertex> vertices, ArrayList<Vertex> normals){
        this.name = name;
        this.material = material;
        this.vertices = vertices;
        this.normals = normals;
    }

    public void addFace(int index, ArrayList<Integer> faceIndices){
        ArrayList<Vertex> faceVertices = new ArrayList<>();
        for(Integer i: faceIndices){
            faceVertices.add(vertices.get(i));
        }
        faces.add(new Face(faceVertices, normals.get(index)));
    }

    public String getName() {
        return name;
    }

    public String getMaterial() {
        return material;
    }

    public ArrayList<Face> getFaces() {
        return faces;
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public ArrayList<Vertex> getNormals() {
        return normals;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void addVertex(Vertex v){
        vertices.add(v);
    }

    public void addNormal(Vertex v){
        normals.add(v);
    }

    public void addFace(Face f){
        faces.add(f);
    }
}
