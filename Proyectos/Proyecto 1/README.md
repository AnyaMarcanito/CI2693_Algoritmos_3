Alejandro Zambrano 17-10684
Anya Marcano 19-

Metodo Add:
Se basa en aprovechar el metodo .containsKey() de la clase HashMap implementada en Java, 
el cual tiene una complejidad constante, independientemente del tamaño del mapa considerado,
por lo tanto nuestro metodo contains termina teniendo una complejidad O(1) para el peor caso.

Metodo Connect:
Se basa en utilizar el metodo contains para comprobar si los vertices relacionados al arco 
que se desea crear existen dentro del grafo y de ser así, utiliza el metodo .get() de la clase 
HashMap y los metodos .contains() y .add() de la clase List de Java para agregar el nuevo arco 
en la lista de sucesores del vertice from.

La complejidad del peor caso para el método .get() de la clase HashMap implementada en Java 
es O(1), mientras que la complejidad en el peor caso para .contains() es O(n), donde n es el 
tamaño de la lista, ya que puede ser necesario recorrer toda la lista para buscar un elemento,
y el peor caso para .add() es O(1) amortizado, lo que significa que en promedio es constante,
pero en casos raros puede ser lineal. Considerando todo esto, tenemos que nuestro metodo tiene
una complejidad O(n) para el peor caso.

Metodo Disconnect:
Se basa en hace uso del metodo contains para comprobar que los vertices relacionados al arco
existen, de ser asi, se hace uso del metodo .get() de las clase HashMap, para inmediatamente
utilizar .remove() sobre el vertice de llegada. Tenemos que la complejidad de .remove()
de las listas en Java es O(n) en el peor caso, por tanto la complejidad del metodo completo
en el peor caso es O(n) al ser la operacion mas costosa.

Metodo Contains:
Se basa en aprovechar el metodo .containsKey() de la clase HashMap implementada en Java,
el cual tiene una complejidad constante, independientemente del tamaño del mapa considerado,
por lo tanto nuestro metodo contains termina teniendo una complejidad O(1) para el peor caso.

Metodo GetInwardEdges:
Se basa en un bucle for sobre todo el mapa de vertices, obteniendo cada lista con .get() y
confirmando si nuestro vertice de llegada se encuentra en la lista del vertice actual, en cuyo
caso se agrega a la lista de predecesores adyacentes. Tenemos que la operacion mas costosa
del algoritmo es el metodo .contains() para List de Java que se encuentra anidado dentro
de nuestro bucle for, como .contains() es O(n) e iteramos n veces donde n es la cardinalidad
del conjunto de vertices, tenemos que este metodo es O(n^2) en el peor caso.

Metodo GetOutwardEdges:
Se basa en usar el metodo contains() para verificar si el vertice esta en el mapa, en cuyo
caso se utiliza el metodo .get() para obtener su lista de sucesores inmediatos. Ambos metodos
son O(1) y por tanto este tiene una complejidad O(1)

Metodo GetVerticesConnectedTo:
Se basa en usar el metodo contains() para verificar si el vertice esta en el mapa, de ser asi,
se crea un Set inicializado en GetOutwardEdges para posteriormente añadir con .addAll() vertices
faltantes con GetInwardEdges en una llamada directa. Tras esto se regresa una Lista con todos
los vertices en el Set.

La implementación de la estructura Set fue unicamente por comodidad y facilidad. Puesto que la
alternativa hubiera sido iterar sobre la lista GetInwardEdges filtrando a traves de comparaciones
los vertices repetidos. Esta comparacion es realizada automaticamente con esta estructura, 
simplemente ahorrando lineas de codigo y dando un resultado mas limpio.

Al haber una llamada de GetInwardEdges, tenemos que la complejidad en el tiempo de este metodo
es O(n^2) en el peor de los casos, al ser esta la operacion con mayor costo.

Metodo GetAllVertices:
Se basa en simplemente devolver una lista con todos los vertices usando .keySet() de HashMap.
Esto tiene una complejidad O(1) acordado a lo dicho por Java.

Metodo Remove:
Se basa en verificar la pertenencia del vertice con contains(), en caso positivo se sigue del
metodo .remove() de HashMap, para luego entrar en un bucle for de todas las listas en el mapa,
sobre las cuales se aplicara .remove() de List. El metodo .remove() de HashMap es O(1) mientras
que el de list es O(n) en el peor de los casos. Al estar el .remove() de List anidado en el bucle
for, tenemos que el costo de dicha operacion sera O(n^2) y al ser la mas costosa, esa sera
la complejidad del metodo.

Metodo Size:
Se basa en usar el metodo .size() de HashMap, siendo esta operacion O(1).

Metodo Subgraph:
Se basa en crear un nuevo Grafo, para luego iterar sobre todos los vertices de la coleccion dada,
filtrando cada vertice que no pertenezca al Grafo original con el metodo contains(). Si el vertice
pertence, es añadido con add(), se obtiene la lista asociada con GetOutwardEdges(), para iterar
sobre el mismo, se filtran todos los vertices que si esten dentro de la coleccion para añadirlos
con add() y conectarlos al vertice con connect().

Tenemos un bucle for anidado dentro de otro bucle for, donde el bucle mas interno realiza
las operaciones add() y connect(), donde connect() es la operacion mas costosa con complejidad
lineal. El peor caso seria O(n^3).
