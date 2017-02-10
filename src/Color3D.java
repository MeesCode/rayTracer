import java.awt.*;

/**
 * Created by meesb on 2/10/2017.
 */
public class Color3D extends Color {

    public Color3D(int r, int g, int b){
        super(r, g, b);
    }

    public Color3D multiply(Color3D that){
        return new Color3D(this.getRed() * that.getRed(),
                           this.getGreen() * that.getGreen(),
                           this.getBlue() * that.getBlue());
    }

    public Color3D multiply(float that){
        return new Color3D((int)(this.getRed() * that),
                (int)(this.getGreen() * that),
                (int)(this.getBlue() * that));
    }

    public Color3D minus(Color3D that){
        return new Color3D(this.getRed() - that.getRed(),
                this.getGreen() - that.getGreen(),
                this.getBlue() - that.getBlue());
    }

    public Color3D plus(Color3D that){
        return new Color3D(this.getRed() + that.getRed(),
                this.getGreen() + that.getGreen(),
                this.getBlue() + that.getBlue());
    }
}
