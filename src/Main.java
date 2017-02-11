
import java.io.File;
import java.util.ArrayList;

/**
 * Created by meesb on 2/8/2017.
 */
public class Main {

    static ArrayList<Object3D> list = new ArrayList<>();

    public static void main(String[] args){

        list = createObject3D.read(new File("cornell.obj"), new File("cornell.mtl"));

        Window w = new Window();
        rayTracer r = new rayTracer();

        ArrayList<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vertex(0.8f, 1.2f, 1.8f), Color3D.white, .5f));
        lights.add(new Light(new Vertex(-0.8f, 1.2f, 1.8f), Color3D.white, .5f));


        Vertex cameraOrigin = new Vertex(0, -1.05f, 1);
        Vertex cameraDirection = new Vertex(0, 1, 0);
        cameraDirection.normalize();

        w.drawImage(r.rayTrace(500, 500, cameraOrigin, cameraDirection, list, lights));

    }


}

