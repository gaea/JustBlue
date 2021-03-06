import com.sun.j3d.utils.universe.*; 
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.behaviors.keyboard.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.geometry.*;

import com.sun.j3d.utils.picking.*;
import ncsa.j3d.loaders.ModelLoader;

import javax.media.j3d.TextureUnitState;

import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;

import java.io.*;
import com.sun.j3d.utils.image.TextureLoader;
import javax.media.j3d.Behavior.*;
import javax.media.j3d.WakeupOnAWTEvent;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.util.*;
import java.util.Enumeration;
import javax.media.j3d.BoundingBox;
import com.sun.j3d.utils.image.TextureLoader;

import java.awt.event.*;
import java.net.*;

public class Board extends BranchGroup {
    private TransformGroup vpTrans;
    private BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
    private KeyNavigatorBehavior keyNavCamara;
    private TransformGroup caballo;
    private Tablero tablero;
    private PickCanvas pickCanvas;
    private Appearance apparWhite = new Appearance();
    private Appearance apparBlue = new Appearance();
    private Appearance apparWhiteShadow = new Appearance();
    private Appearance apparBlueShadow = new Appearance();
    private int dificultad = 2;
    private Box [][] floor = new Box[6][6];

    public Board(SimpleUniverse simpleU, Canvas3D canvas3D, final Tablero tablero, int dificultad){

	this.dificultad = dificultad;
	this.vpTrans = simpleU.getViewingPlatform().getViewPlatformTransform();
	this.tablero = tablero;
	loadAppearances();
	addCamera();
	addLight();
	addBackground();
	addMaze();
	addHorse();

	this.pickCanvas = new PickCanvas(canvas3D, this);

	canvas3D.addMouseListener(new MouseAdapter(){
	    public void mouseClicked(MouseEvent e) {
			machineResponce(e);
	    }
	});
    
	this.pickCanvas.setMode(PickTool.GEOMETRY_INTERSECT_INFO); 
        this.pickCanvas.setTolerance(0.0f);
		compile();
    }

    public void machineResponce(MouseEvent e){
		pickCanvas.setShapeLocation(e);
		PickResult result = pickCanvas.pickClosest();
		boolean gana = false;
		if (result == null) {
		    System.out.println("Nothing picked");
		} 
		else {
		    Primitive p = (Primitive)result.getNode(PickResult.PRIMITIVE);
		    
		    if (p != null) {
				String pos [] = ((String)(p.getUserData())).split(",");
				if(tablero.validarPosicion(new Point(Integer.parseInt(pos[0]), Integer.parseInt(pos[1])))){
				    tablero.setPosCaballo(new Point(Integer.parseInt(pos[0]), Integer.parseInt(pos[1])));
				    
				    if((Integer.parseInt(pos[0])+Integer.parseInt(pos[1])) % 2 == 0){
				    	p.setAppearance(apparBlueShadow);
				    }
				    else{
				    	p.setAppearance(apparWhiteShadow);
				    }
				    
				    Transform3D t3d = new Transform3D();
				    Vector3f translate = new Vector3f();
				    translate.set((float)tablero.getPosCaballo().getY(), 0.0f, (float)tablero.getPosCaballo().getX());
				    t3d.setTranslation(translate);
				    caballo.setTransform(t3d);

				    Tablero tempTablero = tablero.clonar();
				    MinMaxSolucion minMax = new MinMaxSolucion(tempTablero, dificultad);
				    Point point = minMax.respuesta();
				    tempTablero = null;
				    minMax = null;
				    
				    if(point.getX() != 7.0) {
						tablero.setPosCaballo(point);
						t3d = new Transform3D();
						translate = new Vector3f();
						translate.set((float)tablero.getPosCaballo().getY(), 0.0f, (float)tablero.getPosCaballo().getX());
						t3d.setTranslation(translate);
						caballo.setTransform(t3d);
						
						if(((int)point.getX()+(int)point.getY())%2 == 0){
					    	(floor[(int)point.getX()][(int)point.getY()]).setAppearance(apparBlueShadow);
					    }
					    else{
					    	(floor[(int)point.getX()][(int)point.getY()]).setAppearance(apparWhiteShadow);
					    }
						
						System.out.println("respuesta: "+point.getX()+", "+point.getY());
				    }
				    else{
						gana = true;
						javax.swing.JOptionPane.showMessageDialog(null, "Ganaste !!!");
				    }

				    Tablero tableroTemp = tablero.clonar();

				    if(tableroTemp.pierde() && !gana){
				    	
						javax.swing.JOptionPane.showMessageDialog(null, "Perdiste !!!");
				    }

				    tableroTemp = null;
				}
				else{
				    System.out.println("soy un caballo !!!! no puedo llegar ahi o... quizas ya estube alli");
				}
		    }
		    else{
				System.out.println("tengo hambre...");
		    }
		}

		System.gc();
    }
    
    public void loadAppearances(){
      /***********************      ColoringAttributes       *********************************/	
      ColoringAttributes colorAtri= new ColoringAttributes();
	  colorAtri.setShadeModel(ColoringAttributes.SHADE_GOURAUD);
	  /***************************************************************************************/
      
      /************************************   apparWhite   ************************************/
      apparWhite.setCapability(Appearance.ALLOW_MATERIAL_READ);
	  apparWhite.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
	  
	  Color3f emissiveColourWhite= new Color3f(0.0f, 0.0f, 0.0f);
	  Color3f specularColourWhite= new Color3f(0.5f, 0.5f, 0.5f);
	  Color3f ambientColourWhite= new Color3f(Color.WHITE);
	  Color3f diffuseColourWhite= new Color3f(Color.WHITE);
	  
	  Material materialWhite = new Material(ambientColourWhite, emissiveColourWhite, diffuseColourWhite, specularColourWhite, 20.0f);
	  materialWhite.setCapability(Material.ALLOW_COMPONENT_WRITE);
	  materialWhite.setCapability(Material.ALLOW_COMPONENT_READ);
	  
	  apparWhite.setMaterial(materialWhite);
	  apparWhite.setColoringAttributes(colorAtri);
	  /***************************************************************************************/
	  
	  /************************************   apparBlue   ************************************/
	  apparBlue.setCapability(Appearance.ALLOW_MATERIAL_READ);
	  apparBlue.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
	  
	  Color3f emissiveColourBlue= new Color3f(0.0f, 0.0f, 0.0f);
	  Color3f specularColourBlue= new Color3f(0.5f, 0.5f, 0.5f);
	  Color3f ambientColourBlue= new Color3f(Color.BLUE);
	  Color3f diffuseColourBlue= new Color3f(Color.BLUE);

	  Material materialBlue = new Material(ambientColourBlue, emissiveColourBlue, diffuseColourBlue, specularColourBlue, 20.0f);
	  materialBlue.setCapability(Material.ALLOW_COMPONENT_WRITE);
	  materialBlue.setCapability(Material.ALLOW_COMPONENT_READ);
	  
      apparBlue.setMaterial(materialBlue);
	  apparBlue.setColoringAttributes(colorAtri);
	  /***************************************************************************************/
	  
	  /***************************** apparWhiteShadow ************************************/
	  apparWhiteShadow.setCapability(Appearance.ALLOW_MATERIAL_READ);
	  apparWhiteShadow.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
	  
	  Color3f diffuseColourShadow= new Color3f(Color.BLACK);
	  
	  Material materialWhiteShadow = new Material(ambientColourWhite, emissiveColourWhite, diffuseColourShadow, specularColourWhite, 20.0f);
	  materialWhiteShadow.setCapability(Material.ALLOW_COMPONENT_WRITE);
	  materialWhiteShadow.setCapability(Material.ALLOW_COMPONENT_READ);
	  
	  apparWhiteShadow.setMaterial(materialWhiteShadow);
	  apparWhiteShadow.setColoringAttributes(colorAtri);
	  /***************************************************************************************/
	  
	  /***************************** apparBlueShadow ************************************/
      apparBlueShadow.setCapability(Appearance.ALLOW_MATERIAL_READ);
	  apparBlueShadow.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
	  
	  Material materialBlueShadow = new Material(ambientColourBlue, emissiveColourBlue, diffuseColourShadow, specularColourBlue, 20.0f);
	  materialBlueShadow.setCapability(Material.ALLOW_COMPONENT_WRITE);
	  materialBlueShadow.setCapability(Material.ALLOW_COMPONENT_READ);
	  
	  apparBlueShadow.setMaterial(materialBlueShadow);
	  apparBlueShadow.setColoringAttributes(colorAtri);
    }

    public void addHorse(){
		BranchGroup caballoBG = new BranchGroup();
	      
		TransformGroup objScale = new TransformGroup();
		Transform3D t3d = new Transform3D();
		t3d.setScale(0.002);
		
		Transform3D T3D;
		Vector3f translate = new Vector3f();
		
		T3D = new Transform3D();
		translate.set(0.0f , 0.7f, -0.5f );
		Transform3D rotViewHorse= new Transform3D();
		rotViewHorse.rotX(-Math.PI/2.0);
		
		T3D.setTranslation(translate);
		T3D.mul(t3d);
		T3D.mul(rotViewHorse);
		objScale.setTransform(T3D);

		caballoBG.addChild(objScale);

		TransformGroup objCaballo = new TransformGroup();
		objCaballo.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		objCaballo.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		objScale.addChild(objCaballo);

		ModelLoader loader = new ModelLoader();

		Scene horse = null;
		try {
		    horse = loader.load("../3ds/horse.3ds");
		} catch (FileNotFoundException e) {
		    System.err.println(e);
		    System.exit(1);
		} catch (ParsingErrorException e) {
		    System.err.println(e);
		    System.exit(1);
		} catch (IncorrectFormatException e) {
		    System.err.println(e);
		    System.exit(1);
		}

		BranchGroup horseBG = horse.getSceneGroup();
		horseBG.removeChild(0);
		objCaballo.addChild(horseBG);

		caballo = new TransformGroup();
		caballo.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		caballo.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		caballo.addChild(caballoBG);
		
		Transform3D posCaballo = new Transform3D();
		posCaballo.setTranslation(new Vector3f ((float)tablero.getPosCaballo().getY(), 0.0f, (float)tablero.getPosCaballo().getX()));
		caballo.setTransform(posCaballo);
		
		addChild(caballo);
    }

    public void addBackground(){
		Background background = new Background();
		background.setColor( 0.0f, 0.0f, 0.0f );
		background.setApplicationBounds(new BoundingSphere());
		addChild(background);
    }

    public void addLight(){
		Color3f ambientColour = new Color3f(0.2f, 0.2f, 0.2f);
		AmbientLight ambientLightNode = new AmbientLight(ambientColour);
		ambientLightNode.setInfluencingBounds(bounds);

		Color3f lightColour = new Color3f(1.0f, 1.0f, 1.0f);
		Vector3f lightDir = new Vector3f(-1.0f, -1.0f, -0.5f);
		DirectionalLight light = new DirectionalLight(lightColour, lightDir);
		light.setInfluencingBounds(bounds);

		addChild(ambientLightNode);
		addChild(light);
    }

	public void addCamera(){
		Transform3D T3D;
		Vector3f translate = new Vector3f();

		T3D = new Transform3D();
		translate.set(2.5f, 4.5f, 13.0f);

		Transform3D rotViewScene = new Transform3D();
		rotViewScene.rotX(-Math.PI/8.0);

		T3D.setTranslation(translate);
		T3D.mul(rotViewScene);
		vpTrans.setTransform(T3D);

		keyNavCamara = new KeyNavigatorBehavior(vpTrans);
		keyNavCamara.setSchedulingBounds(bounds);
		addChild(keyNavCamara);
	}
      
	public void setDificultad(int dificultad){
		this.dificultad = dificultad;
	}
      
	public void addMaze(){
		for(int i=0; i < 6; i++){
			for(int j=0; j < 6; j++){
				Transform3D t3dSpf = new Transform3D();
				t3dSpf.setTranslation( new Vector3d(((float)j), 0.0, (float)i));
				TransformGroup vpTransSpf = new TransformGroup(t3dSpf);

				if( ((i+j)%2) == 0 ){
					Box box = new Box(0.5f, 0.1f, 0.5f, Box.GENERATE_NORMALS | Box.ENABLE_APPEARANCE_MODIFY, apparBlue);

					if( j == (int)tablero.getPosCaballo().getY() && i == (int)tablero.getPosCaballo().getX() ){
						box = new Box(0.5f, 0.1f, 0.5f, Box.GENERATE_NORMALS | Box.ENABLE_APPEARANCE_MODIFY, apparBlueShadow);
					}

					box.setUserData(i+","+j);
					vpTransSpf.addChild(box);
					addChild(vpTransSpf);
					floor[i][j] = box;
				}
				else{
					Box box = new Box(0.5f, 0.1f, 0.5f, Box.GENERATE_NORMALS | Box.ENABLE_APPEARANCE_MODIFY, apparWhite);

					if( j == (int)tablero.getPosCaballo().getY() && i == (int)tablero.getPosCaballo().getX() ){
						box = new Box(0.5f, 0.1f, 0.5f, Box.GENERATE_NORMALS | Box.ENABLE_APPEARANCE_MODIFY, apparWhiteShadow);
					}

					box.setUserData(i+","+j);
					vpTransSpf.addChild(box);
					addChild(vpTransSpf);
					floor[i][j] = box;
				}
			}
		}
	}
}