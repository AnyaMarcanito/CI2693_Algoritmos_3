Universidad Simón Bolívar

Departamento de Computación y Tecnología de la Información.

CI–2693 – Laboratorio de Algoritmos y Estructuras III.

Septiembre–Diciembre 2023



Alejandro Zambrano 17-10684

Anya Marcano 19-10336
                                           
                                                       Proyecto I: Grafo
                                            Implementación con listas de adyacencias

                          
La implementación diseñada para este proyecto se basa en la creación de una clase 
AdjacencyListGraph<T> que implemente la interfaz Graph<T> teniendo como atributo 
principal una variable de tipo Map en la que se almacena un mapa de vértices a 
listas de vértices adyacentes, donde el tipo de la clave del mapa es T, que a su 
vez es el tipo de los vértices del grafo, mientras que el tipo de valor del mapa 
es List<T>, es decir, una lista de vértices adyacentes al vértice de la clave. 
Teniéndose como constructor de la clase la inicialización de la variable 
adjacencyMap con un nuevo objeto HashMap, el cual es una implementación de la 
interfaz Map que utiliza una tabla de hash para almacenar los datos.


El principal motivo por el cual se decidió optar por una implementación que 
utilizara objetos de tipo HashMap en lugar de considerar únicamente el uso de listas 
enlazadas tuvo que ver con la idea de aprovechar las complejidades de las operaciones
de insertar, eliminar y buscar elementos propias de las tablas de hash y con ello 
conseguir complejidades más eficientes en la medida de lo posible, idea que resultó 
en costos computacionales que varían desde complejidades constantes, lineales y 
cuadráticas como se explican a continuación:

                          
                                            Complejidades de los metodos implementados                         


➱Método Add:

Se basa en aprovechar el metodo .containsKey() de la clase HashMap implementada en Java, 
el cual tiene una complejidad constante, independientemente del tamaño del mapa considerado,
por lo tanto nuestro metodo contains termina teniendo una complejidad O(1) para el peor caso.


➱Método Connect:

Se basa en utilizar el metodo contains para comprobar si los vertices relacionados al arco 
que se desea crear existen dentro del grafo y de ser así, utiliza el metodo .get() de la clase 
HashMap y los metodos .contains() y .add() de la clase List de Java para agregar el nuevo arco 
en la lista de sucesores del vertice from.

La complejidad del peor caso para el método .get() de la clase HashMap implementada en Java 
es O(1), mientras que la complejidad en el peor caso para .contains() es O(n), donde n es el 
tamaño de la lista (ya que puede ser necesario recorrer toda la lista para buscar un elemento)
y el peor caso para .add() es O(1) amortizado, lo que significa que en promedio es constante,
pero en casos raros puede ser lineal. Considerando todo esto, tenemos que nuestro metodo tiene
una complejidad O(n) para el peor caso.


➱Método Disconnect:

Se basa en hacer uso del metodo contains para comprobar que los vertices relacionados al arco 
que se desea eliminar existen, ante lo cual son usados sucesivamente los métodos .get() de la 
clase HashMap yo .remove() de la clase List. La complejidad de .get() es O(1) mientras que la
de .remove para las listas en Java es O(n) en el peor caso, con esto podemos concluir que
complejidad del metodo disconnect es O(n) en el peor caso posible.


➱Método Contains:

Se basa en aprovechar el metodo .containsKey() de la clase HashMap implementada en Java,
el cual tiene una complejidad constante, independientemente del tamaño del mapa considerado,
por lo tanto nuestro metodo contains termina teniendo una complejidad O(1) para el peor caso.


➱Método GetInwardEdges:

Se basa en un bucle for sobre todo el mapa de vertices, obteniendo cada lista con .get() y
confirmando si nuestro vertice de llegada se encuentra en la lista del vertice actual, en cuyo
caso se agrega a la lista de adyacentes. Tenemos que la operacion más costosa corresponde
con el metodo .contains() para List de Java que se encuentra anidado dentro
de nuestro bucle for, como .contains() es O(n) e iteramos n veces donde n es la cardinalidad
del conjunto de vertices, tenemos que este metodo es O(n^2) en el peor caso.


➱Método GetOutwardEdges:

Se basa en usar el metodo contains() para verificar si el vértice está en el mapa, en cuyo
caso se utiliza el metodo .get() para obtener su lista de sucesores inmediatos. Como ambos 
métodos son O(1), tenemos que getOutwardEdgespor tiene una complejidad O(1).


➱Método GetVerticesConnectedTo:

Se basa en usar el metodo contains() para verificar si el vertice esta en el mapa, de ser asi,
se crea un Set inicializado con GetOutwardEdges para posteriormente añadir con .addAll() vertices
faltantes con GetInwardEdges en una llamada directa. Tras esto se regresa una Lista con todos
los vertices del Set.

La implementación de la estructura Set fue unicamente por comodidad y facilidad. Puesto que la
alternativa hubiera sido iterar sobre la lista GetInwardEdges filtrando a traves de comparaciones
los vertices repetidos. Esta comparación es realizada automáticamente con esta estructura, 
simplemente ahorrando lineas de codigo y dando un resultado mas límpio.

Al haber una llamada de GetInwardEdges, tenemos que la complejidad en el tiempo de este método
es O(n^2) en el peor de los casos, al ser esta la operacion con mayor costo.


➱Método GetAllVertices:

Se basa en simplemente devolver una lista con todos los vertices usando .keySet() de HashMap.
Esto tiene una complejidad en Java de O(1) para el peor caso.


➱Método Remove:

Se basa en verificar la pertenencia del vértice con contains(), en caso positivo se hace uso del
metodo .remove() de HashMap, para luego entrar en un bucle for de todas las listas en el mapa,
sobre las cuales se aplicará .remove() de List. El metodo .remove() de HashMap es O(1), mientras
que el de list es O(n) en el peor de los casos. Al estar el .remove() de List anidado en el bucle
for, tenemos que el costo de dicha operación será O(n^2) y al ser la más costosa esa será
la complejidad del metodo.


➱Método Size:

Se basa simplemente en usar el metodo .size() de HashMap, con lo cual, como su complejidad es O(1), 
entonces nuestro metodo size también es O(1) para el peor de los casos.


➱Método Subgraph:

Se basa en crear un nuevo Grafo, para luego iterar sobre todos los vertices de la colección dada,
filtrando cada vertice que no pertenezca al Grafo original con el metodo contains(). Si el vertice
pertence, es añadido con add(), se obtiene la lista asociada con GetOutwardEdges(), para iterar
sobre el mismo y se filtran todos los vertices que si esten dentro de la coleccion para añadirlos
con add() y conectarlos al vertice con connect().

Cabe acotar que si ninguno de los vértices que pertenecen a la colección que es introducida 
pertenecen al grafo original, entonces se retornara un subgrafo vacío.

Tenemos un bucle for anidado dentro de otro bucle for, donde el bucle mas interno realiza
las operaciones add() y connect(), donde connect() es la operación más costosa con complejidad
lineal, con lo cual el peor caso sería O(n^3).



                                        Observaciones y Consideraciones sobre la implementación

Pese a que se obtuvieron ciertos beneficios por parte del uso de los objetos tipo HashMap,
como puede ser el hecho de que las operaciones .add(), .contains(), size(), .getAllVertices(), y
.getOutwardEdges() resultaron con una complejidad computacional O(1), el hecho de mantener,
objetos tipo lista en el tipo de valor del mapa hace que operaciones como .connect() y
.disconnect() no puedan tener una complejidad computacional mejor que la lineal y con ello se
desencadena que operaciones como .remove(), .getInwardEdges() y .getVerticesConnectedTo()
tengan una complejidad cuadrática, mientras que el peor caso de subgraph puede llegar a tener 
el comportamiento más ineficiente de toda la implementación con una cota que no llega a sobrepasar 
la complejidad cúbica. 

Haciendo un análisis del porqué terminan surgiendo complejidades tan costosas en ciertos metodos,
se vio como un factor relevante la presencia de los elementos de tipo List, los cuales son los
responsables de que las complejidades de .connect() y .disconnect() para su peor caso lleguen a 
ser lineales, mientras que en .remove() y .getInwardEdges() (y por transitividad en 
.getVerticesConnectedTo() ya que este hereda la misma complejidad que .getInwardEdges())
lleguen a ser cuadraticas, por el hecho de tener que recorrer las listas y de que metodos propios 
de ellas como: .remove() y .contains() son de orden O(n), los cuales al ser ejecutados dentro de
bucles que iteran sobre todos los vertices del grafo, producen complejidades computacionales 
que se aproximan a O(n^2) y O(n^3).

Siendo esto así, una de las alternativas discutidas para lograr la reducción de las complejidades
se centró en el hecho de tener una implementacion en la que se prescinda el uso de elementos de tipo
lista y se obten por estructuras de datos en Java que proporcionan complejidades, por ejemplo de
búsqueda, constantes, como sucede con los HashSet y LinkedHashSet, las cuales son estructuras de 
datos que utilizan tablas hash para almacenar y buscar elementos de manera eficiente, y con las 
cuales es completamente factible bajar las complejidades de .connect() y .disconnect()
a O(1), las de .getInwardEdges() y .getVerticesConnectedTo() a O(n) y la de .subgraph() a O(n^2) 
manteniéndose la de .remove() en O(n^2). Sin embargo, se decidió mantener una implementación con 
listas dado que ellas forman parte de las estructuras que intervienen en las especificaciones 
de los metodos a implementar en el proyecto, siendo los retornosde las funciones getInwardEdges,
getOutwardEdges, getVerticesConnectedTo y getAllVertices.

Finalmente, podemos decir que hemos conseguido una implementación que logra aprovechar para 
ciertas operaciones las ventajas en eficiencia de las tablas de hash, pero que aún así mantiene 
ciertos metodos que pueden considerarse costosos debido a la presencia de listas dentro de las
estructuras utilizadas.

