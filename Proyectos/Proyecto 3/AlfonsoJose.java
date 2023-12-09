import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlfonsoJose {
    public static void main(String[] args) {
        String fileName = "atlantis.txt";
        Graph<Vertex<Integer>> graph = readMatrixFromFile(fileName);
        
    }
    
    private static Graph<Vertex<Integer>> readMatrixFromFile(String fileName) {
        // Creamos un grafo vacío de tipo AdjacencyListGraph con vértices Vertex de tipo Integer:
        Graph<Vertex<Integer>> graph = new AdjacencyListGraph<>();
    
        // Leemos el archivo y agregamos los vértices al grafo:
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            // Leemos la primera línea del archivo:
            String line;
            // Definimos contadores para saber el número de filas y columnas de la matriz de entrada:
            int fila = 0;
            int columna = 0;
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
            // Creamos la matriz de alturas de cada torre:
            int [][] = new int [fila][columna];

                
                // Definimos un contador para saber en qué columnaumna estamos:
                // Iteramos sobre cada valor de la línea:
                for (String value : values) {
                    // Convertimos el valor a entero:
                    int torre = Integer.parseInt(value);
                    // Creamos un nuevo vértice con el valor de la torre y asignamos la altura:
                    Vertex<Integer> vertex = new Vertex<>(torre);
                    // Asignamos la altura del vértice:
                    vertex.setHeight(torre);
                    // Agregamos el vértice al grafo:
                    graph.add(vertex);
                    // Conectamos el vértice con sus vecinos:

                    // Si no estamos en la primera fila, conectamos el vértice con el de arriba:
                    if (fila > 0) {
                        // El vértice de arriba está en la posición (fila - 1) * values.length + columna:
                        Vertex<Integer> vertexArriba = verticesList.get((fila - 1) * values.length + columna);
                        // Conectamos el vértice con el de arriba:
                        // Y podemos conectarlos porque ya pasamos por el vértice de arriba, es decir, este vertice ya existe en el grafo:
                        graph.connect(vertexArriba, vertex);
                    }
                    // Si no estamos en la primera columnaumna, conectamos el vértice con el de la izquierda:
                    if (columna > 0) {
                        // El vértice de la izquierda está en la posición fila * values.length + (columna - 1):
                        Vertex<Integer> vertexIzquierdo = verticesList.get(fila * values.length + (columna - 1));
                        // Conectamos el vértice con el de la izquierda:
                        // Y podemos conectarlos porque ya pasamos por el vértice de la izquierda es decir, este vertice ya existe en el grafo:
                        graph.connect(vertexIzquierdo, vertex);
                    }
                    // Incrementamos el contador de columna:
                    columna++;
                }
                // Incrementamos el contador de fila:
                fila++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return graph;
    }
    
   
}
