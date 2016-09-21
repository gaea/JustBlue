import java.awt.*;
public class Tablero{
    
    private int tablero [][];
    private Point posCaballo;
    private int largo;
    private int ancho;
    
    public Tablero(int ancho, int largo){
	this.ancho = ancho;
	this.largo = largo;
	posCaballo = new Point();
	tablero = new int [ancho][largo];
	for(int i=0; i<ancho; i++){
	    for(int j=0; j<largo; j++){
		tablero[i][j]=0;
	    }
	}
    }

    public boolean validarPosicion(Point point){
		int x = (int)point.getX();
		int y = (int)point.getY();

		if(tablero[x][y] == 1){
		    return false;
		}
		else{
		    if(((Math.abs(this.posCaballo.getX()-x)) + (Math.abs(this.posCaballo.getY()-y))) == 3){
				if(((Math.abs(this.posCaballo.getX()-x)) == 2) ||
				  ((Math.abs(this.posCaballo.getY()-y)) == 2)){
				    return true;
				}
				else{
				    return false;
				}
		    }
		    else{
				return false;
		    }
		}
    }
    
    public boolean pierde(){
		int count =0;
		for(int i=(int)this.getPosCaballo().getX()-2; i<(int)this.getPosCaballo().getX()+3; i++){
		    for(int j=(int)this.getPosCaballo().getY()-2; j<(int)this.getPosCaballo().getY()+3; j++){
				if(i>=0 && i<6 && j>=0 && j<6){
				    Point point = new Point(i, j);
				    if(this.validarPosicion(point)){
						count++;
				    }
				}
		    }
		}
		if(count==0){
		    return true;
		}
		else{
		    return false;
		}
    }

    public Point getPosCaballo(){
		return this.posCaballo;
    }
    
    public void setPosCaballo(Point posCaballo){
		this.posCaballo.setLocation(posCaballo);
		//System.out.println((int)posCaballo.getX()+"----"+(int)posCaballo.getY());
		tablero[(int)posCaballo.getX()][(int)posCaballo.getY()] = 1;
		
		/*for(int i=0; i<ancho; i++){
		    for(int j=0; j<largo; j++){
			System.out.print(tablero[i][j]+" ");
		    }
		    System.out.println(" ");
		}*/
    }
    
    public Tablero clonar(){
		Tablero tab = new Tablero(ancho, largo);
		for(int i=0; i<ancho; i++){
		    for(int j=0; j<largo; j++){
				tab.getTablero()[i][j] = this.tablero[i][j];
		    }
		}
		int x = (int)this.posCaballo.getX();
		int y = (int)this.posCaballo.getY();
		tab.setPosCaballo(new Point(x, y));
		return tab;
    }

    public int[][] getTablero(){
		return this.tablero;
    }
}