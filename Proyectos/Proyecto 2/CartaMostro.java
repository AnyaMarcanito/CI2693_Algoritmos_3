import java.util.HashSet;
import java.util.Set;

/**
 * La clase CartaMostro representa una carta de monstruo en un juego de cartas. 
 * Cada carta tiene un nombre, nivel, poder y atributo. 
 * El nombre no puede ser null o vacío, el nivel debe estar entre 1 y 12, 
 * el poder debe ser un múltiplo de 50, y el atributo debe ser uno de los siguientes: 
 * AGUA, FUEGO, VIENTO, TIERRA, LUZ, OSCURIDAD, DIVINO.
 * Además, no puede haber dos cartas con el mismo nombre.
 */
public class CartaMostro {
    private String nombre;
    private int nivel;
    private int poder;
    private String atributo;

    private static Set<String> cartasExistentes = new HashSet<>();

    //Construtor de la clase CartaMostro
    public CartaMostro(String nombre, int nivel, int poder, String atributo) {
        //Verificamos que los parametros sean validos
        //Si no son validos se lanza una excepcion

        if (nombre == null || nombre.equals("")) {
            //Verificamos que el nombre no sea null o vacío
            throw new IllegalArgumentException("El nombre de la carta no puede ser null o vacío");
        } else if (nivel < 1 || nivel > 12) {
            //Verificamos que el nivel este entre 1 y 12
            throw new IllegalArgumentException("El nivel debe estar entre 1 y 12");
        } else if (poder % 50 != 0) {
            //Verificamos que el poder sea un múltiplo de 50
            throw new IllegalArgumentException("La magnitud de poder debe ser un número entero múltiplo de 50");
        } else if (!atributo.equals("AGUA") && !atributo.equals("FUEGO") && !atributo.equals("VIENTO") &&
        !atributo.equals("TIERRA") && !atributo.equals("LUZ") && !atributo.equals("OSCURIDAD") &&
        !atributo.equals("DIVINO")) {
            //Verificamos que el atributo sea uno de los siguientes: AGUA, FUEGO, VIENTO, TIERRA, LUZ, OSCURIDAD, DIVINO
            throw new IllegalArgumentException("El atributo debe ser uno de los siguientes: AGUA, FUEGO, VIENTO, TIERRA, LUZ, OSCURIDAD, DIVINO");
        } else if (cartasExistentes.contains(nombre)) {
            //Verificamos que no exista una carta con el mismo nombre
            throw new IllegalArgumentException("Ya existe una carta con el nombre " + nombre);
        } else {
            //Si los parametros son validos se agregan al Set de cartas existentes
            cartasExistentes.add(nombre);
            this.nombre = nombre;
            this.nivel = nivel;
            this.poder = poder;
            this.atributo = atributo;
        }
        
    }
    
    //Metodo para obtener el nombre de la carta
    public String getNombre() {
        return nombre;
    }
    //Metodo para obtener el nivel de la carta
    public int getNivel() {
        return nivel;
    }
    //Metodo para obtener el poder de la carta
    public int getPoder() {
        return poder;
    }
    //Metodo para obtener el atributo de la carta
    public String getAtributo() {
        return atributo;
    }
}
