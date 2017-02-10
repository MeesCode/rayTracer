
import java.io.File;
import java.util.ArrayList;

/**
 * Created by meesb on 2/8/2017.
 */
public class Main {

    static ArrayList<Object3D> list = new ArrayList<>();

    public static void main(String[] args){

        list = createObject3D.read(new File("cube.obj"), new File("cube.mtl"));

        Window w = new Window();
        rayTracer r = new rayTracer();

        Vertex cameraOrigin = new Vertex(0, -1, 3);
        Vertex cameraDirection = new Vertex(-.1f, 1, -.3f);
        cameraDirection.normalize();

        w.drawImage(r.rayTrace(500, 500, cameraOrigin, cameraDirection, list));

    }


}

