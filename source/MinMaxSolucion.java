import java.awt.*;
import java.util.*;

public class MinMaxSolucion{

    private MinMaxArbol minMaxArbol;
    private Tablero tablero;
    private Point respuesta;
    private int dificultad;

    public MinMaxSolucion(Tablero tablero, int dificultad){
		this.tablero = tablero;
		this.dificultad = dificultad;
		this.respuesta = null;
		this.minMaxArbol = new MinMaxArbol(tablero);
		solucionMinMax(minMaxArbol);
		respuesta = minMaxArbol.getSolucion();
		minMaxArbol.destruir();
		minMaxArbol = null;
    }

    public void solucionMinMax(MinMaxArbol minMaxArbol){
		if(minMaxArbol.getAltura() < dificultad){
		    int count = 0;
		    for(int i=0; i<6; i++){
				for(int j=0; j<6; j++){
				    Point point = new Point(i, j);
				    if(minMaxArbol.getTablero().validarPosicion(point)){
						count++;
						Tablero tempTablero = minMaxArbol.getTablero().clonar();
						MinMaxArbol hijo = new MinMaxArbol(tempTablero);
						hijo.getTablero().setPosCaballo(point);
						hijo.setPadre(minMaxArbol);
						hijo.setAltura(minMaxArbol.getAltura() + 1);
						if(minMaxArbol.getTipo() == 1){
						    hijo.setTipo(0);
						}
						minMaxArbol.agregarHijo(hijo);
						tempTablero = null;
				    }
				}
		    }

		    for(int i=0; i<minMaxArbol.getHijos().size(); i++){
				solucionMinMax((MinMaxArbol)minMaxArbol.getHijos().get(i));
		    }
		    if(minMaxArbol.getHijos().size()>0){
				retornarSolucion(minMaxArbol);
		    }
		    else{
				minMaxArbol.calcularUtilidad();
		    }
		}   
		if(minMaxArbol.getAltura() == dificultad){
		    minMaxArbol.calcularUtilidad();
		}
    }
    
    public void retornarSolucion(MinMaxArbol minMaxArbol){
		if(minMaxArbol.getUtilidad() == null){
		    Point desicion = new Point();
		    double tempUilidad;
		    if(minMaxArbol.getTipo() == 1){
				tempUilidad = ((MinMaxArbol)minMaxArbol.getHijos().get(0)).getUtilidad();
				desicion =  ((MinMaxArbol)minMaxArbol.getHijos().get(0)).getTablero().getPosCaballo();
				for(int i=1; i<minMaxArbol.getHijos().size(); i++){
				    if(tempUilidad < ((MinMaxArbol)minMaxArbol.getHijos().get(i)).getUtilidad()){
						tempUilidad = ((MinMaxArbol)minMaxArbol.getHijos().get(i)).getUtilidad();
						desicion =  ((MinMaxArbol)minMaxArbol.getHijos().get(i)).getTablero().getPosCaballo();
				    }
				}
		    }
		    else{
				tempUilidad = ((MinMaxArbol)minMaxArbol.getHijos().get(0)).getUtilidad();
				desicion =  ((MinMaxArbol)minMaxArbol.getHijos().get(0)).getTablero().getPosCaballo();
				for(int i=1; i<minMaxArbol.getHijos().size(); i++){
				    if(tempUilidad > ((MinMaxArbol)minMaxArbol.getHijos().get(i)).getUtilidad()){
						tempUilidad = ((MinMaxArbol)minMaxArbol.getHijos().get(i)).getUtilidad();
						desicion =  ((MinMaxArbol)minMaxArbol.getHijos().get(i)).getTablero().getPosCaballo();
				    }
				}
		    }
		    minMaxArbol.setUtilidad(tempUilidad);
		    minMaxArbol.setSolucion(desicion);
		    respuesta = desicion;
		}
		else{
		    minMaxArbol.setSolucion(minMaxArbol.getTablero().getPosCaballo());
		    respuesta = minMaxArbol.getTablero().getPosCaballo();
		}
    }

    public Point respuesta(){
		return this.respuesta;
    }
}