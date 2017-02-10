import java.awt.*;

/**
 * Created by meesb on 2/10/2017.
 */
public class Color3D extends Color {

    public static Color3D black = new Color3D(0, 0, 0);
    public static Color3D white = new Color3D(255, 255, 255);

    public Color3D(int r, int g, int b){
        super(r, g, b);
    }

    public Color3D multiply(Color3D that){

        int red = this.getRed() * that.getRed();
        int green = this.getGreen() * that.getGreen();
        int blue = this.getBlue() * that.getBlue();

        red = Math.min(red, 255);
        green = Math.min(green, 255);
        blue = Math.min(blue, 255);

        return new Color3D(red, green, blue);
    }

    public Color3D multiply(float that){
        int red = (int)(this.getRed() * that);
        int green = (int)(this.getGreen() * that);
        int blue = (int)(this.getBlue() * that);

        red = Math.min(red, 255);
        green = Math.min(green, 255);
        blue = Math.min(blue, 255);

        return new Color3D(red, green, blue);
    }

    public Color3D minus(Color3D that){
        return new Color3D(this.getRed() - that.getRed(),
                this.getGreen() - that.getGreen(),
                this.getBlue() - that.getBlue());
    }

    public Color3D add(Color3D that) {
        int red = this.getRed() + that.getRed();
        int green = this.getGreen() + that.getGreen();
        int blue = this.getBlue() + that.getBlue();

        red = Math.min(red, 255);
        green = Math.min(green, 255);
        blue = Math.min(blue, 255);

        return new Color3D(red, green, blue);
    }
}
