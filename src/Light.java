/**
 * Created by meesb on 2/10/2017.
 */
public class Light {
    private Vertex position;
    private Color3D color;
    private float intensity;

    public Light(Vertex position, Color3D color, float intensity) {
        this.position = position;
        this.color = color;
        this.intensity = intensity;
    }

    public Vertex getPosition() {
        return position;
    }

    public void setPosition(Vertex position) {
        this.position = position;
    }

    public Color3D getColor() {
        return color;
    }

    public void setColor(Color3D color) {
        this.color = color;
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}
