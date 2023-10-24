import java.util.Arrays;
import java.util.List;

public class main {
    public static void main(String[] args) {
        // Crear un grafo dirigido
        Graph<String> graph = new AdjacencyListGraph<>();

        // Agregar vértices al grafo
        graph.add("A");
        graph.add("B");
        graph.add("C");
        graph.add("D");
        graph.add("E");
        graph.add("A");
        System.out.println("----------------------------------------------------------------");

        // Conectar vértices en el grafo
        graph.connect("A", "B");
        graph.connect("A", "A");
        graph.connect("A", "C");
        graph.connect("A", "D");
        graph.connect("A", "E");
        graph.connect("B", "C");
        graph.connect("B", "A");
        graph.connect("B", "D");
        graph.connect("B", "E");
        graph.connect("C", "D");
        graph.connect("D", "E");
        graph.connect("E", "A");
        graph.connect("E", "G");
        graph.connect("G", "B");
        graph.connect("E", "E");
        System.out.println("----------------------------------------------------------------");

        // Desconectar vertices en el grafo
        graph.disconnect("A", "C");
        graph.disconnect("A", "E");
        graph.disconnect("G", "E");
        graph.disconnect("C", "E");
        System.out.println("----------------------------------------------------------------");

        // Ver si un vertice esta en el grafo
        graph.contains("F");
        graph.contains("D");
        System.out.println("----------------------------------------------------------------");

        // Ver arcos entrantes y salientes
        List<String> allVerticesInE = graph.getInwardEdges("E");
        List<String> allVerticesOutA = graph.getOutwardEdges("A");
        System.out.println("Vertices con arcos hacia E: " + allVerticesInE);
        System.out.println("Vertices con arcos desde A: " + allVerticesOutA);
        List<String> allVerticesWithB = graph.getVerticesConnectedTo("B");
        System.out.println("Vertices relacionados a B: " + allVerticesWithB);
        List<String> allVerticesOutG = graph.getOutwardEdges("G");
        System.out.println("Vertices con arcos desde G: " + allVerticesOutG);
        List<String> allVerticesInG = graph.getInwardEdges("G");
        System.out.println("Vertices con arcos hacia G: " + allVerticesInG);
        System.out.println("----------------------------------------------------------------");

        // Ver el tamano del grafo
        graph.size();
        System.out.println("----------------------------------------------------------------");

        // Obtener todos los vértices del grafo
        List<String> allVertices = graph.getAllVertices();
        System.out.println("Todos los vértices: " + allVertices);

        System.out.println("----------------------------------------------------------------");


        // Crear un subgrafo a partir de un conjunto de vértices
        List<String> subgraphVertices = Arrays.asList("A", "B", "C");
        Graph<String> subgraph = graph.subgraph(subgraphVertices);
        System.out.println("Subgrafo de tamano: " + subgraph.size());
        List<String> allVerticesSub = subgraph.getAllVertices();
        System.out.println("Todos los vértices: " + allVerticesSub);
        List<String> allVerticesWithA = subgraph.getVerticesConnectedTo("A");
        System.out.println("Vertices relacionados a A: " + allVerticesWithA);
        List<String> allVerticesWithC = subgraph.getVerticesConnectedTo("C");
        System.out.println("Vertices relacionados a C: " + allVerticesWithC);
        allVerticesWithB = subgraph.getVerticesConnectedTo("B");
        System.out.println("Vertices relacionados a B: " + allVerticesWithB);

        // Eliminar un vértice y sus conexiones del grafo
        boolean removed = graph.remove("D");
        System.out.println("Vértice eliminado: " + removed);
        removed = graph.remove("T");
        System.out.println("Vértice eliminado: " + removed);
        System.out.println("Grafo después de eliminar el vértice: " + graph.getAllVertices());
    }
}