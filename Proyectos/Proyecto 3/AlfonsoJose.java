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
        System.out.println(graph.size());
        printGraph(graph);
        List<List<Vertex<Integer>>> componentes = calculoDeComponentesFuertementeConexas(graph);
        generateGraphReducido(graph, componentes);
        OrdenTopologico(graph);
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

    private static Graph<Vertex<Integer>> createGraph(int [][] matriz) {
    // Creamos el grafo de vértices.
    Graph<Vertex<Integer>> graph = new AdjacencyListGraph<>();
    // Recolectamos las dimensiones de la matriz nxm.
    int n = matriz.length;
    int m = matriz[0].length;
    // Creamos un mapa para almacenar los vértices existentes por su clave única
    Map<Integer, Vertex<Integer>> vertexMap = new HashMap<>();
    // Verificamos la existencia de vértices y los creamos si es necesario
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            int k = valueK(i, j, n, m);
            System.out.println("Valor: " + k + " , Altura: " + matriz[i][j]);
            if (!vertexMap.containsKey(k)) {
                Vertex<Integer> torre = new Vertex<Integer>(k);
                torre.setHeight(matriz[i][j]);
                // Si se encuentra en uno de los bordes, contamos que el agua se puede derramar por el.
                if (i == 0 || i == n-1 || j == 0 || j == m-1) {
                    torre.setSpills(true);
                }
                graph.add(torre);
                vertexMap.put(k, torre);
            }
        }
    }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // Creamos un valor unico para el vertice del grafo
                int k = valueK(i, j, n, m);
                Vertex<Integer> torre = vertexMap.get(k);
                
                // Luego, conectamos el vertice a sus vertices adyacentes:
                // Caso particular 1, primera posicion:
                if (i == 0 && j == 0) {
                    // Discriminador de caso borde "solo hay una columna".
                    if (m > 1) {
                        if (torre.getHeight() >= matriz[i][j+1]) {
                            Vertex<Integer> torreDerecha = vertexMap.get(valueK(i, j+1, n, m));
                            if (graph.connect(torre, torreDerecha)) {
                                torreDerecha.upInwardDegree();
                            }
                        }
                    }

                    // Discriminador de caso brode "solo hay una fila".
                    if (n > 1) {
                        if (torre.getHeight() >= matriz[i+1][j]) {
                            Vertex<Integer> torreInferior = vertexMap.get(valueK(i+1, j, n, m));
                            if (graph.connect(torre, torreInferior)) {
                                torreInferior.upInwardDegree();
                            }
                        }
                    }
                }
                // Caso general 1, primera fila:
                if (i == 0 && j > 0 && j != m-1) {
                    if (torre.getHeight() >= matriz[i][j-1]) {
                        Vertex<Integer> torreIzquierda = vertexMap.get(valueK(i, j-1, n, m));
                        if (graph.connect(torre, torreIzquierda)) {
                            torreIzquierda.upInwardDegree();
                        }
                    }
                    if (torre.getHeight() >= matriz[i][j+1]) {
                        Vertex<Integer> torreDerecha = vertexMap.get(valueK(i, j+1, n, m));
                        
                        if (graph.connect(torre, torreDerecha)) {
                            torreDerecha.upInwardDegree();
                        }
                    }
                    // Discriminador de caso brode "solo hay una fila".
                    if (n > 1) {
                        if (torre.getHeight() >= matriz[i+1][j]) {
                            Vertex<Integer> torreInferior = vertexMap.get(valueK(i+1, j, n, m));
                            
                            if (graph.connect(torre, torreInferior)) {
                                torreInferior.upInwardDegree();
                            }
                        }
                    }
                }
                // Caso particular 2, primera fila-ultima columna:
                if (i == 0 && j == m-1 && m > 1) {
                    if (torre.getHeight() >= matriz[i][j-1]) {
                        Vertex<Integer> torreIzquierda = vertexMap.get(valueK(i, j-1, n, m));
                        
                        if (graph.connect(torre, torreIzquierda)) {
                            torreIzquierda.upInwardDegree();
                        }
                    }
                    // Discriminador de caso brode "solo hay una fila".
                    if (n > 1) {
                        if (torre.getHeight() >= matriz[i+1][j]) {
                            Vertex<Integer> torreInferior = vertexMap.get(valueK(i+1, j, n, m));
                            
                            if (graph.connect(torre, torreInferior)) {
                                torreInferior.upInwardDegree();
                            }
                        }
                    }
                }
                // Caso general 2, primera columna:
                if (i > 0 && j == 0 && i != n-1) {
                    if (torre.getHeight() >= matriz[i-1][j]) {
                        Vertex<Integer> torreSuperior = vertexMap.get(valueK(i-1, j, n, m));
                        
                        if (graph.connect(torre, torreSuperior)) {
                            torreSuperior.upInwardDegree();
                        }
                    }
                    // Discriminador de caso borde "solo hay una columna".
                    if (m > 1) {
                        if (torre.getHeight() >= matriz[i][j+1]) {
                            Vertex<Integer> torreDerecha = vertexMap.get(valueK(i, j+1, n, m));
                            
                            if (graph.connect(torre, torreDerecha)) {
                                torreDerecha.upInwardDegree();
                            }
                        }
                    }
                    if (torre.getHeight() >= matriz[i+1][j]) {
                        Vertex<Integer> torreInferior = vertexMap.get(valueK(i+1, j, n, m));
                    
                        if (graph.connect(torre, torreInferior)) {
                            torreInferior.upInwardDegree();
                        }
                    }
                }
                // Caso particular 3, ultima fila-primera columna:
                if (i == n-1 && j == 0 && n > 1) {
                    if (torre.getHeight() >= matriz[i-1][j]) {
                        Vertex<Integer> torreSuperior = vertexMap.get(valueK(i-1, j, n, m));
                        
                        if (graph.connect(torre, torreSuperior)) {
                            torreSuperior.upInwardDegree();
                        }
                    }
                    // Discriminador de caso borde "solo hay una columna".
                    if (m > 1) {
                        if (torre.getHeight() >= matriz[i][j+1]) {
                            Vertex<Integer> torreDerecha = vertexMap.get(valueK(i, j+1, n, m));
                            
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
                            Vertex<Integer> torreSuperior = vertexMap.get(valueK(i-1, j, n, m));
                            
                            if (graph.connect(torre, torreSuperior)) {
                                torreSuperior.upInwardDegree();
                            }
                        }
                    }
                    if (torre.getHeight() >= matriz[i][j-1]) {
                        Vertex<Integer> torreIzquierda = vertexMap.get(valueK(i, j-1, n, m));
                        
                        if (graph.connect(torre, torreIzquierda)) {
                            torreIzquierda.upInwardDegree();
                        }
                    }
                    if (torre.getHeight() >= matriz[i][j+1]) {
                        Vertex<Integer> torreDerecha = vertexMap.get(valueK(i, j+1, n, m));
                        
                        if (graph.connect(torre, torreDerecha)) {
                            torreDerecha.upInwardDegree();
                        }
                    }
                }
                // Caso particular 4, ultima posicion:
                if (i == n-1 && j == m-1 && n > 1 && m > 1) {
                    if (torre.getHeight() >= matriz[i-1][j]) {
                        Vertex<Integer> torreSuperior = vertexMap.get(valueK(i-1, j, n, m));
                        
                        if (graph.connect(torre, torreSuperior)) {
                            torreSuperior.upInwardDegree();
                        }
                    }
                    if (torre.getHeight() >= matriz[i][j-1]) {
                        Vertex<Integer> torreIzquierda = vertexMap.get(valueK(i, j-1, n, m));
                        
                        if (graph.connect(torre, torreIzquierda)) {
                            torreIzquierda.upInwardDegree();
                        }
                    }
                }
                // Caso general 4, ultima columna:
                if (i > 0 && j == m-1 && i != n-1) {
                    if (torre.getHeight() >= matriz[i-1][j]) {
                        Vertex<Integer> torreSuperior = vertexMap.get(valueK(i-1, j, n, m));
                        
                        if (graph.connect(torre, torreSuperior)) {
                            torreSuperior.upInwardDegree();
                        }
                    }
                    // Discriminador de caso borde "solo hay una columna".
                    if (m > 1) {
                        if (torre.getHeight() >= matriz[i][j-1]) {
                            Vertex<Integer> torreIzquierda = vertexMap.get(valueK(i, j-1, n, m));
                            
                            if (graph.connect(torre, torreIzquierda)) {
                                torreIzquierda.upInwardDegree();
                            }
                        }
                    }
                    if (torre.getHeight() >= matriz[i+1][j]) {
                        Vertex<Integer> torreInferior = vertexMap.get(valueK(i+1, j, n, m));
                        
                        if (graph.connect(torre, torreInferior)) {
                            torreInferior.upInwardDegree();
                        }
                    }
                }
                // Caso general 5, interior de la matriz:
                if (i > 0 && j > 0 && i < n-1 && j < m-1) {
                    if (torre.getHeight() >= matriz[i-1][j]) {
                        Vertex<Integer> torreSuperior = vertexMap.get(valueK(i-1, j, n, m));
                        
                        if (graph.connect(torre, torreSuperior)) {
                            torreSuperior.upInwardDegree();
                        }
                    }
                    if (torre.getHeight() >= matriz[i][j-1]) {
                        Vertex<Integer> torreIzquierda = vertexMap.get(valueK(i, j-1, n, m));
                        
                        if (graph.connect(torre, torreIzquierda)) {
                            torreIzquierda.upInwardDegree();
                        }
                    }
                    if (torre.getHeight() >= matriz[i+1][j]) {
                        Vertex<Integer> torreInferior = vertexMap.get(valueK(i+1, j, n, m));
                        
                        if (graph.connect(torre, torreInferior)) {
                            torreInferior.upInwardDegree();
                        }
                    }
                    if (torre.getHeight() >= matriz[i][j+1]) {
                        Vertex<Integer> torreDerecha = vertexMap.get(valueK(i, j+1, n, m));
                        if (graph.connect(torre, torreDerecha)) {
                            torreDerecha.upInwardDegree();
                        }
                    }
                
                }
                
            }
        }
        System.out.println(" ");
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
        //Creamos un valor unico que sera utilizado para los nuevos vertices.
        int v = graph.size() + 1;
        //Recorremos la lista de componentes fuertemente conexas.
        for (List<Vertex<Integer>> component : componentes) {
            //Si la componente tiene mas de un vertice entonces se crea un vertice representante.
            if (component.size() > 1) {
                //Se inicializa el vertice representante con el primer vertice de la componente.
                Vertex<Integer> representante = new Vertex<Integer>(v);
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
            System.out.println("Inward edges: ");
            for (Vertex<Integer> inward : graph.getInwardEdges(vertex)) {
                System.out.println(inward);
            }
            System.out.println("Outward edges: ");
            for (Vertex<Integer> outward : graph.getOutwardEdges(vertex)) {
                System.out.println(outward);
            }
            System.out.println();
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
        //Recorremos la lista de vertices ordenados topologicamente.
        for (Vertex<Integer> torre: listaVerticesOrdenTopologico){
            //Si la torre tiene grado de entrada 0, es decir, es un vertice fuente entonces se llama al 
            //metodo derramado().
            if (torre.getInwardDegree() != 0){
                //Se llama al metodo derramado() para la torre actual.
                derramado(graph, torre, torre);
                //Si la torre no se derrama
                if (!torre.isSpills()){
                    //Asignamos a una variable agua el maximo valor entero
                    int agua = Integer.MAX_VALUE;
                    //Recorremos todos los predecesores de la torre.
                    for (Vertex<Integer> predecesor : graph.getInwardEdges(torre)){
                        //Si la altura del predecesor es menor que el valor de agua entonces se actualiza el valor de agua.
                        if (agua < predecesor.getHeight()){
                            agua = predecesor.getHeight();
                        }
                    }
                    //Se actualiza el valor del contador, siendo este la diferencia entre el agua y la altura de la torre.
                    contador = contador + (agua - torre.getHeight());
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

