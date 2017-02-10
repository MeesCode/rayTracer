import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by meesb on 2/9/2017.
 */
public class rayTracer {

    Vertex light = new Vertex(2.1f, -6.0f, 2.8f);

    private Vertex planeIntersect(Face face, Vertex rayOrigin, Vertex rayDirection){
        float distance;
        float D = face.getNormal().dotProduct(face.getVertices().get(0));
        //System.out.println(D);
        float divisor = rayDirection.dotProduct(face.getNormal());

        if(Math.abs(divisor) < 1E-7){
            return null;
        } else {
            distance = (D - rayOrigin.dotProduct(face.getNormal())) / divisor;
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
            si.setFace(face);
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
        return directLight(rayOrigin, rayDirection, si);
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

    public BufferedImage rayTrace(int width, int height, ArrayList<Object3D> list){
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(float y = 0; y < height; y += 1){
            for(float x = 0; x < width; x += 1){
                Vertex dest = new Vertex((float)(((x)/500)-0.5), (float)(((y)/500)-0.5), 1);
                dest.normalize();
                //System.out.println(dest.toString());
                bi.setRGB((int)x, (int)y, trace(Vertex.NULL, dest, list).getRGB());
            }
        }
        return bi;
    }

    public void rayTraceTest(ArrayList<Object3D> list){
        System.out.println(trace(Vertex.NULL, new Vertex(0,0,1), list).toString());
    }
}
