    import java.util.*;
    import java.awt.*;

    public class MinMaxArbol{
        private Point solucion;
        private MinMaxArbol padre;
        private int altura;
        private Vector <MinMaxArbol> hijos;
        private Tablero tablero;
        private int tipo; // 1 max 0 min
        private boolean termina;
        private Double utilidad;
        
        public MinMaxArbol(Tablero tablero){
        	this.padre = null;
        	this.hijos = new Vector();
        	this.altura = 0;
        	this.tablero = tablero;
        	this.tipo = 1;
        	this.termina = false;
        	this.solucion = new Point(7, 7);
        }
        
        public Point getSolucion(){
    	   return solucion;
        }
        
        public void setSolucion(Point sol){
    	   this.solucion = sol;
        }

        public void setUtilidad(Double utilidad){
    	   this.utilidad = utilidad;
        }
        
        public Double getUtilidad(){
    	   return this.utilidad;
        }

        public void calcularUtilidad(){
        	double count = 0;
        	for(int i=(int)tablero.getPosCaballo().getX()-2; i<(int)tablero.getPosCaballo().getX()+3; i++){
        	    for(int j=(int)tablero.getPosCaballo().getY()-2; j<(int)tablero.getPosCaballo().getY()+3; j++){
        		if(i>=0 && i<6 && j>=0 && j<6){
        		    if((tablero.getTablero())[i][j]==0){
        			count++;
        		    }
        		}
        	    }
        	}
    	   this.utilidad = (double)(count);
        }

        public boolean getTermina(){
    	   return this.termina;
        }

        public void setTermina(boolean termina){
    	   this.termina = termina;
        }

        public int getTipo(){
    	   return this.tipo;
        }

        public void setTipo(int tipo){
    	   this.tipo = tipo;
        }

        public Tablero getTablero(){
    	   return this.tablero;
        }

        public void setTablero(Tablero tablero){
    	   this.tablero = tablero;
        }

        public void setPadre(MinMaxArbol padre){
    	   this.padre = padre;
        }

        public MinMaxArbol getPadre(){
    	   return this.padre;
        }

        public void agregarHijo(MinMaxArbol hijo){
    	   this.hijos.add(hijo);
        }

        public MinMaxArbol getHijo(int i){
    	   return this.hijos.get(i);
        }

        public Vector getHijos(){
    	   return this.hijos;
        }

        public void setAltura(int altura){
    	   this.altura = altura;
        }

        public int getAltura(){
    	   return this.altura;
        }

        public void destruir() {
            for(int i=0; i<this.hijos.size(); i++){
                this.hijos.elementAt(i).destruir();
                this.hijos.set(i, null);
            }

            this.hijos = null;
        }
    }