import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlfonsoJose {
    public static void main(String[] args) {
        String fileName = "atlantis.txt";
        int[][] matrix= readMatrixFromFile(fileName);
        Graph<Vertex<Integer>> graph = createGraph(matrix);
        List<List<Vertex<Integer>>> componentes = calculoDeComponentesFuertementeConexas(graph);
        /*for (List<Vertex<Integer>> component : componentes) {
            System.out.println("Componente: ");
            for (Vertex<Integer> vertex : component) {
                System.out.println(vertex);
            }
            System.out.println();
        }*/
        generateGraphReducido(graph, componentes, matrix);
        //System.out.println(graph.size());
        printGraph(graph);
        OrdenTopologico(graph);
        for (Vertex<Integer> vertex : graph.getAllVertices()) {
            System.out.println("Vertice " + vertex.getValue() + " tiene f: " + vertex.getF());
        }
        propagacionAgua(graph);
                               
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
        return (i*n*m)+j;
    }

    /**
     * Genera un grafo dirigido a partir de una matriz.
     *
     * @param matriz la matriz de alturas de las torres de la ciudad.
     * @return el grafo construido a partir de la matriz de alturas, 
     *         donde las conexiones se establecen entre vertices adyacentes
     *         en la matriz (arriba, abajo, izquierda, derecha) y siempre
     *         en direccion de los vertices de mayor altura a los de menor 
     *         altura.
     */
    private static Graph<Vertex<Integer>> createGraph(int [][] matriz) {
        // Creamos el grafo de vértices.
        Graph<Vertex<Integer>> graph = new AdjacencyListGraph<>();
        // Recolectamos las dimensiones de la matriz nxm.
        int n = matriz.length;
        int m = matriz[0].length;
        // Creamos un mapa para almacenar los vértices existentes por su clave única
        Map<Integer, Vertex<Integer>> vertexMap = new HashMap<>();
        // Recorremos la matriz de alturas de las torres:
        for (int i = 0; i < n; i++) {
            // Recorremos las columnas:
            for (int j = 0; j < m; j++) {
                // Creamos un valor unico para el vertice del grafo
                int k = valueK(i, j, n, m);
                // Si el vertice no existe, lo creamos y lo agregamos al grafo.
                if (!vertexMap.containsKey(k)) {
                    // Creamos el vertice:
                    Vertex<Integer> torre = new Vertex<Integer>(k);
                    // Le asignamos la altura de la torre:
                    torre.setHeight(matriz[i][j]);
                    // Si se encuentra en uno de los bordes, contamos que el agua se puede derramar por el.
                    if (i == 0 || i == n-1 || j == 0 || j == m-1) {
                        torre.setSpills(true);
                    }
                    // Agregamos el vertice al grafo y al mapa de vertices.
                    graph.add(torre);
                    vertexMap.put(k, torre);
                }
            }
        }
        // Recorremos la matriz de alturas de las torres:
        for (int i = 0; i < n; i++) {
            // Recorremos las columnas:
            for (int j = 0; j < m; j++) {
                // Calculamos el valor unico que identifica al vertice en el que estamos:
                int k = valueK(i, j, n, m);
                // Obtenemos el vertice del mapa de vertices:
                Vertex<Integer> torre = vertexMap.get(k);
                
            // Luego, conectamos el vertice a sus vertices adyacentes:

                // Caso particular 1, primera posicion:
                if (i == 0 && j == 0) {
                    // Discriminador de caso borde "solo hay una columna".
                    if (m > 1) {
                        // Si la torre es mayor o igual a la torre de la derecha, entonces se conectan.
                        if (torre.getHeight() >= matriz[i][j+1]) {
                            // Obtenemos el vertice de la derecha:
                            Vertex<Integer> torreDerecha = vertexMap.get(valueK(i, j+1, n, m));
                            // Conectamos el vertice actual con el vertice de la derecha:
                            if (graph.connect(torre, torreDerecha)) {
                                // Aumentamos el grado de entrada del vertice de la derecha:
                                torreDerecha.upInwardDegree();
                            }
                        }
                    }

                    // Discriminador de caso brode "solo hay una fila".
                    if (n > 1) {
                        // Si la torre es mayor o igual a la torre de abajo, entonces se conectan.
                        if (torre.getHeight() >= matriz[i+1][j]) {
                            // Obtenemos el vertice de abajo:
                            Vertex<Integer> torreInferior = vertexMap.get(valueK(i+1, j, n, m));
                            // Conectamos el vertice actual con el vertice de abajo:
                            if (graph.connect(torre, torreInferior)) {
                                // Aumentamos el grado de entrada del vertice de abajo:
                                torreInferior.upInwardDegree();
                            }
                        }
                    }
                }
                // Caso general 1, primera fila:
                if (i == 0 && j > 0 && j != m-1) {

                    if (torre.getHeight() >= matriz[i][j-1]) {
                        // Obtenemos el vertice de la izquierda:
                        Vertex<Integer> torreIzquierda = vertexMap.get(valueK(i, j-1, n, m));
                        // Conectamos el vertice actual con el vertice de la izquierda:
                        if (graph.connect(torre, torreIzquierda)) {
                            // Aumentamos el grado de entrada del vertice de la izquierda:
                            torreIzquierda.upInwardDegree();
                        }
                    }
                    if (torre.getHeight() >= matriz[i][j+1]) {
                        // Obtenemos el vertice de la derecha:
                        Vertex<Integer> torreDerecha = vertexMap.get(valueK(i, j+1, n, m));
                        // Conectamos el vertice actual con el vertice de la derecha:                        
                        if (graph.connect(torre, torreDerecha)) {
                            // Aumentamos el grado de entrada del vertice de la derecha:
                            torreDerecha.upInwardDegree();
                        }
                    }
                    if (n > 1) {
                        // Si la torre es mayor o igual a la torre de abajo, entonces se conectan.
                        if (torre.getHeight() >= matriz[i+1][j]) {
                            // Obtenemos el vertice de abajo:
                            Vertex<Integer> torreInferior = vertexMap.get(valueK(i+1, j, n, m));
                            // Conectamos el vertice actual con el vertice de abajo:
                            if (graph.connect(torre, torreInferior)) {
                                // Aumentamos el grado de entrada del vertice de abajo:
                                torreInferior.upInwardDegree();
                            }
                        }
                    }
                }
                // Caso particular 2, primera fila-ultima columna:
                if (i == 0 && j == m-1 && m > 1) {

                    if (torre.getHeight() >= matriz[i][j-1]) {
                        // Obtenemos el vertice de la izquierda:
                        Vertex<Integer> torreIzquierda = vertexMap.get(valueK(i, j-1, n, m));
                        // Conectamos el vertice actual con el vertice de la izquierda:
                        if (graph.connect(torre, torreIzquierda)) {
                            // Aumentamos el grado de entrada del vertice de la izquierda:
                            torreIzquierda.upInwardDegree();
                        }
                    }
                    // Discriminador de caso brode "solo hay una fila".
                    if (n > 1) {
                        if (torre.getHeight() >= matriz[i+1][j]) {
                            // Obtenemos el vertice de abajo:
                            Vertex<Integer> torreInferior = vertexMap.get(valueK(i+1, j, n, m));
                            // Conectamos el vertice actual con el vertice de abajo:
                            if (graph.connect(torre, torreInferior)) {
                                // Aumentamos el grado de entrada del vertice de abajo:
                                torreInferior.upInwardDegree();
                            }
                        }
                    }
                }
                // Caso general 2, primera columna:
                if (i > 0 && j == 0 && i != n-1) {
                    if (torre.getHeight() >= matriz[i-1][j]) {
                        // Obtenemos el vertice de arriba:
                        Vertex<Integer> torreSuperior = vertexMap.get(valueK(i-1, j, n, m));
                        // Conectamos el vertice actual con el vertice de arriba:
                        if (graph.connect(torre, torreSuperior)) {
                            // Aumentamos el grado de entrada del vertice de arriba:
                            torreSuperior.upInwardDegree();
                        }
                    }
                    // Discriminador de caso borde "solo hay una columna".
                    if (m > 1) {
                        if (torre.getHeight() >= matriz[i][j+1]) {
                            // Obtenemos el vertice de la derecha:
                            Vertex<Integer> torreDerecha = vertexMap.get(valueK(i, j+1, n, m));
                            // Conectamos el vertice actual con el vertice de la derecha:
                            if (graph.connect(torre, torreDerecha)) {
                                // Aumentamos el grado de entrada del vertice de la derecha:
                                torreDerecha.upInwardDegree();
                            }
                        }
                    }
                    if (torre.getHeight() >= matriz[i+1][j]) {
                        // Obtenemos el vertice de abajo:
                        Vertex<Integer> torreInferior = vertexMap.get(valueK(i+1, j, n, m));
                        // Conectamos el vertice actual con el vertice de abajo:
                        if (graph.connect(torre, torreInferior)) {
                            // Aumentamos el grado de entrada del vertice de abajo:
                            torreInferior.upInwardDegree();
                        }
                    }
                }
                // Caso particular 3, ultima fila-primera columna:
                if (i == n-1 && j == 0 && n > 1) {
                    if (torre.getHeight() >= matriz[i-1][j]) {
                        // Obtenemos el vertice de arriba:
                        Vertex<Integer> torreSuperior = vertexMap.get(valueK(i-1, j, n, m));
                        // Conectamos el vertice actual con el vertice de arriba:
                        if (graph.connect(torre, torreSuperior)) {
                            // Aumentamos el grado de entrada del vertice de arriba:
                            torreSuperior.upInwardDegree();
                        }
                    }
                    // Discriminador de caso borde "solo hay una columna".
                    if (m > 1) {
                        if (torre.getHeight() >= matriz[i][j+1]) {
                            // Obtenemos el vertice de la derecha:
                            Vertex<Integer> torreDerecha = vertexMap.get(valueK(i, j+1, n, m));
                            // Conectamos el vertice actual con el vertice de la derecha:
                            if (graph.connect(torre, torreDerecha)) {
                                // Aumentamos el grado de entrada del vertice de la derecha:
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
                            // Obtenemos el vertice de arriba:
                            Vertex<Integer> torreSuperior = vertexMap.get(valueK(i-1, j, n, m));
                            // Conectamos el vertice actual con el vertice de arriba:                            
                            if (graph.connect(torre, torreSuperior)) {
                                // Aumentamos el grado de entrada del vertice de arriba:
                                torreSuperior.upInwardDegree();
                            }
                        }
                    }
                    if (torre.getHeight() >= matriz[i][j-1]) {
                        // Obtenemos el vertice de la izquierda:
                        Vertex<Integer> torreIzquierda = vertexMap.get(valueK(i, j-1, n, m));
                        // Conectamos el vertice actual con el vertice de la izquierda:
                        if (graph.connect(torre, torreIzquierda)) {
                            // Aumentamos el grado de entrada del vertice de la izquierda:
                            torreIzquierda.upInwardDegree();
                        }
                    }
                    if (torre.getHeight() >= matriz[i][j+1]) {
                        // Obtenemos el vertice de la derecha:
                        Vertex<Integer> torreDerecha = vertexMap.get(valueK(i, j+1, n, m));
                        // Conectamos el vertice actual con el vertice de la derecha:
                        if (graph.connect(torre, torreDerecha)) {
                            // Aumentamos el grado de entrada del vertice de la derecha:
                            torreDerecha.upInwardDegree();
                        }
                    }
                }
                // Caso particular 4, ultima posicion:
                if (i == n-1 && j == m-1 && n > 1 && m > 1) {
                    if (torre.getHeight() >= matriz[i-1][j]) {
                        // Obtenemos el vertice de arriba:
                        Vertex<Integer> torreSuperior = vertexMap.get(valueK(i-1, j, n, m));
                        // Conectamos el vertice actual con el vertice de arriba:
                        if (graph.connect(torre, torreSuperior)) {
                            // Aumentamos el grado de entrada del vertice de arriba:
                            torreSuperior.upInwardDegree();
                        }
                    }
                    if (torre.getHeight() >= matriz[i][j-1]) {
                        // Obtenemos el vertice de la izquierda:
                        Vertex<Integer> torreIzquierda = vertexMap.get(valueK(i, j-1, n, m));
                        // Conectamos el vertice actual con el vertice de la izquierda:
                        if (graph.connect(torre, torreIzquierda)) {
                            // Aumentamos el grado de entrada del vertice de la izquierda:
                            torreIzquierda.upInwardDegree();
                        }
                    }
                }
                // Caso general 4, ultima columna:
                if (i > 0 && j == m-1 && i != n-1) {
                    if (torre.getHeight() >= matriz[i-1][j]) {
                        // Obtenemos el vertice de arriba:
                        Vertex<Integer> torreSuperior = vertexMap.get(valueK(i-1, j, n, m));
                        // Conectamos el vertice actual con el vertice de arriba:
                        if (graph.connect(torre, torreSuperior)) {
                            // Aumentamos el grado de entrada del vertice de arriba:
                            torreSuperior.upInwardDegree();
                        }
                    }
                    // Discriminador de caso borde "solo hay una columna".
                    if (m > 1) {
                        if (torre.getHeight() >= matriz[i][j-1]) {
                            // Obtenemos el vertice de la izquierda:
                            Vertex<Integer> torreIzquierda = vertexMap.get(valueK(i, j-1, n, m));
                            // Conectamos el vertice actual con el vertice de la izquierda:
                            if (graph.connect(torre, torreIzquierda)) {
                                // Aumentamos el grado de entrada del vertice de la izquierda:
                                torreIzquierda.upInwardDegree();
                            }
                        }
                    }
                    if (torre.getHeight() >= matriz[i+1][j]) {
                        // Obtenemos el vertice de abajo:
                        Vertex<Integer> torreInferior = vertexMap.get(valueK(i+1, j, n, m));
                        // Conectamos el vertice actual con el vertice de abajo:
                        if (graph.connect(torre, torreInferior)) {
                            // Aumentamos el grado de entrada del vertice de abajo:
                            torreInferior.upInwardDegree();
                        }
                    }
                }
                // Caso general 5, interior de la matriz:
                if (i > 0 && j > 0 && i < n-1 && j < m-1) {
                    if (torre.getHeight() >= matriz[i-1][j]) {
                        // Obtenemos el vertice de arriba:
                        Vertex<Integer> torreSuperior = vertexMap.get(valueK(i-1, j, n, m));
                        // Conectamos el vertice actual con el vertice de arriba:
                        if (graph.connect(torre, torreSuperior)) {
                            // Aumentamos el grado de entrada del vertice de arriba:
                            torreSuperior.upInwardDegree();
                        }
                    }
                    if (torre.getHeight() >= matriz[i][j-1]) {
                        // Obtenemos el vertice de la izquierda:
                        Vertex<Integer> torreIzquierda = vertexMap.get(valueK(i, j-1, n, m));
                        // Conectamos el vertice actual con el vertice de la izquierda:
                        if (graph.connect(torre, torreIzquierda)) {
                            // Aumentamos el grado de entrada del vertice de la izquierda:
                            torreIzquierda.upInwardDegree();
                        }
                    }
                    if (torre.getHeight() >= matriz[i+1][j]) {
                        // Obtenemos el vertice de abajo:
                        Vertex<Integer> torreInferior = vertexMap.get(valueK(i+1, j, n, m));
                        // Conectamos el vertice actual con el vertice de abajo:
                        if (graph.connect(torre, torreInferior)) {
                            // Aumentamos el grado de entrada del vertice de abajo:
                            torreInferior.upInwardDegree();
                        }
                    }
                    if (torre.getHeight() >= matriz[i][j+1]) {
                        // Obtenemos el vertice de la derecha:
                        Vertex<Integer> torreDerecha = vertexMap.get(valueK(i, j+1, n, m));
                        // Conectamos el vertice actual con el vertice de la derecha:
                        if (graph.connect(torre, torreDerecha)) {
                            // Aumentamos el grado de entrada del vertice de la derecha:
                            torreDerecha.upInwardDegree();
                        }
                    }
                
                }
                
            }
        }
        System.out.println(" ");
        // Retornamos el grafo creado:
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
        // Recorremos cada una de las componentes creadas
        for (List<Vertex<Integer>> component : components) {
            // 
            for (Vertex<Integer> vertex : component) {
                vertex.setTamanoCFC(component.size());
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

    public static void generateGraphReducido(Graph<Vertex<Integer>> graph, List<List<Vertex<Integer>>> componentes, int[][] matriz) {
        
        // Recolectamos las dimensiones de la matriz nxm.
        int n = matriz.length;
        int m = matriz[0].length;

        //Creamos un valor unico que sera utilizado para los nuevos vertices.
        int v = ((n*n*m)+m) + 1;
        //Recorremos la lista de componentes fuertemente conexas.
        for (List<Vertex<Integer>> component : componentes) {
            //Si la componente tiene mas de un vertice entonces se crea un vertice representante.
            if (component.size() > 1) {
                //Se inicializa el vertice representante con el primer vertice de la componente.
                Vertex<Integer> representante = new Vertex<Integer>(v);
                representante.setHeight(component.get(0).getHeight());
                representante.setTamanoCFC(component.size());
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
                            if (graph.connect(vecino, representante)) {
                                //Se aumenta el grado de entrada del representante.
                                representante.upInwardDegree();
                            }
                        }
                    }
                    //Se elimina el vertice vertex del grafo.
                    graph.remove(vertex);
                    //Aumentamos el valor del proximo vertice.
                    v++;
                }
            }
        }
    }
    
    static int contador = 0;

    public static void OrdenTopologico(Graph<Vertex<Integer>> graph){
        contador = graph.size();
        List<Vertex<Integer>> visitados = new ArrayList<>();
        for (Vertex<Integer> vertex : graph.getAllVertices()) {
            if(!visitados.contains(vertex)){
                dfsTopologico(graph, vertex, visitados);
            }
        }
    }

    private static void dfsTopologico(Graph<Vertex<Integer>> graph, Vertex<Integer> vertex, List<Vertex<Integer>> visitados){
        visitados.add(vertex);
        for (Vertex<Integer> vecino : graph.getOutwardEdges(vertex)) {
            if (!visitados.contains(vecino)) {
                dfsTopologico(graph, vecino, visitados);
            }
        }
        contador = contador -1;
        vertex.setF(contador);
    }

    private static void printGraph(Graph<Vertex<Integer>> graph) {
        for (Vertex<Integer> vertex : graph.getAllVertices()) {
            System.out.println(vertex);
            /*System.out.println("Inward edges: ");
            for (Vertex<Integer> inward : graph.getInwardEdges(vertex)) {
                System.out.println(inward);
            }
            System.out.println("Outward edges: ");
            for (Vertex<Integer> outward : graph.getOutwardEdges(vertex)) {
                System.out.println(outward);
            }
            System.out.println();*/
        }
    }

    private static void propagacionAgua(Graph<Vertex<Integer>> graph){
        //Inicializamos un contador para el agua que se derrama.
        contador = 0;
        //Creamos una lista con todos los vertices del grafo:
        List<Vertex<Integer>> listaVerticesOrdenTopologico = new ArrayList<>();
        //Recorremos todos los vertices del grafo y los agregamos a la lista.
        for (Vertex<Integer> vertex : graph.getAllVertices()) {
            listaVerticesOrdenTopologico.add(vertex);
        }
        //Ordenamos la lista de vertices por su atributo f.
        Collections.sort(listaVerticesOrdenTopologico, new Comparator<Vertex<Integer>>() {
            @Override
            public int compare(Vertex<Integer> o1, Vertex<Integer> o2) {
                return o1.getF() - o2.getF();
            }
        }); 
        System.out.println("Lista de vertices ordenados topologicamente: ");
        for (Vertex<Integer> vertex : listaVerticesOrdenTopologico) {
            System.out.println(vertex);
        }

        //Recorremos la lista de vertices ordenados topologicamente.
        for (Vertex<Integer> torre: listaVerticesOrdenTopologico){
            //Si la torre tiene grado de entrada 0, es decir, es un vertice fuente entonces se llama al 
            //metodo derramado().
            System.out.println("Torre: " + torre.getValue() + " va a ser revisada");
            if (torre.getInwardDegree() != 0 && !torre.isSpills()){
                System.out.println("Torre: " + torre.getValue() + " no es fuente");
                //Se llama al metodo derramado() para la torre actual.
                derramado(graph, torre, torre);
                //Si la torre no se derrama
                if (!torre.isSpills()){
                    System.out.println("Estamos entrando al bucle");
                    //Asignamos a una variable agua el maximo valor entero
                    int agua = Integer.MAX_VALUE;
                    //Recorremos todos los predecesores de la torre.
                    for (Vertex<Integer> predecesor : graph.getInwardEdges(torre)){
                        System.out.println("Predecesor: " + predecesor.getValue() + " altura: " + predecesor.getHeight()+ " agua: " + agua);
                        //Si la altura del predecesor es menor que el valor de agua entonces se actualiza el valor de agua.
                        if (agua > predecesor.getHeight()){
                            agua = predecesor.getHeight();
                            System.out.println("El agua es: " + agua);
                        }
                    }
                    //Se actualiza el valor del contador, siendo este la diferencia entre el agua y la altura de la torre.
                    System.out.println("El vertice actual es:"+ torre);
                    contador = contador + ((agua - torre.getHeight()) * torre.getTamanoCFC());
                    System.out.println("El contador es: " + contador);
                    //Se actualiza la altura de la torre.
                    torre.setHeight(agua);
                }
            }
        }
        System.out.println("La cantidad de agua necesaria es: " + contador);

    }

    private static void derramado(Graph<Vertex<Integer>> graph, Vertex<Integer> torre1, Vertex<Integer> torre2){
        //Si la torre1 se derrama entonces se cambia el atributo spills de la torre2 a true.
        if (torre1.isSpills()) {
            //Se cambia el atributo spills de la torre2 a true.
            torre2.setSpills(true);
            return;
        }
        //Si la torre1 no se derrama entonces se recorren todos los sucesores de la torre1.
        for (Vertex<Integer> sucesor : graph.getOutwardEdges(torre1)) {
            //Se llama al metodo derramado() para el sucesor actual.
            derramado(graph, sucesor, torre2);
        }
    }
    
}

