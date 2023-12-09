import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class codigoAdicional {

    public static void main(String[] args) {
        
    }

    /**
     * Calcula las componentes fuertemente conexas de un grafo dado.
     * Utiliza el algoritmo de búsqueda en profundidad (DFS) para encontrar las componentes.
     * 
     * @param graph el grafo en el que se buscarán las componentes fuertemente conexas
     * @return una lista de listas que contiene las componentes fuertemente conexas del grafo
     */
    public static List<List<Vertex<Integer>>> calculoDeComponentesFuertementeConexas(Graph<Vertex<Integer>> graph) {
        //Inicializamos un conjunto de visitados, una lista de finalizados y una lista para las componentes 
        //fuertemente conexas
        Set<Vertex<Integer>> visitados = new HashSet<>();
        List<Vertex<Integer>> finalizados = new ArrayList<>();
        List<List<Vertex<Integer>>> components = new ArrayList<>();

        //Recorremos todos los vertices del grafo.
        for (Vertex<Integer> vertex : graph.getAllVertices()) {
            //Si el vertice no ha sido visitado entonces se llama al metodo dfs().
            if (!visitados.contains(vertex)) {
                dfs(graph, vertex, visitados, finalizados, true);
            }
        }

        //Vaciamos el conjunto de visitados.
        visitados.clear();       

        //Recorremos la lista de finalizados en orden inverso.
        Collections.reverse(finalizados);

        //Creamos un grafo simetrico al grafo original.
        Graph<Vertex<Integer>> simetrico = graph.getSimetric();

        //Recorremos todos los vertices del grafo.
        for (Vertex<Integer> vertex : finalizados) {
            //Si el vertice no ha sido visitado entonces se llama al metodo dfsTranspose() 
            //y se inicializa una lista para la componenete fuertemente conexa del vertice actual.
            if (!visitados.contains(vertex)) {
                List<Vertex<Integer>> component = new ArrayList<>();
                dfs(simetrico, vertex, visitados, component, false);
                components.add(component);
            }
        }
        //Retornamos la lista de componentes.
        return components;
    }

    /**
     * Realiza una búsqueda en profundidad (DFS) en el grafo dado.
     * Si cualDFS es true, entonces en la busqueda DFS se busca generar la lista de finalizados.
     * Si cualDFS es false, entonces en la busqueda DFS se busca generar las componentes fuertemente conexas.
     * 
     * @param graph el grafo en el que se realizará la búsqueda
     * @param vertex el vértice actual
     * @param visitados un conjunto de vértices visitados
     * @param finalizadosOComponentes una lista de vértices finalizados o lista de vertices de una componente fuertemente 
     * conexa
     * @param cualDFS un booleano que indica si se trata del primer o segundo DFS
     */
    private static void dfs(Graph<Vertex<Integer>> graph, Vertex<Integer> vertex, Set<Vertex<Integer>> visitados, List<Vertex<Integer>> finalizadosOComponentes, boolean cualDFS) {
        //Si cualDFS es true entonces se busca generar la lista de finalizados.
        if (cualDFS == true){
            //Agregamos el vertice vertex al conjunto de visitados.
            visitados.add(vertex);
            //Recorremos todos los sucesores del vertice vertex.
            for (Vertex<Integer> vecino : graph.getOutwardEdges(vertex)) {
                //Si el sucesor no ha sido visitado entonces se llama al metodo dfs().
                if (!visitados.contains(vecino)) {
                    dfs(graph, vecino, visitados, finalizadosOComponentes, true);
                }
            }
            //Cuando ya hemos recorrido todos los sucesores de un vertice lo agregamos a la lista de finalizados.
            finalizadosOComponentes.add(vertex);
        //Si cualDFS es false entonces se busca generar las componentes fuertemente conexas.
        } else {
            //Agregamos el vertice vertex al conjunto de visitados y a la lista de componentes fuertemente conexas.
            visitados.add(vertex);
            finalizadosOComponentes.add(vertex);
            //Recorremos todos los predecesores del vertice vertex
            for (Vertex<Integer> vecino : graph.getOutwardEdges(vertex)) {
                //Si el sucesor no ha sido visitado entonces se llama al metodo dfs().
                if (!visitados.contains(vecino)) {
                    dfs(graph, vecino, visitados, finalizadosOComponentes, false);
                }
            }
        }
    }

    public static void generateGraphReducido(Graph<Vertex<Integer>> graph, List<List<Vertex<Integer>>> componentes) {
        //Recorremos la lista de componentes fuertemente conexas.
        for (List<Vertex<Integer>> component : componentes) {
            //Si la componente tiene mas de un vertice entonces se crea un vertice representante.
            if (component.size() > 1) {
                //Se inicializa el vertice representante con el primer vertice de la componente.
                Vertex<Integer> representante = new Vertex<Integer>(null);
                representante.setHeight(component.get(0).getHeight());
                //Se recorren todos los vertices de la componente.
                for (Vertex<Integer> vertex : component) {
                    //Si algun vertice se derrama entonces el representante tambien se derrama.
                    if (vertex.isSpills()) {
                        //Se cambia el atributo spills del representante a true.
                        representante.setSpills(true);
                    }
                }
                //Se agrega el representante al grafo.
                graph.add(representante);
                //Se recorren todos los vertices de la componente.
                for (Vertex<Integer> vertex : component) {
                    //Se recorren todos los adyacentes del vertice vertex.
                    for (Vertex<Integer> vecino : graph.getOutwardEdges(vertex)) {
                        //Si el vecino no pertenece a la componente entonces se conecta al representante.
                        if (!component.contains(vecino)) {
                            //Se conecta el representante con el vecino.
                            graph.connect(representante, vecino);
                        }
                    }
                    for (Vertex<Integer> vecino : graph.getInwardEdges(vertex)) {
                        //Si el vecino no pertenece a la componente entonces se conecta al representante.
                        if (!component.contains(vecino)) {
                            //Se conecta el representante con el vecino.
                            graph.connect(representante, vecino);
                            //Se aumenta el grado de entrada del representante.
                            representante.upInwardDegree();
                        }
                    }
                    //Se elimina el vertice vertex del grafo.
                    graph.remove(vertex);
                }
            }
        }
    }
}

