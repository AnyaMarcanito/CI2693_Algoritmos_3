import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

interface Graph<T> {
    boolean add(T vertex);
    boolean connect(T from, T to);
    boolean disconnect(T from, T to);
    boolean contains(T vertex);
    List<T> getInwardEdges(T to);
    List<T> getOutwardEdges(T from);
    List<T> getVerticesConnectedTo(T vertex);
    List<T> getAllVertices();
    boolean remove(T vertex);
    int size();
    Graph<T> subgraph(Collection<T> vertices);
}

class AdjacencyListGraph<T> implements Graph<T> {
    private Map<T, List<T>> adjacencyMap;

    //METODO CONSTRUCTOR
    public AdjacencyListGraph() {
        adjacencyMap = new HashMap<>();
    }

    //METODO ADD
    public boolean add(T vertex) {
        //Verificamos si el vertice que queremos agregar ya pertenece al HashMap.
        if (!contains(vertex)) {
            //Si no pertenece es agregado al HashMap y se retorna true.
            adjacencyMap.put(vertex, new ArrayList<>());
            System.out.println("Se ha agregado el vertice "+vertex+" con exito.");
            return true;
        }
        //Si pertenece se retorna false.
        System.out.println("No se ha podido agregar el vertice "+vertex+" .");
        return false;
    }

    //METODO CONNECT
    public boolean connect(T from, T to) {
        //Verificamos si los vertices from y to pertenecen al HashMap.
        if (contains(from) && contains(to)) {
            /**Buscamos la lista de los sucesores del vertice from usando el metodo 
             * .get() del HashMap.*/
            List<T> sucesores = adjacencyMap.get(from);
            //Verificamos los sucesores para ver si el arco from-to ya existe.
            if (!sucesores.contains(to)) {
                //Si no existe lo agregamos usando .add
                sucesores.add(to);
                System.out.println("Se ha agregado el arco "+from+" - "+to+" con exito.");
                return true;
            }
        }
        /**Si alguno de los vertices (from o to) no pertenecen al HashMap, o
         * el arco from-to ya existe entonces se retorna false.*/
        System.out.println("No se ha podido agregar el arco "+from+" - "+to+" .");
        return false;
    }

    //METODO DISCONNET
    public boolean disconnect(T from, T to) {
        //Verificamos si los vertices from y to pertenecen al HashMap.
        if (contains(from) && contains(to)) {
            /**Buscamos la lista de los sucesores del vertice from usando el metodo
             * .get() del HashMap.*/
            List<T> sucesores = adjacencyMap.get(from);
            /**Usamos el metodo .remove() de los ArrayList para eliminar el arco from-to.
             * Esta llamada devuelve true si el elemento se encontraba en la lista y se 
             * eliminó exitosamente, y devuelve false si el elemento no se encontraba en
             *  la lista*/
            System.out.println("Se ha eliminado el arco "+from+" - "+to+" con exito.");
            return sucesores.remove(to);
        }
        System.out.println("No se ha podido eliminar el arco "+from+" - "+to+" .");
        return false;
    }

    //METODO CONTAINS
    public boolean contains(T vertex) {
        /**Usanmos el método .containsKey() propio de la clase HashMap para verificar si 
         * la clave vertex está presente en el mapa. Si lo esta retorna true, si no
         * retorna false.*/
        boolean x = adjacencyMap.containsKey(vertex);
        if (x == true){
            System.out.println("El grafo contiene al vertice "+vertex);
        } else {
            System.out.println("El grafo no contiene al vertice "+vertex);
        }
        return x;
    }

    //METODO GETINWARDEDGES
    public List<T> getInwardEdges(T to) {
        /**Creamos una lista nueva para almacenar los vértices que tienen arcos dirigidas 
         * hacia el vertice to.*/
        List<T> predecesores = new ArrayList<>();
        /**Usamos el metodo .keyset() de la clase HashMap para iterar sobre todos los 
         * vertices del mapa.*/
        for (T vertex : adjacencyMap.keySet()) {
            /**Buscamos la lista de los sucesores del vertice vertex usando el metodo 
             * .get() del HashMap.*/
            List<T> sucesores = adjacencyMap.get(vertex);
            /**Usamos el metodo .contains() de la clase ArrayList para verificar si la 
             * lista de sucesores contiene al vertice to.*/
            if (sucesores.contains(to)) {
                /**Si es asi, entonces vertex es un predecesor de to, por lo que lo 
                 * agregamos a la lista predecesores.*/
                predecesores.add(vertex);
            }
        }
        return predecesores;
    }

    //METODO GETOUTWARDEDGES
    public List<T> getOutwardEdges(T from) {
        //Usamos contains() para verificar que el vertice from pertenezca al HashMap.
        if (contains(from)) {
            /**Si pertenece, usamos el metodo .get() de la clase HashMap para retornar
             * la lista de sucesores del vertice from.*/
            return adjacencyMap.get(from);
        }
        //Si no pertenece se retorna un ArrayList vacio.
        return new ArrayList<>();
    }

    //METODO GETVERTICESCONNECTEDTO
    public List<T> getVerticesConnectedTo(T vertex) {
        //Usamos contains() para verificar que el vertice vertex pertenezca al HashMap.
        if (contains(vertex)) {
            /**Creamos una lista nueva para almacenar los vértices que estan conectados
             * al vertice vertex.*/
            List<T> adyacentesVertex = new ArrayList<>();
            /**Usamos el metodo .keyset() de la clase HashMap para iterar sobre todos los 
             * vertices del mapa.*/
            for (T v : adjacencyMap.keySet()) {
                /**Buscamos la lista de los sucesores del vertice v usando el metodo 
                 * .get() del HashMap.*/
                List<T> sucesores = adjacencyMap.get(v);
                /**Usamos el metodo .contains() de la clase ArrayList para verificar si la 
                 * lista de sucesores contiene al vertice vertex.*/
                if (sucesores.contains(vertex)) {
                    /**Si vertex forma parte de los sucesores de v, entonces agregamos 
                     * el vertice v a la lista de adyacentes de vertex.*/
                    adyacentesVertex.add(v);
                }
            }
            return adyacentesVertex;
        }
        //Si vertex no pertenece al HashMap se retorna un ArrayList vacio.
        return new ArrayList<>();
    }

    //METODO GETALLVERTICES
    public List<T> getAllVertices() {
        /**Usamos el metodo .keyset() de la clase HashMap para devolver un ArrayList 
         * con todos los elementos dentro del HashMap */
        return new ArrayList<>(adjacencyMap.keySet());
    }

    //METODO REMOVE
    public boolean remove(T vertex) {
        //Usamos contains() para verificar que el vertice vertex pertenezca al HashMap.
        if (contains(vertex)) {
            /**Si el vertice vertex pertenece, usamos el metodo .remove() de la clase
             * HashMap para eliminarlo.*/
            adjacencyMap.remove(vertex);
            /**Usamos el metodo .values() de la clase HashMap para iterar sobre todos
             * los valores del HashMap.*/
            for (List<T> sucesores : adjacencyMap.values()) {
                /**Usamos el método .remove() en cada lista de sucesores para eliminar 
                 * el vértice vertex de todas las listas de sucesores.*/
                sucesores.remove(vertex);
            }
            System.out.println("El vertice "+vertex+" ha sido eliminado del grafo.");
            return true;
        }
        //Si el vertice vertex no pertenece se retorna false.
        System.out.println("El vertice "+vertex+" no pertenece al grafo");
        return false;
    }

    //METODO SIZE
    public int size() {
        //Usamos el metodo .size() de la clase HashMap.
        System.out.println(adjacencyMap.size());
        return adjacencyMap.size();
    }

    //METODO SUBGRAPH
    public Graph<T> subgraph(Collection<T> vertices) {
        //Creamos un nuevo objeto tipo Graph<T>.
        Graph<T> subgraph = new AdjacencyListGraph<>();
        //Iteramos sobre la collection de vertices tipo T.
        for (T vertex : vertices) {
            //Usamos contains para verificar si el vertice vertex pertenece al grafo original.
            if (contains(vertex)) {
                //Si pertenece agregamos el vertice vertex a los vertices del subgrafo.
                subgraph.add(vertex);
                //Usamos el metodo getOutwardEdges para conseguir los sucesores de vertex.
                List<T> sucesores = getOutwardEdges(vertex);
                //Iteramos sobre cada sucesor de vertex.
                for (T sucesor : sucesores) {
                    //Verificamos si la collection vertices contiene a sucesor.
                    if (vertices.contains(sucesor)) {
                        //Si el sucesor pertenece, lo agregamos al subgrafo y conectamos.
                        subgraph.add(sucesor);
                        subgraph.connect(vertex, sucesor);
                    }
                }
            }
        }
        return subgraph;
    }

}

public class main {
    public static void main(String[] args) {
        // Crear un grafo dirigido
        Graph<String> graph = new AdjacencyListGraph<>();

        // Agregar vértices al grafo
        graph.add("A");
        graph.add("B");
        graph.add("C");
        graph.add("D");
        graph.add("E");

        // Conectar vértices en el grafo
        graph.connect("A", "B");
        graph.connect("A", "C");
        graph.connect("A", "D");
        graph.connect("A", "E");
        graph.connect("B", "C");
        graph.connect("B", "A");
        graph.connect("B", "D");
        graph.connect("B", "E");
        graph.connect("C", "D");
        graph.connect("D", "E");
        graph.connect("E", "A");

        // Desconectar vertices en el grafo
        graph.disconnect("A", "C");
        graph.disconnect("A", "E");

        // Ver si un vertice esta en el grafo
        graph.contains("F");
        graph.contains("A");

        // Ver arcos entrantes y salientes
        graph.getInwardEdges("E");
        graph.getOutwardEdges("A");

        // Ver el tamano del grafo
        graph.size();

        // Obtener todos los vértices del grafo
        List<String> allVertices = graph.getAllVertices();
        System.out.println("Todos los vértices: " + allVertices);

        System.out.println("----------------------------------------------------------------");


        // Crear un subgrafo a partir de un conjunto de vértices
        List<String> subgraphVertices = Arrays.asList("A", "B", "C");
        Graph<String> subgraph = graph.subgraph(subgraphVertices);
        System.out.println("Subgrafo: " + subgraph);

        // Eliminar un vértice y sus conexiones del grafo
        boolean removed = graph.remove("D");
        System.out.println("Vértice eliminado: " + removed);
        System.out.println("Grafo después de eliminar el vértice: " + graph);
    }
}