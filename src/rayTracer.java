import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by meesb on 2/9/2017.
 */
public class rayTracer {

    ArrayList<Object3D> list = new ArrayList<>();
    float fov = 0.002f;
    float threshold = 0.00004f;
    float ambientLight = 0.1f;
    ArrayList<Light> lights = new ArrayList<>();
    int level = 3;

    Vertex cameraOrigin;
    Vertex cameraDirection;

    private Vertex reflectRay(Vertex direction, Vertex normal){
        Vertex reflection = direction.subtract(normal.multiply(2).multiply(direction.dotProduct(normal)));
        reflection.normalize();
        return reflection;
    }

    private Vertex refractRay(Vertex direction, shadeInfo si){

        Vertex normal = si.getFace().getNormal();
        float eta;
        if(direction.dotProduct(normal) <= 0){
            eta = 1/1.6f;
        } else {
            eta = 1.6f;
            normal = normal.multiply(-1);
        }

        float c1 = -direction.dotProduct(normal);
        float cs2 = 1 - eta * eta * (1 - c1 * c1);

        if(cs2 < 0){
             return reflectRay(direction, normal);
        }

        Vertex a = direction.multiply(eta);
        Vertex b = normal.multiply((float)(eta * c1 - Math.sqrt(cs2)));
        Vertex c = a.add(b);
        c.normalize();

        return c;
    }

    private Vertex planeIntersect(Face face, Vertex rayOrigin, Vertex rayDirection){
        float distance;
        float D = face.getNormal().dotProduct(face.getVertices().get(0));
        float divisor = rayDirection.dotProduct(face.getNormal());

        distance = (D - rayOrigin.dotProduct(face.getNormal())) / divisor;
        if(distance < 0){
            return null;
        }
        return rayDirection.multiply(distance).add(rayOrigin);
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

            Float distance = rayOrigin.distance(cords);

            Vertex bCords = new Vertex(r, t, 0);

            shadeInfo si = new shadeInfo();
            si.setbCord(bCords);
            si.setDistance(distance);
            si.setFace(face.copy());
            si.setHitpoint(cords);

            return si;
        }

    }

    private Color3D directLight(Vertex rayOrigin, Vertex rayDestination, shadeInfo si, Light light){

        Vertex direction = light.getPosition().subtract(si.getHitpoint());
        direction.normalize();
        float intensity = si.getFace().getNormal().dotProduct(direction);

        if(intensity < 0){
            return new Color3D(0, 0, 0);
        }

        return si.getMaterial().getKd().multiply(intensity).multiply(light.getIntensity());
    }

    private Color3D specularLight(Vertex rayOrigin, Vertex rayDestination, shadeInfo si, Light light){
        Vertex direction = light.getPosition().subtract(si.getHitpoint());
        direction.normalize();

        Vertex viewDir = cameraOrigin.subtract(si.getHitpoint());
        Vertex H = direction.add(viewDir);
        H.normalize();

        float NdotH = H.dotProduct(si.getFace().getNormal());

        if (NdotH < 0) {
            return Color3D.black;
        }

        float intensity = (float)Math.pow(NdotH, si.getMaterial().getNs());
        return si.getMaterial().getKs().multiply(intensity).multiply(light.getIntensity());
    }

    public boolean isShadow(Vertex hitPoint, Light light){
        float minDistance = Float.MAX_VALUE;
        Vertex direction = new Vertex(hitPoint).subtract(light.getPosition());
        direction.normalize();

        float distanceExpected = light.getPosition().distance(hitPoint);

        for(Object3D o: list){
            for(Face f: o.getFaces()){
                shadeInfo temp = triangleIntersect(f, light.getPosition(), direction);
                if(temp != null && temp.getDistance() < minDistance){
                    minDistance = temp.getDistance();
                }
            }
        }
        return Math.abs(distanceExpected - minDistance) > threshold;
    }

    private Color3D shade(int level, Vertex rayOrigin, Vertex rayDirection, shadeInfo si){

        Color3D total = Color3D.black;

        for(Light light: lights) {

            si.getFace().setNormal(smoothNormal(si));
            if (isShadow(si.getHitpoint(), light))
                continue;

            Color3D directLight = directLight(rayOrigin, rayDirection, si, light);
            Color3D specularLight = specularLight(rayOrigin, rayDirection, si, light);
            Color3D reflectedLight = Color3D.black;
            Color3D refractedLight = Color3D.black;

            if(si.getMaterial().getIllum() == 3 || si.getMaterial().getIllum() == 4){
                Vertex reflectedRay = reflectRay(rayDirection, si.getFace().getNormal());
                reflectedLight = trace(level - 1, si.getHitpoint().add(reflectedRay.multiply(threshold)), reflectedRay);
                float mirror = si.getMaterial().getNs()/1000;
                reflectedLight = reflectedLight.multiply(mirror);
                directLight = directLight.multiply(1 - mirror);
            }

            if(si.getMaterial().getIllum() == 4 || si.getMaterial().getIllum() == 5){
                Vertex refractedRay = refractRay(rayDirection, si);
                refractedLight = trace(level - 1, si.getHitpoint().add(refractedRay.multiply(threshold)), refractedRay);
                float opacity = si.getMaterial().getD();
                refractedLight = refractedLight.multiply(1 - opacity);
                directLight = directLight.multiply(opacity);
            }

            total = total.add(directLight.add(specularLight).add(reflectedLight).add(refractedLight));
        }

        return total;
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

    private Color3D trace(int level, Vertex rayOrigin, Vertex rayDirection){
        if(level == 0){
            return Color3D.black;
        }

        float minDistance = Float.MAX_VALUE;
        shadeInfo closest = null;
        for(Object3D o: list){
            for(Face f: o.getFaces()){
                shadeInfo temp = triangleIntersect(f, rayOrigin, rayDirection);
                if(temp != null && temp.getDistance() < minDistance){
                    temp.setMaterial(o.getMaterial());
                    closest = temp;
                    minDistance = temp.getDistance();
                }
            }
        }
        if(closest == null) {
            return Color3D.black;
        } else {
            return shade(level, rayOrigin, rayDirection, closest);
        }
    }

    public BufferedImage rayTrace(int width, int height, Vertex cameraOrigin, Vertex cameraDirection, ArrayList<Object3D> list, ArrayList<Light> lights){
        this.cameraOrigin = cameraOrigin;
        this.cameraDirection = cameraDirection;
        this.list = list;
        this.lights = lights;

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for(float z = (height/2)-1; z >= -height/2; z -= 1){
            for(float x = -width/2; x < width/2; x += 1){
                Vertex dest = cameraDirection.subtract(new Vertex(-x*fov, 0, z*fov));
                dest.normalize();
                bi.setRGB((int)(x+width/2), (int)(z+height/2), trace(level, cameraOrigin, dest).getRGB());
            }
            if(z%10 == 0){
                System.out.println((int)(height/2 - z) + "/" + height);
            }
        }
        return bi;
    }
}
