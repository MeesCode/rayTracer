import java.awt.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by meesb on 2/8/2017.
 */
public class createObject3D {

    private static ArrayList<Object3D> objects = new ArrayList<>();
    private static ArrayList<Material> materials = new ArrayList<>();

    public static ArrayList<Object3D> read(File obj, File mtl){
        try{
            readMaterials(mtl);
            readObjects(obj);
        }catch(IOException e){
            e.printStackTrace();
        }

        return objects;
    }

    private static void readObjects(File f) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(f));

        String line = br.readLine();


        int indexOffset = 0;
        int tempIndex = 0;

        int indexOffsetNormal = 0;
        int tempIndexNormal = 0;

        while(line != null){
            String[] parts = line.split(" ");

            if(parts[0].equals("o")){
                objects.add(new Object3D(parts[1]));
                indexOffset += tempIndex;
                indexOffsetNormal += tempIndexNormal;
                tempIndex = 0;
                tempIndexNormal = 0;
            }

            if(parts[0].equals("v")){
                objects.get(objects.size()-1).addVertex(new Vertex(Float.parseFloat(parts[1]), Float.parseFloat(parts[3]), Float.parseFloat(parts[2])));
                tempIndex++;
            }

            if(parts[0].equals("vn")){
                Vertex normal = new Vertex(Float.parseFloat(parts[1]), Float.parseFloat(parts[3]), Float.parseFloat(parts[2]));
                normal.normalize();
                objects.get(objects.size()-1).addNormal(normal);
                tempIndexNormal++;
            }

            if(parts[0].equals("usemtl")){
                for(Material m: materials){
                    if(m.getName().equals(parts[1])){
                        objects.get(objects.size()-1).setMaterial(m);
                    }
                }
            }

            if(parts[0].equals("f")){
                ArrayList<Vertex> vertices = new ArrayList<>();
                int index = Integer.parseInt(parts[1].split("//")[1]) - 1 - indexOffsetNormal;
                for(int i = 1; i < parts.length; i++){
                    vertices.add(objects.get(objects.size()-1).getVertices().get(Integer.parseInt(parts[i].split("//")[0]) - 1 - indexOffset));
                }
                objects.get(objects.size()-1).addFace(new Face(vertices, objects.get(objects.size()-1).getNormals().get(index)));
            }

            line = br.readLine();
        }
        br.close();
    }

    private static void readMaterials(File f) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(f));

        String line = br.readLine();

        while(line != null){
            String[] parts = line.split(" ");

            if(parts[0].equals("newmtl")){
                materials.add(new Material(parts[1]));
            }

            if(parts[0].equals("Ns")){
                materials.get(materials.size()-1).setNs(Float.parseFloat(parts[1]));
            }

            if(parts[0].equals("Ka")){
                materials.get(materials.size()-1).setKa(new Color((int)(Float.parseFloat(parts[1])*255),
                                                                  (int)(Float.parseFloat(parts[2])*255),
                                                                  (int)(Float.parseFloat(parts[3])*255)));
            }

            if(parts[0].equals("Kd")){
                materials.get(materials.size()-1).setKd(new Color((int)(Float.parseFloat(parts[1])*255),
                        (int)(Float.parseFloat(parts[2])*255),
                        (int)(Float.parseFloat(parts[3])*255)));
            }

            if(parts[0].equals("Ks")){
                materials.get(materials.size()-1).setKs(new Color((int)(Float.parseFloat(parts[1])*255),
                        (int)(Float.parseFloat(parts[2])*255),
                        (int)(Float.parseFloat(parts[3])*255)));
            }

            if(parts[0].equals("Ke")){
                materials.get(materials.size()-1).setKe(new Color((int)(Float.parseFloat(parts[1])*255),
                        (int)(Float.parseFloat(parts[2])*255),
                        (int)(Float.parseFloat(parts[3])*255)));
            }

            if(parts[0].equals("Ni")){
                materials.get(materials.size()-1).setNi(Float.parseFloat(parts[1]));
            }

            if(parts[0].equals("d")){
                materials.get(materials.size()-1).setD(Float.parseFloat(parts[1]));
            }

            if(parts[0].equals("illum")){
                materials.get(materials.size()-1).setIllum(Integer.parseInt(parts[1]));
            }

            line = br.readLine();
        }
        br.close();
    }
}
