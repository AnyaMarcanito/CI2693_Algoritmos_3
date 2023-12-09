import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlfonsoJose {
    public static void main(String[] args) {
        String fileName = "atlantis.txt";
        int[][] matrix= readMatrixFromFile(fileName);
        
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
            // Creamos la matriz de alturas de cada torre:
            int [][] matriz = new int [fila][columna];
            // Volvemos a iterar, ahora si, sobre todas los valores del archivo.
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
    
   
}
