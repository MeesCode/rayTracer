
import java.io.File;
import java.util.ArrayList;

/**
 * Created by meesb on 2/8/2017.
 */
public class Main {

    static ArrayList<Object3D> list = new ArrayList<>();

    public static void main(String[] args){

        list = createObject3D.read(new File("cube.obj"), new File("cube.mtl"));

        /*for(Object3D o: list){
            System.out.println(o.getName());
            for(Face f: o.getFaces()){
                System.out.println("\tface:");
                System.out.println("\t\tnormal: " + f.getNormal());
                for(Vertex v: f.getVertices()){
                    System.out.println("\t\t" + v.toString());
                }
            }
        }*/

        Window w = new Window();
        rayTracer r = new rayTracer();

        w.drawImage(r.rayTrace(500, 500, list));

        //r.rayTraceTest(list);

    }


}

