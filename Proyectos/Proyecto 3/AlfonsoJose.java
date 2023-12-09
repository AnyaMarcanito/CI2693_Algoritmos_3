import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
    
   
}
