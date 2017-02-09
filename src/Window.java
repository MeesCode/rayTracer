
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by meesb on 2/8/2017.
 */
public class Window{

    public void drawImage(BufferedImage bi){
        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(bi)));
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
