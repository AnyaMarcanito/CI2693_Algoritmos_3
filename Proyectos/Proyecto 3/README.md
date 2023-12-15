Universidad Simón Bolívar, Sartenejas

Departamento de Computación y Tecnología de la Información.

CI–2693 – Laboratorio de Algoritmos y Estructuras III.

Septiembre–Diciembre 2023

Alejandro Zambrano 17-10684

Anya Marcano 19-10336

                                                        Proyecto III:
                                                         Mundo Cubo
                                                         
Para la resolucion del problema propuesto se planteo la siguiente estrategia, una vez es leida y creada la matriz presente en el archivo atlantis.txt
se crea un grafo dirigido usando AdjacencyListGraph junto con una nueva estructura para los vertices llamada Vertex, la cual es capaz de almacenar 
cierta informacion sobre cada uno de los vertices del grafo, entre ellos: un identificador unico (value), una variable entera para la altura (height),
otra para tener el grado interno (inwardDegree), otra para asignar un valor dentro de un orden topologico (f), otra para saber el tamaño que posee la
componente fuertemente conexa a la que pertenece el vertice (tamañoCFC) y finalmente una variable booleana que permite saber si por ese vertice se derrama 
o no el agua al ser colocada en el (spills).

Partiendo de esta estructura, donde los vertices son cada una de las torres que se reflejan en las casillas de la matriz, y los arcos represenetan las 
conexiones que se establecen entre vertices adyacentes hacia arriba, abajo, izquierda y derecha, generandose estos siempre en direccion de los vertices
de mayor altura a los de menor altura; buscamos entonces las componentes fuertemente conexas del grafo, para asi con ellas poder inducir el grafo 
reducido a partir de estas componentes, haciendolo de forma de que en el grafo original, todos los vertices que pertenecen a una misma componente 
conexa sean fusionados en un solo vertice representante, que preserva todas las conexiones que se establecian entre los vertices de la componente y 
aquellos fuera de la misma. 

Luego, teniendo nuestro grafo reducido creado, necesitamos tener un orden para visitar los vertices y verificar si al 
colocar agua en ellos, la misma se derrama o no, por lo que necesitamos encontrar un orden para todos los vertices fuentes del grafo reducido, es por 
ello que en este punto decidimos calcular un orden topologico que nos permitiera garantizar lo anterior, para asi, siguiendo el orden topologico 
encontrado pudieramos hacer uso de las propiedades de las busqueda en profundidad, para que con los metodos: propagacion de agua y derramado, 
tengamos certeza, tanto de cuantos cubos de agua van siendo usados para llenar los huecos de la configuracion de la ciudad introducida, y a la vez ir 
marcando como spills == true todos aquellos vertices que se derraman, ya sea porque conectan con un borde, o porque tienen algun sucesor que desemboca 
en un borde, haciendo que no sea posible la retencion del agua dentro del tal cubo de la ciudad. 

A continuacion se dara una breve explicacion de cada una de las clases y métodos que fueron diseñados para la solución del problema propuesto:

➱ Sobre la implementación de la clase AlfonsoJose:

   En esta clase se tiene un atributo contador, atributos relacionados con el establecimiento de colores para los textos de la salida estandar y ademas
   en esta clase se encuentran almacenados todos los metodos necesarios para lograr llevar a cabo la estrategia de solucion planteada anteriormente.

   Estos metodos son:

   ☆ El Metodo main:
  
  Basicamente es el punto de entrada de todo nuestro programa, haciendose desde el todas las llamadas a los demas metodos que permiten ir construyendo la 
  solucion al problema planteado, con lo cual, el main lee una matriz de alturas desde un archivo .txt con el metodo readMatrixFromFile(), crea un grafo 
  dirigido basado en las alturas de las torres de la ciudad llamando a createGraph(), calcula las componentes fuertemente conectados del grafo con 
  calculoDeComponentesFuertementeConexas(), genera un grafo reducido basado en los componentes fuertemente conectados gracias al metodo generateGraphReducido()
  genera un orden topológico para los vértices del grafo reducido con OrdenTopologico() y dfsTopologico() y finalmente calcula la cantidad de agua necesaria 
  para inundar la ciudad utilizando el orden topológico calculado y el método propagaciónAgua().
      
   ☆ El Metodo readMatrixFromFile:
  
  Este metodo se ecnarga unicamente de leer una matriz desde un archivo .txt y generar con ella una matriz de enteros, siendo la entrada del metodo el nombre 
  del archivo que contiene la matriz y la salida la matriz generada como un arreglo de arreglos de enteros.
     
   ☆ El Metodo valueK:
   
  Esta es una funcion que fue creada con el proposito de poder generar un identificador unico para todos los vertices del grafo, esto con el proposito de poder 
  almacenar la altura como un atributo y poder diferenciar todos los vertices unos de otros a pesar de que compartan altura, algo que dadas las caracteristicas 
  del problema sucede con regularidad, por lo que, la creacion de este metodo surgio como para de la idea de no usar las alturas como los identificadores del 
  los vertices, sino poder tener una forma de poder tener una clave unica para cada uno de ellos y poder diferenciarlos con mayor facilidad.
  En si, la funcion tiene como entradas: 
      
                -> i El índice de la fila.
                -> j El índice de columna.
                -> n El número de filas.
                -> m El número de columnas.
                
   Donde la salida es K = (i*n*m)+j para cada vertice del grafo
   
   ☆ El Metodo createGraph:
   
   Este metodo se encarga de la construccion de nuestro grafo dirigido a partir de la matriz que ya fue leida del archivo .txt, teniendo como parametros de 
   entrada la matriz generada por readMatrixFromFile y como salida el grafo dirigido de los vertices que representan las torres de la ciudad a hundir, y cuyos 
   lados son las conexiones se establecen entre vertices adyacentes en la matriz (arriba, abajo, izquierda, derecha) y siempre en direccion de los vertices de 
   mayor altura a los de menor altura. 
      
   ☆ El Metodo calculoDeComponentesFuertementeConexas:
   
   Tomado del laboratorio #2, este metodo fue creado dentro de la clase NextToYou.java, y esta siendo reutilizado en este laboratorio por adecuarse a las 
   necesidades que se tenian para la implementacion de la solucion encontrada:

   Se trata de la implementación del pseudocódigo visto en teoría para el cálculo de las componentes fuertemente conexas por medio de una búsqueda recursiva de 
   DFS, en el se tiene como parámetro de entrada el grafo del que se quieren las componentes fuertemente conexas y se retorna una lista de listas que contiene las 
   componentes fuertemente conexas del grafo.

   En este método se realiza una corrida de VisitaDFS sobre el grafo original, marcando los vértices visitados con un conjunto de visitados y conservando el orden 
   en el que fueron finalizados los vértices en la búsqueda con una lista de finalizados, luego de lo cual se vacía el conjunto de visitados, se invierte el orden 
   de finalizados, se calcula el grafo simétrico del grafo introducido como parámetro y se procede a correr nuevamente una visitaDFS, en la cual el orden para 
   recorrer los vértices coincide con el orden presente en finalizados y del cual se retornarán las componentes fuertemente conexas conseguidas.
   
   ☆ El Metodo dfs:
   
   Siendo este un metodo auxiliar del calculoDeComponentesFuertementeConexas, tenemos que tambien fue reutilizado de la clase NextToYou.java.

   Se trata de la implementación de la funcion recursiva de DFS presente en el algoritmo de VisitaDFS, en este caso la implementación tiene como parametros: el 
   grafo en el que se realizará la búsqueda, el vértice actual, el conjunto de vértices visitados, una lista de vértices , que bien puede ser de los que ya fueron 
   finalizados durante la busqueda o de los presentes en una componente fuertemente conexa y un booleano que indica qué DFS se quiere implementar, el que permite 
   mantener los vertices que van finalizando o el que permite calcular las componentes fuertemente conexas.

   El método comienza revisando el booleano del parametro, si es true, se va a ejecutar la función recursiva de DFS que mantiene los vertices que van siendo f 
   finalizados, necesaria para la primera corrida de VisitaDFS de calculoDeComponentesFuertementeConexas(), mientras que si el booleano es false, el método de dfs 
   va a ir almacenando las componentes fuertemente conexas, lo cual es necesario durante la segunda corrida de VisitaDFS en el método de 
   calculoDeComponentesFuertementeConexas() una vez que se tiene el orden inverso de los finalizados y ya fue calculado el grafo simétrico.
   
   ☆ El Metodo generateGraphReducido:

   Este metodo se encarga de generar un grafo reducido fusionando las componentes fuertemente conexas en vértices representativos en el grafo que es introducido, 
   esto mientras se encarga de copiar todas las conexiones particulares de cada vertice de la componente al representante, asi como las carteristicas que 
   comparten los vertices de la componente, es decir: heigth, spills y tamanoCFC.
   
   ☆ El Metodo OrdenTopologico:

   
   ☆ El Metodo dfsTopologico
   ☆ El Metodo propagacionAgua
   ☆ El Metodo derramado
   ☆ El Metodo printGraph
   
➱ Sobre la implementación de la clase Vertex:
