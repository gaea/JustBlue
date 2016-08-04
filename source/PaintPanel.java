import javax.swing.*;
import java.awt.*;

public class PaintPanel extends JPanel{

    public PaintPanel(){
	
    }
    
    public void paintComponent(Graphics g){
	Dimension tamano = this.getSize();
        ImageIcon imagen = new ImageIcon("../images/JustBlueImage2.gif");
        g.drawImage(imagen.getImage(), 0, 0, imagen.getIconWidth(), imagen.getIconHeight(), this);
        this.setOpaque(false);
        this.setSize(imagen.getIconWidth(), imagen.getIconHeight());
        this.setSize(imagen.getIconWidth(), imagen.getIconHeight());
        //this.ventana.setTitle(archivo);
        super.paintComponents(g);
    }

}