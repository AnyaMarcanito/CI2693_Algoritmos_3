import java.util.Arrays;
import java.util.List;

public class main {
    public static void main(String[] args) {
        // Crear un grafo dirigido
        Graph<String> graph = new AdjacencyListGraph<>();

        // Agregar vértices al grafo
        System.out.println(graph.add("A"));
        System.out.println(graph.add("B"));
        System.out.println(graph.add("C"));
        System.out.println(graph.add("D"));
        System.out.println(graph.add("E"));
        System.out.println(graph.add("A"));
        System.out.println("----------------------------------------------------------------");

        // Conectar vértices en el grafo
        System.out.println(graph.connect("A", "B"));
        System.out.println(graph.connect("A", "A"));
        System.out.println(graph.connect("A", "C"));
        System.out.println(graph.connect("A", "D"));
        System.out.println(graph.connect("A", "E"));
        System.out.println(graph.connect("B", "C"));
        System.out.println(graph.connect("B", "A"));
        System.out.println(graph.connect("B", "D"));
        System.out.println(graph.connect("B", "E"));
        System.out.println(graph.connect("C", "D"));
        System.out.println(graph.connect("D", "E"));
        System.out.println(graph.connect("E", "A"));
        System.out.println(graph.connect("E", "G"));
        System.out.println(graph.connect("G", "B"));
        System.out.println(graph.connect("E", "E"));
        System.out.println("----------------------------------------------------------------");

        // Desconectar vertices en el grafo
        System.out.println(graph.disconnect("A", "C"));
        System.out.println(graph.disconnect("A", "E"));
        System.out.println(graph.disconnect("G", "E"));
        System.out.println(graph.disconnect("C", "E"));
        System.out.println("----------------------------------------------------------------");

        // Ver si un vertice esta en el grafo
        System.out.println(graph.contains("F"));
        System.out.println(graph.contains("D"));
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
        System.out.println(graph.size());
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