import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by meesb on 2/9/2017.
 */
public class rayTracer {

    Vertex light = new Vertex(0, -1.5f, 1);
    float fov = 0.002f;

    private Vertex planeIntersect(Face face, Vertex rayOrigin, Vertex rayDirection){
        float distance;
        float D = face.getNormal().dotProduct(face.getVertices().get(0));
        //System.out.println(D);
        float divisor = rayDirection.dotProduct(face.getNormal());

        if(Math.abs(divisor) < 1E-7){
            return null;
        } else {
            distance = (D - rayOrigin.dotProduct(face.getNormal())) / divisor;
            if(distance > 0){
                return null;
            }
            //System.out.println(distance);
            return rayDirection.multiply(distance).add(rayOrigin);
        }
    }

    private shadeInfo triangleIntersect(Face face, Vertex rayOrigin, Vertex rayDirection){
        Vertex cords = planeIntersect(face, rayOrigin, rayDirection);
        if(cords == null){
            return null;
        } else {

            Vertex u = face.getVertices().get(1).subtract(face.getVertices().get(0));
            Vertex v = face.getVertices().get(2).subtract(face.getVertices().get(0));
            Vertex w = cords.subtract(face.getVertices().get(0));

            float r = v.crossProduct(w).getLength() / v.crossProduct(u).getLength();
            float t = u.crossProduct(w).getLength() / u.crossProduct(v).getLength();

            float sr = v.crossProduct(w).dotProduct(v.crossProduct(u));
            float st = u.crossProduct(w).dotProduct(u.crossProduct(v));

            if(sr < 0.0f){
                r = -r;
            }

            if(st < 0.0f){
                t = -t;
            }

            if (r < 0.0f || r > 1.0f || t < 0.0f || r + t > 1.0f) {
                return null;
            }

            Float distance = (float)Math.sqrt(Math.pow(rayOrigin.getX() - cords.getX(), 2) +
                                              Math.pow(rayOrigin.getY() - cords.getY(), 2) +
                                              Math.pow(rayOrigin.getZ() - cords.getZ(), 2));

            Vertex bCords = new Vertex(r, t, 0);

            shadeInfo si = new shadeInfo();
            si.setbCord(bCords);
            si.setDistance(distance);
            si.setFace(face.copy());
            si.setHitpoint(cords);

            return si;
        }

    }

    private Color3D directLight(Vertex rayOrigin, Vertex rayDestination, shadeInfo si){
        Vertex direction = light.subtract(si.getHitpoint());
        direction.normalize();
        float intensity = si.getFace().getNormal().dotProduct(direction);

        if(intensity < 0){
            return new Color3D(0, 0, 0);
        }

        return si.getMaterial().getKd().multiply(intensity);
    }

    private Color3D shade(Vertex rayOrigin, Vertex rayDirection, shadeInfo si){
        si.getFace().setNormal(smoothNormal(si));
        return directLight(rayOrigin, rayDirection, si);
    }

    public Vertex smoothNormal(shadeInfo si){

        Vertex n0 = si.getFace().getVertices().get(0).getVertexNormal();
        Vertex n1 = si.getFace().getVertices().get(1).getVertexNormal();
        Vertex n2 = si.getFace().getVertices().get(2).getVertexNormal();

        n0.normalize();
        n1.normalize();
        n2.normalize();

        Vertex t1 = n0.multiply(1 - si.getbCord().getX() - si.getbCord().getY());
        Vertex t2 = n1.multiply(si.getbCord().getX());
        Vertex t3 = n2.multiply(si.getbCord().getY());

        Vertex result = t1.add(t2).add(t3);
        result.normalize();

        return result;
    }

    private Color3D trace(Vertex rayOrigin, Vertex rayDirection, ArrayList<Object3D> list){
        float minDistance = Float.MAX_VALUE;
        shadeInfo clostest = null;
        for(Object3D o: list){
            for(Face f: o.getFaces()){
                shadeInfo temp = triangleIntersect(f, rayOrigin, rayDirection);
                if(temp != null && temp.getDistance() < minDistance){
                    temp.setMaterial(o.getMaterial());
                    clostest = temp;
                    minDistance = temp.getDistance();
                }
            }
        }
        if(clostest == null) {
            return new Color3D(0, 0, 0);
        } else {
            return shade(rayOrigin, rayDirection, clostest);
        }
    }

    public BufferedImage rayTrace(int width, int height, Vertex cameraOrigin, Vertex cameraDirection, ArrayList<Object3D> list){
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(float z = (height/2)-1; z >= -height/2; z -= 1){
            for(float x = -width/2; x < width/2; x += 1){
                Vertex dest = cameraDirection.subtract(new Vertex(x*fov, 0, -z*fov));
                dest.normalize();
                //System.out.println(dest.toString());
                bi.setRGB((int)(x+width/2), (int)(z+height/2), trace(cameraOrigin, dest, list).getRGB());
            }
        }
        return bi;
    }

    public void rayTraceTest(ArrayList<Object3D> list){
        System.out.println(trace(Vertex.NULL, new Vertex(0,0,1), list).toString());
    }
}
