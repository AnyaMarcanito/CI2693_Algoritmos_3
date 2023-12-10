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
    //Atributo contador para el orden topologico y para la cantidad de agua necesaria.
    static int contador = 0;

    /**
     * El método main es el punto de entrada del programa.
     * Lee una matriz de alturas desde un archivo .txt, crea un grafo dirigido basado en las alturas de las torres de la ciudad,
     * calcula los componentes fuertemente conectados del grafo, genera un grafo reducido basado en los componentes fuertemente conectados,
     * genera un orden topológico para los vértices del grafo reducido y calcula la cantidad de agua necesaria para inundar la ciudad
     * utilizando el orden topológico y el método propagaciónAgua().
     *
     * @param args los argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        String fileName = "atlantis.txt";
        // Generamos la matriz de alturas a partir del archivo atlantis.txt
        int[][] matrix= readMatrixFromFile(fileName);
        // Creamos el grafo dirigido que modela el problema en funcion de las alturas de las torres de la ciudad.
        Graph<Vertex<Integer>> graph = createGraph(matrix);
        // Calculamos las componentes fuertemente conexas del grafo.
        List<List<Vertex<Integer>>> componentes = calculoDeComponentesFuertementeConexas(graph);
        // Convertimos el grafo original en uno reducido a partir de las componenetes fuertemente conexas.
        generateGraphReducido(graph, componentes, matrix);
        // Generamos un orden topologico para los vertices del grafo reducido.
        OrdenTopologico(graph);
        // Usando el orden topologico anterior, calculamos la cantidad de agua que se requiere para inundar la ciudad
        // a traves del metodo propagacionAgua()
        propagacionAgua(graph);                     
    }
    
    /**
     * Lee una matriz desde un archivo .txt y genera una matriz de enteros.
     *
     * @param fileName nombre del archivo que contiene la matriz.
     * @return matriz construida a partir del archivo, generada como un arreglo de 
     *         arreglos de enteros.
     */
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
        // Retornamos la matriz de alturas de las torres:
        return matriz;
    }

    /**
     * Calcula el valor de K basándose en los índices y dimensiones dados.
     * 
     * @param i El índice de la fila.
     * @param j El índice de columna.
     * @param n El número de filas.
     * @param m El número de columnas.
     * @return El valor calculado de K.
     */
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
            // Recorremos cada uno de los vértices de la componente
            for (Vertex<Integer> vertex : component) {
                // Asignamos a cada vértice de la componente el tamaño de la componente
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

    /**
     * Genera un grafo reducido fusionando componentes fuertemente conectados en vértices representativos.
     * 
     * @param graph el grafo original
     * @param componentes la lista de componentes fuertemente conectados
     * @param matriz la matriz que representa el grafo
     */
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
                // Se inicializa un vertice representante para fusionar todos los vertices de una componente en uno solo.
                Vertex<Integer> representante = new Vertex<Integer>(v);
                // Se le asigna al representante la altura del primer vertice de la componente.
                representante.setHeight(component.get(0).getHeight());
                // Se le asigna al representante el tamaño de la componente.
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
                    //Se recorren todos los sucesores del vertice vertex.
                    for (Vertex<Integer> vecino : graph.getOutwardEdges(vertex)) {
                        //Si el vecino no pertenece a la componente entonces se conecta al representante.
                        if (!component.contains(vecino)) {
                            //Se conecta el representante con el vecino.
                            graph.connect(representante, vecino);
                        }
                    }
                    //Se recorren todos los predecesores del vertice vertex.
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
                    //Aumentamos el valor del proximo vertice representante.
                    v++;
                }
            }
        }
    }
    

    public static void OrdenTopologico(Graph<Vertex<Integer>> graph){
        // 
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

