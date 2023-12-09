import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    //Metodo Constructor
    public AdjacencyListGraph() {
        adjacencyMap = new HashMap<>();
    }

    //Metodo add
    public boolean add(T vertex) {
        //Verificamos si el vertice que queremos agregar ya pertenece al HashMap.
        if (!contains(vertex)) {
            //Si no pertenece es agregado al HashMap y se retorna true.
            adjacencyMap.put(vertex, new ArrayList<>());
            return true;
        }
        //Si pertenece se retorna false.
        return false;
    }

    //Metodo Connect
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
                return true;
            }
        }
        /**Si alguno de los vertices (from o to) no pertenecen al HashMap, o
         * el arco from-to ya existe entonces se retorna false.*/
        return false;
    }

    //Metodo Disconnect
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
            return sucesores.remove(to);
        }
        return false;
    }

    //Metodo Contains
    public boolean contains(T vertex) {
        /**Usamos el método .containsKey() propio de la clase HashMap para verificar si 
         * la clave vertex está presente en el mapa. Si lo esta retorna true, si no
         * retorna false.*/
        boolean x = adjacencyMap.containsKey(vertex);
        return x;
    }

    //Metodo GetInwardEdges
    public List<T> getInwardEdges(T to) {
        /**Creamos una lista nueva para almacenar los vértices que tienen arcos 
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

    //Metodo GetOutwardEdges
    public List<T> getOutwardEdges(T from) {
        //Usamos contains() para verificar que el vertice from pertenezca al HashMap.
        if (contains(from)) {
            /**Si pertenece, usamos el metodo .get() de la clase HashMap para retornar
             * la lista de sucesores del vertice from.*/
            return adjacencyMap.get(from);
        }
        //Si vertex no pertenece al HashMap se retorna un ArrayList vacio.
        return new ArrayList<>();
    }

    //Metodo GetVerticesConnectedTo
    public List<T> getVerticesConnectedTo(T vertex) {
        //Usamos contains() para verificar que el vertice vertex pertenezca al HashMap.
        if (contains(vertex)) {
            /**Creamos un conjunto nuevo para almacenar los vértices que estan conectados
             * al vertice vertex e introducimos los sucesores*/
            Set<T> adjacentVertex = new HashSet<>(getOutwardEdges(vertex));
            //Se agregan los predecesores.
            adjacentVertex.addAll(getInwardEdges(vertex));
            //Se retorna el conjunto como lista.
            return new ArrayList<>(adjacentVertex);
        }
        //Si vertex no pertenece al HashMap se retorna un ArrayList vacio.
        return new ArrayList<>();
    }

    //Metodo GetAllVertices
    public List<T> getAllVertices() {
        /**Usamos el metodo .keyset() de la clase HashMap para devolver un ArrayList 
         * con todos los elementos dentro del HashMap */
        return new ArrayList<>(adjacencyMap.keySet());
    }

    //Metodo Remove
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
            return true;
        }
        //Si el vertice vertex no pertenece se retorna false.
        return false;
    }

    //Metodo Size
    public int size() {
        //Usamos el metodo .size() de la clase HashMap.
        return adjacencyMap.size();
    }

    //Metodo Subgraph
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
