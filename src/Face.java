import java.util.ArrayList;

/**
 * Created by meesb on 2/8/2017.
 */
public class Face {
    ArrayList<Vertex> vertices;
    Vertex normal;

    public Face(ArrayList<Vertex> vertices, Vertex normal){
        this.vertices = vertices;
        this.normal = normal;
    }

    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public Vertex getNormal() {
        return normal;
    }

    public Face copy(){
        return new Face(new ArrayList<>(vertices), new Vertex(normal.getX(), normal. getY(), normal.getZ()));
    }

    public void setNormal(Vertex normal) {
        this.normal = normal;
    }
}
