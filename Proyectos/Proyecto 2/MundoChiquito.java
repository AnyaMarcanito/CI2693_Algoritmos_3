import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MundoChiquito {
    
    public static void main(String[] args) {
        //Creamos un grafo de tipo AdjacencyListGraph.
        Graph<CartaMostro> graph = new AdjacencyListGraph<>();

        //Leemos el archivo deck.csv y agregamos las cartas mostro al grafo.
        try {
            //Creamos un objeto de tipo File y Scanner para leer el archivo deck.csv.
            File inputFile = new File("deck.csv");
            Scanner scanner = new Scanner(inputFile);
            //Usamos un boolean para saltar la primera linea del archivo.
            boolean firstLine = true;
            //Iteramos sobre cada linea del archivo.
            while (scanner.hasNextLine()) {
                //Saltamos la primera linea.
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                //Leemos la linea y la separamos por comas.
                String line = scanner.nextLine();
                String[] data = line.split(",");
                //Creamos un objeto de tipo CartaMostro con los datos de la linea.
                String nombre = data[0];
                int nivel = Integer.parseInt(data[1]);
                String atributo = data[2];
                int poder = Integer.parseInt(data[3]);
                CartaMostro carta = new CartaMostro(nombre, nivel, poder, atributo);
                //Agregamos la carta al grafo.
                graph.add(carta);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            //Si no se encuentra el archivo input.txt se imprime un mensaje de error.
            System.out.println("No se pudo encontrar el archivo deck.csv");
            return;
        }
    }
}
