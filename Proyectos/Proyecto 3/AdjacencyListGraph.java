import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interfaz GraphV2: modificación de la interfaz Graph
 * en la que se agrega el método getPeso, se modifica
 * el método connect para almacene un peso junto con el 
 * arco (from, to) y se agrega el uso de la clase 
 * ArcosConPesos como tipo de dato para almacenar los
 * arcos con sus respectivos pesos. Asimismo, se modifica 
 * el método getOutwardEdges para que retorne una lista 
 * de objetos de tipo ArcosConPesos.
 * 
 * NOTA: Solo fueron adaptados los metodos de la interfaz 
 * Graph que serian necesarios para la implementacion de
 * la solucion al problema planteado para este laboratorio.
 */
interface GraphV2<T> {
    boolean add(T vertex);
    boolean connect(T from, T to, double peso);
    boolean contains(T vertex);
    List<ArcosConPesos<T>> getOutwardEdges(T from);
    List<T> getAllVertices();
    Double getPeso(T from, T to);
}
/**
 * Clase ArcosConPesos: clase que almacena un arco
 * junto con su peso, y que implementa los metodos getVertex()
 * y getPeso() para tener acceso a los atributos de la clase.
 */

class ArcosConPesos<T> {
    private T vertex;
    private double peso;

    // Método Constructor
    public ArcosConPesos(T vertex, double peso) {
        this.vertex = vertex;
        this.peso = peso;
    }

    // Métodos get para acceder al vertice del arco
    public T getVertex() {
        return vertex;
    }

    // Métodos get para acceder al peso del arco
    public double getPeso() {
        return peso;
    }

    @Override
    public String toString() {
        return vertex + " " + peso;
    }
}

/**
 * Clase AdjacencyListGraph: implementacion de la interfaz
 * GraphV2 que utiliza una lista de adyacencia para almacenar
 * los vertices y sus arcos con pesos.
 */
class AdjacencyListGraph<T> implements GraphV2<T> {
    private Map<T, List<ArcosConPesos<T>>> adjacencyMap;

    // Método Constructor
    public AdjacencyListGraph() {
        adjacencyMap = new HashMap<>();
    }

    // Método add
    public boolean add(T vertex) {
        // Si el vertice no existe, se agrega a la lista de adyacencia
        if (!contains(vertex)) {
            // Se crea una lista vacia para almacenar los arcos del vertice
            adjacencyMap.put(vertex, new ArrayList<>());
            // Se retorna true para indicar que el vertice fue agregado
            return true;
        }
        // Si el vertice ya existe, se retorna false
        return false;
    }

    // Método connect
    public boolean connect(T from, T to, double peso) {
        // Revisamos que ambos vertices existan en la lista de adyacencia
        if (contains(from) && contains(to)) {
            // De ser asi, obtenemos la lista de arcos del vertice from
            List<ArcosConPesos<T>> successors = adjacencyMap.get(from);
            // Revisamos que el arco (from, to) no exista
            for (ArcosConPesos<T> pair : successors) {
                if (pair.getVertex().equals(to)) {
                    return false; // La arista ya existe
                }
            }
            // Si el arco no existe, lo agregamos a la lista de arcos del vertice from.
            successors.add(new ArcosConPesos<>(to, peso));
            // Retornamos true para indicar que el arco fue agregado exitosamente.
            return true;
        }
        // Si alguno de los vertices no existe, retornamos false.
        return false;
    }

    // Método contains
    public boolean contains(T vertex) {
        // Revisamos si el vertice existe en la lista de adyacencia y retornamos el resultado.
        return adjacencyMap.containsKey(vertex);
    }

    // Método getOutwardEdges
    public List<ArcosConPesos<T>> getOutwardEdges(T from) {
        // Revisamos si el vertice existe en la lista de adyacencia
        if (contains(from)) {
            // Si existe, retornamos la lista de arcos del vertice from
            List<ArcosConPesos<T>> lista = adjacencyMap.get(from);
            return lista;
        }
        // Si el vertice no existe, retornamos una lista vacia
        return new ArrayList<>();
    }

    // Método getAllVertices
    public List<T> getAllVertices() {
        // Retornamos una lista con todos los vertices de la lista de adyacencia
        return new ArrayList<>(adjacencyMap.keySet());
    }

    // Método getPeso
    public Double getPeso(T from, T to) {
        // Revisamos que ambos vertices existan en la lista de adyacencia
        if (contains(from) && contains(to)) {
            // De ser asi, obtenemos la lista de arcos del vertice from
            List<ArcosConPesos<T>> successors = adjacencyMap.get(from);
            for (ArcosConPesos<T> pair : successors) {
                // Revisamos si el arco (from, to) existe
                if (pair.getVertex().equals(to)) {
                    // Si existe, retornamos el peso del arco
                    return pair.getPeso();
                }
            }
        }
        // Si alguno de los vertices no existe o el arco no existe retornamos null
        return null;
    }
}

