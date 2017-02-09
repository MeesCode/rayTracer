import java.io.*;
import java.util.ArrayList;

/**
 * Created by meesb on 2/8/2017.
 */
public class createObject3D {

    public static ArrayList<Object3D> read(File f) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(f));

        String line = br.readLine();
        ArrayList<Object3D> list = new ArrayList<>();

        int indexOffset = 0;
        int tempIndex = 0;

        int indexOffsetNormal = 0;
        int tempIndexNormal = 0;

        while(line != null){
            String[] parts = line.split(" ");

            if(parts[0].equals("o")){
                list.add(new Object3D(parts[1]));
                indexOffset += tempIndex;
                indexOffsetNormal += tempIndexNormal;
                tempIndex = 0;
                tempIndexNormal = 0;
            }

            if(parts[0].equals("v")){
                list.get(list.size()-1).addVertex(new Vertex(Float.parseFloat(parts[1]), Float.parseFloat(parts[3]), Float.parseFloat(parts[2])));
                tempIndex++;
            }

            if(parts[0].equals("vn")){
                Vertex normal = new Vertex(Float.parseFloat(parts[1]), Float.parseFloat(parts[3]), Float.parseFloat(parts[2]));
                normal.normalize();
                list.get(list.size()-1).addNormal(normal);
                tempIndexNormal++;
            }

            if(parts[0].equals("usemtl")){
                list.get(list.size()-1).setMaterial(parts[1]);
            }

            if(parts[0].equals("f")){
                ArrayList<Vertex> vertices = new ArrayList<>();
                int index = Integer.parseInt(parts[1].split("//")[1]) - 1 - indexOffsetNormal;
                for(int i = 1; i < parts.length; i++){
                    vertices.add(list.get(list.size()-1).getVertices().get(Integer.parseInt(parts[i].split("//")[0]) - 1 - indexOffset));
                }
                list.get(list.size()-1).addFace(new Face(vertices, list.get(list.size()-1).getNormals().get(index)));
            }

            line = br.readLine();
        }
        return list;
    }
}
