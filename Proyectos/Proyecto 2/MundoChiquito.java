import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MundoChiquito {

    /**
     * Dado el grafo de cartas mostro, imprime todas las ternas que se conectaron 
     * cumpliendo con las condiciones.
     * @param graph el grafo de cartas mostro
     */
    public static void ternaChiquita(Graph<CartaMostro> graph) {
        //Dado el grafo de cartas mostro, encuentra toda las ternas que cumplen con 
        //las condiciones.
        //Iteramos sobre cada carta mostro del grafo.
        for (CartaMostro mostro1 : graph.getAllVertices()) {
            //Iteramos sobre cada carta mostro conectada a la carta mostro actual.
            for (CartaMostro mostro2 : graph.getOutwardEdges(mostro1)) {
                //Iteramos sobre cada carta mostro conectada a la carta mostro anterior.
                for (CartaMostro mostro3 : graph.getOutwardEdges(mostro2)) {
                    //Imprimimos la terna de cartas mostro si esta existe.
                    System.out.println(mostro1.getNombre() + " " + mostro2.getNombre() + " " + mostro3.getNombre());
                }
            }
        }
    }

    /**
     * Compara dos cartas de monstro y devuelve verdadero si tienen exactamente una 
     * característica en común.
     * Las características comparadas son nivel, poder y atributo.
     * @param mostro1 la primera carta de monstro a comparar.
     * @param mostro2 la segunda carta de monstro a comparar.
     * @return verdadero si las cartas tienen exactamente una característica en común, 
     * falso de lo contrario.
     */
    public static boolean compartenUnaCaracteristica(CartaMostro mostro1, CartaMostro mostro2) {
        int caracteristicasEnComun = 0;
        if (mostro1.getNivel() == mostro2.getNivel()) caracteristicasEnComun++;
        if (mostro1.getPoder() == mostro2.getPoder()) caracteristicasEnComun++;
        if (mostro1.getAtributo().equals(mostro2.getAtributo())) caracteristicasEnComun++;
        return caracteristicasEnComun == 1;
    }
    
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
                // Saltamos la primera línea.
                if (firstLine) {
                    scanner.nextLine();
                    firstLine = false;
                    continue;
                }
                // Leemos la línea y la separamos por comas.
                String line = scanner.nextLine();
                String[] data = line.split(",");
                // Creamos un objeto de tipo CartaMostro con los datos de la línea.
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
        //Iteramos sobre cada carta mostro del grafo.
        for (CartaMostro mostro1 : graph.getAllVertices()) {
            //Iteramos otra vez sobre cada carta mostro del grafo para conectar las cartas 
            //mostro que comparten exactamente una característica.
            for (CartaMostro mostro2 : graph.getAllVertices()) {
                //Verificamos que las cartas mostro sean distintas y que compartan exactamente 
                //una característica con el metodo compartenUnaCaracteristica()
                if (mostro1 != mostro2 && compartenUnaCaracteristica(mostro1, mostro2)) {
                    //Conectamos las cartas mostro en ambas direcciones.
                    graph.connect(mostro1, mostro2);
                    graph.connect(mostro2, mostro1);
                }
            }
        }
        //Imprimimos las ternas de cartas mostro que cumplen con las condiciones con el metodo ternaChiquita().
        ternaChiquita(graph);
    }
}
