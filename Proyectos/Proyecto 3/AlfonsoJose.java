import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlfonsoJose {
    public static void main(String[] args) {
        String fileName = "atlantis.txt";
        int[][] matrix= readMatrixFromFile(fileName);
        Graph<Vertex<Integer>> graph = createGraph(matrix);
        
    }
    
    private static int[][] readMatrixFromFile(String fileName) {    
        // Leemos el archivo y agregamos los vértices al grafo:
        int fila = 0;
        int columna = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // Leemos la primera línea del archivo:
            String line;
            // Definimos contadores para saber el número de filas y columnas de la matriz de entrada:
            // Iteramos sobre cada línea del archivo:
            while ((line = reader.readLine()) != null) {
                // Leemos, solo la primera línea, por entradas:
                if (fila == 0) {
                    // Separamos en un arreglo por espacios en blanco.
                    String[] values = line.trim().split(" ");
                    // El número de columnas es igual a el número de entradas.
                    columna = values.length;
                }
                // Aumentamos el contador de filas cada vez que avanzamos a la siguiente linea.
                fila++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Creamos la matriz de alturas de cada torre:
        int [][] matriz = new int [fila][columna];

        // Volvemos a iterar, ahora si, sobre todas los valores del archivo.
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            // Colocamos un iterador "i" para controlar las filas.
            int i = 0;
            while ((line = reader.readLine()) != null){
                // Colocamos un iterador "j" para controlar las columnas.
                int j = 0;
                String[] values = line.trim().split(" ");
                // Iteramos sobre cada valor de la línea:
                for (String value : values) {
                    // Convertimos el valor a entero:
                    int torre = Integer.parseInt(value);
                    // Añadimos el valor a la matriz en la posición i, j;
                    matriz[i][j] = torre;
                    // Incrementamos el contador de columna:
                    j++;
                }
                // Incrementamos el contador de fila:
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matriz;
    }

    private static int valueK(int i, int j, int n, int m) {
        if ( i != j) {
            return i - j;
        } else {
            return i * n * m;
        }
    }

    private static Graph<Vertex<Integer>> createGraph(int [][] matriz) {
        // Creamos el grafo de vertices.
        Graph<Vertex<Integer>> graph = new AdjacencyListGraph<>();
        // Recolectamos las dimensiones de la matriz nxm.
        int n = matriz.length;
        int m = matriz[0].length;
        // Recorremos la matriz de vertices.
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // Creamos un valor unico para el vertice del grafo
                int k = valueK(i, j, n, m);
                // Creamos el vertices a añadir al grafo.
                Vertex<Integer> torre = new Vertex<Integer>(k);
                // Colocamos su altura.
                torre.setHeight(matriz[i][j]);
                // Si se encuentra en uno de los bordes, contamos que el agua se puede derramar por el.
                if (i == 0 || i == n-1 || j == 0 || j == m-1) {
                    torre.setSpills(true);
                }
                // Añadimos al vertice.
                graph.add(torre);
                // Luego, conectamos el vertice a sus vertices adyacentes:
                // Caso particular 1, primera posicion:
                if (i == 0 && j == 0) {
                    // Discriminador de caso borde "solo hay una columna".
                    if (m > 1) {
                        if (torre.getHeight() >= matriz[i][j+1]) {
                            Vertex<Integer> torreDerecha = new Vertex<Integer>(valueK(i, j+1, n, m));
                            graph.add(torreDerecha);
                            if (graph.connect(torre, torreDerecha)) {
                                torreDerecha.upInwardDegree();
                            }
                        }
                    }
                    // Discriminador de caso brode "solo hay una fila".
                    if (n > 1) {
                        if (torre.getHeight() >= matriz[i+1][j]) {
                            Vertex<Integer> torreInferior = new Vertex<Integer>(valueK(i+1, j, n, m));
                            graph.add(torreInferior);
                            if (graph.connect(torre, torreInferior)) {
                                torreInferior.upInwardDegree();
                            }
                        }
                    }
                }
                // Caso general 1, primera fila:
                if (i == 0 && j > 0 && j != m-1) {
                    if (torre.getHeight() >= matriz[i][j-1]) {
                        Vertex<Integer> torreIzquierda = new Vertex<Integer>(valueK(i, j-1, n, m));
                        graph.add(torreIzquierda);
                        if (graph.connect(torre, torreIzquierda)) {
                            torreIzquierda.upInwardDegree();
                        }
                    }
                    if (torre.getHeight() >= matriz[i][j+1]) {
                        Vertex<Integer> torreDerecha = new Vertex<Integer>(valueK(i, j+1, n, m));
                        graph.add(torreDerecha);
                        if (graph.connect(torre, torreDerecha)) {
                            torreDerecha.upInwardDegree();
                        }
                    }
                    // Discriminador de caso brode "solo hay una fila".
                    if (n > 1) {
                        if (torre.getHeight() >= matriz[i+1][j]) {
                            Vertex<Integer> torreInferior = new Vertex<Integer>(valueK(i+1, j, n, m));
                            graph.add(torreInferior);
                            if (graph.connect(torre, torreInferior)) {
                                torreInferior.upInwardDegree();
                            }
                        }
                    }
                }
                // Caso particular 2, primera fila-ultima columna:
                if (i == 0 && j == m-1 && m > 1) {
                    if (torre.getHeight() >= matriz[i][j-1]) {
                        Vertex<Integer> torreIzquierda = new Vertex<Integer>(valueK(i, j-1, n, m));
                        graph.add(torreIzquierda);
                        if (graph.connect(torre, torreIzquierda)) {
                            torreIzquierda.upInwardDegree();
                        }
                    }
                    // Discriminador de caso brode "solo hay una fila".
                    if (n > 1) {
                        if (torre.getHeight() >= matriz[i+1][j]) {
                            Vertex<Integer> torreInferior = new Vertex<Integer>(valueK(i+1, j, n, m));
                            graph.add(torreInferior);
                            if (graph.connect(torre, torreInferior)) {
                                torreInferior.upInwardDegree();
                            }
                        }
                    }
                }
                // Caso general 2, primera columna:
                if (i > 0 && j == 0 && i != n-1) {
                    if (torre.getHeight() >= matriz[i-1][j]) {
                        Vertex<Integer> torreSuperior = new Vertex<Integer>(valueK(i-1, j, n, m));
                        graph.add(torreSuperior);
                        if (graph.connect(torre, torreSuperior)) {
                            torreSuperior.upInwardDegree();
                        }
                    }
                    // Discriminador de caso borde "solo hay una columna".
                    if (m > 1) {
                        if (torre.getHeight() >= matriz[i][j+1]) {
                            Vertex<Integer> torreDerecha = new Vertex<Integer>(valueK(i, j+1, n, m));
                            graph.add(torreDerecha);
                            if (graph.connect(torre, torreDerecha)) {
                                torreDerecha.upInwardDegree();
                            }
                        }
                    }
                    if (torre.getHeight() >= matriz[i+1][j]) {
                        Vertex<Integer> torreInferior = new Vertex<Integer>(valueK(i+1, j, n, m));
                        graph.add(torreInferior);
                        if (graph.connect(torre, torreInferior)) {
                            torreInferior.upInwardDegree();
                        }
                    }
                }
                // Caso particular 3, ultima fila-primera columna:
                if (i == n-1 && j == 0 && n > 1) {
                    if (torre.getHeight() >= matriz[i-1][j]) {
                        Vertex<Integer> torreSuperior = new Vertex<Integer>(valueK(i-1, j, n, m));
                        graph.add(torreSuperior);
                        if (graph.connect(torre, torreSuperior)) {
                            torreSuperior.upInwardDegree();
                        }
                    }
                    // Discriminador de caso borde "solo hay una columna".
                    if (m > 1) {
                        if (torre.getHeight() >= matriz[i][j+1]) {
                            Vertex<Integer> torreDerecha = new Vertex<Integer>(valueK(i, j+1, n, m));
                            graph.add(torreDerecha);
                            if (graph.connect(torre, torreDerecha)) {
                                torreDerecha.upInwardDegree();
                            }
                        }
                    }
                }
                // Caso general 3, ultima fila:
                if (i == n-1 && j > 0 && j != m-1) {
                    // Discriminador de caso borde "solo hay una fila"
                    if (n > 1) {
                        if (torre.getHeight() >= matriz[i-1][j]) {
                            Vertex<Integer> torreSuperior = new Vertex<Integer>(valueK(i-1, j, n, m));
                            graph.add(torreSuperior);
                            if (graph.connect(torre, torreSuperior)) {
                                torreSuperior.upInwardDegree();
                            }
                        }
                    }
                    if (torre.getHeight() >= matriz[i][j-1]) {
                        Vertex<Integer> torreIzquierda = new Vertex<Integer>(valueK(i, j-1, n, m));
                        graph.add(torreIzquierda);
                        if (graph.connect(torre, torreIzquierda)) {
                            torreIzquierda.upInwardDegree();
                        }
                    }
                    if (torre.getHeight() >= matriz[i][j+1]) {
                        Vertex<Integer> torreDerecha = new Vertex<Integer>(valueK(i, j+1, n, m));
                        graph.add(torreDerecha);
                        if (graph.connect(torre, torreDerecha)) {
                            torreDerecha.upInwardDegree();
                        }
                    }
                }
                // Caso particular 4, ultima posicion:
                if (i == n-1 && j == m-1 && n > 1 && m > 1) {
                    if (torre.getHeight() >= matriz[i-1][j]) {
                        Vertex<Integer> torreSuperior = new Vertex<Integer>(valueK(i-1, j, n, m));
                        graph.add(torreSuperior);
                        if (graph.connect(torre, torreSuperior)) {
                            torreSuperior.upInwardDegree();
                        }
                    }
                    if (torre.getHeight() >= matriz[i][j-1]) {
                        Vertex<Integer> torreIzquierda = new Vertex<Integer>(valueK(i, j-1, n, m));
                        graph.add(torreIzquierda);
                        if (graph.connect(torre, torreIzquierda)) {
                            torreIzquierda.upInwardDegree();
                        }
                    }
                }
                // Caso general 4, ultima columna:
                if (i > 0 && j == m-1 && i != n-1) {
                    if (torre.getHeight() >= matriz[i-1][j]) {
                        Vertex<Integer> torreSuperior = new Vertex<Integer>(valueK(i-1, j, n, m));
                        graph.add(torreSuperior);
                        if (graph.connect(torre, torreSuperior)) {
                            torreSuperior.upInwardDegree();
                        }
                    }
                    // Discriminador de caso borde "solo hay una columna".
                    if (m > 1) {
                        if (torre.getHeight() >= matriz[i][j-1]) {
                            Vertex<Integer> torreIzquierda = new Vertex<Integer>(valueK(i, j-1, n, m));
                            graph.add(torreIzquierda);
                            if (graph.connect(torre, torreIzquierda)) {
                                torreIzquierda.upInwardDegree();
                            }
                        }
                    }
                    if (torre.getHeight() >= matriz[i+1][j]) {
                        Vertex<Integer> torreInferior = new Vertex<Integer>(valueK(i+1, j, n, m));
                        graph.add(torreInferior);
                        if (graph.connect(torre, torreInferior)) {
                            torreInferior.upInwardDegree();
                        }
                    }
                }
                // Caso general 5, interior de la matriz:
                if (i > 0 && j > 0 && i < n-1 && j < m-1) {
                    if (torre.getHeight() >= matriz[i-1][j]) {
                        Vertex<Integer> torreSuperior = new Vertex<Integer>(valueK(i-1, j, n, m));
                        graph.add(torreSuperior);
                        if (graph.connect(torre, torreSuperior)) {
                            torreSuperior.upInwardDegree();
                        }
                    }
                    if (torre.getHeight() >= matriz[i][j-1]) {
                        Vertex<Integer> torreIzquierda = new Vertex<Integer>(valueK(i, j-1, n, m));
                        graph.add(torreIzquierda);
                        if (graph.connect(torre, torreIzquierda)) {
                            torreIzquierda.upInwardDegree();
                        }
                    }
                    if (torre.getHeight() >= matriz[i+1][j]) {
                        Vertex<Integer> torreInferior = new Vertex<Integer>(valueK(i+1, j, n, m));
                        graph.add(torreInferior);
                        if (graph.connect(torre, torreInferior)) {
                            torreInferior.upInwardDegree();
                        }
                    }
                    if (torre.getHeight() >= matriz[i][j+1]) {
                        Vertex<Integer> torreDerecha = new Vertex<Integer>(valueK(i, j+1, n, m));
                        graph.add(torreDerecha);
                        if (graph.connect(torre, torreDerecha)) {
                            torreDerecha.upInwardDegree();
                        }
                    }
                }
            }
        }
        return graph;
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
