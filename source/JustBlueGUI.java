import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.universe.*; 
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import javax.swing.SwingUtilities;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import javax.swing.*;
import java.awt.event.*;
import java.awt.EventQueue;
import java.awt.*;
import java.net.URL;
import java.util.*;

public class JustBlueGUI extends JFrame {
    public JustBlueGUI() {
		try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch ( Exception e ) {
            javax.swing.JOptionPane.showMessageDialog(this, "Imposible modificar el tema visual", "Lookandfeel inválido.",
            javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Just Blue");
        this.setMinimumSize(new Dimension(900, 700));
        this.setResizable(false);
	
		java.awt.Dimension pantalla = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        java.awt.Dimension dVentana = this.getSize();
        this.setLocation((pantalla.width - dVentana.width) / 2,(pantalla.height - dVentana.height) / 2);
	
		this.setLayout(new BorderLayout());

		jButtonStart = new JButton("empezar");
		jPanelcontrol = new JPanel();
		jComboBoxDificultad = new JComboBox(new javax.swing.DefaultComboBoxModel(new String[] {"pricipiante", "amateur", "experto"}));

		this.add("Center", paint);
		this.add("South", jPanelcontrol);

		jButtonStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                empezarJuego();
            }
        });
	
		principiante = new JRadioButton("principiante");
		amateur = new JRadioButton("amateur");
		experto = new JRadioButton("experto");
	
		principiante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionPerformedPrincipiante(evt);
            }
        });
	
		amateur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionPerformedAmateur(evt);
            }
        });
	
		experto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actionPerformedExperto(evt);
            }
        });

		jPanelcontrol.add(new JLabel("Nivel de Dificultad : "));
		jPanelcontrol.add(principiante);
		jPanelcontrol.add(amateur);
		jPanelcontrol.add(experto);
		jPanelcontrol.add( "East", jButtonStart);

		this.pack();
		this.setVisible(true);
    }

    public void empezarJuego(){
    	JFrame gameScreen = null;
    	Tablero tablero = null;
    	System.gc();

		if(dificultad != 0){
		    gameScreen = new JFrame("Just Blue");
		    gameScreen.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		    gameScreen.setTitle("Just Blue");
		    gameScreen.setMinimumSize(new Dimension(900, 700));
		    gameScreen.setResizable(false);
		    
		    java.awt.Dimension pantalla = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		    java.awt.Dimension dVentana = gameScreen.getSize();
		    gameScreen.setLocation((pantalla.width - dVentana.width) / 2,(pantalla.height - dVentana.height) / 2);
		    
		    gameScreen.setLayout(new BorderLayout());
		    GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

		    Canvas3D canvas3D = new Canvas3D(config);

		    SimpleUniverse simpleU = new SimpleUniverse(canvas3D);
		    simpleU.getViewingPlatform().setNominalViewingTransform();

		    View view = simpleU.getViewer().getView();
		    view.setTransparencySortingPolicy(View.TRANSPARENCY_SORT_GEOMETRY);

		    ViewingPlatform viewingPlatform = simpleU.getViewingPlatform();
		    OrbitBehavior orbit = new OrbitBehavior(canvas3D, OrbitBehavior.REVERSE_ALL);
		    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
		    orbit.setSchedulingBounds(bounds);
		    viewingPlatform.setViewPlatformBehavior(orbit);

		    tablero = new Tablero(6, 6);
		    tablero.setPosCaballo(new Point((int)(Math.random()*6), (int)(Math.random()*6)));
		    
		    board = new Board(simpleU, canvas3D, tablero, dificultad);
		    simpleU.addBranchGraph(board);
		    
		    gameScreen.add("Center", canvas3D);
		    gameScreen.setVisible(true);

		    experto.setSelected(false);
		    principiante.setSelected(false);
		    amateur.setSelected(false);
		    dificultad = 0;
		}
		else{
		    JOptionPane.showMessageDialog(this, "Selecciona un nivel de dificultad !!!");
		}
    }
    
    public void actionPerformedPrincipiante(java.awt.event.ActionEvent evt){
		dificultad = 2;
		amateur.setSelected(false);
		experto.setSelected(false);
    }
    
    public void actionPerformedAmateur(java.awt.event.ActionEvent evt){
		dificultad = 4;
		experto.setSelected(false);
		principiante.setSelected(false);
    }
    
    public void actionPerformedExperto(java.awt.event.ActionEvent evt){
		dificultad = 11;
		principiante.setSelected(false);
		amateur.setSelected(false);
    }

    public static void main(String arg[]){
		new JustBlueGUI();
    }

    private JLabel prueba;
    private JRadioButton principiante;
    private JRadioButton amateur;
    private JRadioButton experto;
    private JPanel jPanelcontrol;
    private JPanel screen;
    private Board board;
    private JButton jButtonStart;
    private Vector movimientos;
    private JComboBox jComboBoxDificultad;
    private Tablero tablero;
    private int dificultad = 0;
    private PaintPanel paint = new PaintPanel();
}
