Universidad Simón Bolívar, Sartenejas

Departamento de Computación y Tecnología de la Información.

CI–2693 – Laboratorio de Algoritmos y Estructuras III.

Septiembre–Diciembre 2023

Alejandro Zambrano 17-10684

Anya Marcano 19-10336

                                                        Proyecto III:
                                                         Mundo Cubo
                                                         
Para la resolución del problema propuesto se planteó la siguiente estrategia, una vez es leída y creada la matriz presente en el archivo atlantis.txt
se crea un grafo dirigido usando AdjacencyListGraph junto con una nueva estructura para los vértices llamada Vertex, la cual es capaz de almacenar 
cierta información sobre cada uno de los vértices del grafo, entre ellos: un identificador único (value), una variable entera para la altura (height),
otra para tener el grado interno (inwardDegree), otra para asignar un valor dentro de un orden topológico (f), otra para saber el tamaño que posee la
componente fuertemente conexa a la que pertenece el vértice (tamañoCFC) y finalmente una variable booleana que permite saber si por ese vértice se derrama 
o no el agua al ser colocada en el (spills).

Partiendo de esta estructura, donde los vértices son cada una de las torres que se reflejan en las casillas de la matriz, y los arcos representan las 
conexiones que se establecen entre vértices adyacentes hacia arriba, abajo, izquierda y derecha, generándose estos siempre en dirección de los vértices
de mayor altura a los de menor altura; buscamos entonces las componentes fuertemente conexas del grafo, para así con ellas poder inducir el grafo 
reducido a partir de estas componentes, haciéndolo de forma de que en el grafo original, todos los vértices que pertenecen a una misma componente 
conexa sean fusionados en un solo vértice representante, que preserva todas las conexiones que se establecían entre los vértices de la componente y 
aquellos fuera de la misma. 

Luego, teniendo nuestro grafo reducido creado, necesitamos tener un orden para visitar los vértices y verificar si al 
colocar agua en ellos, la misma se derrama o no, por lo que necesitamos encontrar un orden para todos los vértices fuentes del grafo reducido, es por 
ello que en este punto decidimos calcular un orden topológico que nos permitiera garantizar lo anterior, para así, siguiendo el orden topológico 
encontrado pudiéramos hacer uso de las propiedades de la búsqueda en profundidad, para que con los métodos: propagación de agua y derramado, 
tengamos certeza, tanto de cuantos cubos de agua van siendo usados para llenar los huecos de la configuración de la ciudad introducida, y a la vez ir 
marcando como spills == true todos aquellos vértices que se derraman, ya sea porque conectan con un borde, o porque tienen algún sucesor que desemboca 
en un borde, haciendo que no sea posible la retención del agua dentro del tal cubo de la ciudad. 

A continuación se dará una breve explicación de cada una de las clases y métodos que fueron diseñados para la solución del problema propuesto:

➱ Sobre la implementación de la clase AlfonsoJose:

   En esta clase se tiene un atributo contador, atributos relacionados con el establecimiento de colores para los textos de la salida estándar y además
   en esta clase se encuentran almacenados todos los métodos necesarios para lograr llevar a cabo la estrategia de solución planteada anteriormente.

   Estos métodos son:

   ☆ El Método main:
  
  Básicamente es el punto de entrada de todo nuestro programa, haciéndose desde él todas las llamadas a los demás métodos que permiten ir construyendo la 
  solución al problema planteado, con lo cual, el main lee una matriz de alturas desde un archivo .txt con el método readMatrixFromFile(), crea un grafo 
  dirigido basado en las alturas de las torres de la ciudad llamando a createGraph(), calcula las componentes fuertemente conectadas del grafo con 
  calculoDeComponentesFuertementeConexas(), genera un grafo reducido basado en los componentes fuertemente conectados gracias al método generateGraphReducido()
  genera un orden topológico para los vértices del grafo reducido con OrdenTopologico() y dfsTopologico() y finalmente calcula la cantidad de agua necesaria 
  para inundar la ciudad utilizando el orden topológico calculado y el método propagaciónAgua().
      
   ☆ El Método readMatrixFromFile:
  
  Este método se encarga únicamente de leer una matriz desde un archivo .txt y generar con ella una matriz de enteros, siendo la entrada del método el nombre 
  del archivo que contiene la matriz y la salida la matriz generada como un arreglo de arreglos de enteros.
     
   ☆ El Método valueK:
   
  Esta es una función que fue creada con el propósito de poder generar un identificador único para todos los vértices del grafo, esto con el propósito de poder 
  almacenar la altura como un atributo y poder diferenciar todos los vértices unos de otros a pesar de que compartan altura, algo que dadas las características 
  del problema sucede con regularidad, por lo que, la creación de este método surgió como parte de la idea de no usar las alturas como los identificadores del 
  los vértices, sino poder tener una forma de tener una clave única para cada uno de ellos y diferenciarlos con mayor facilidad.
  En sí, la función tiene como entradas: 
      
                -> i El índice de la fila.
                -> j El índice de columna.
                -> n El número de filas.
                -> m El número de columnas.
                
   Donde la salida es K = (i*n*m)+j para cada vértice del grafo
   
   ☆ El Método createGraph:
   
   Este método se encarga de la construcción de nuestro grafo dirigido a partir de la matriz que ya fue leída del archivo .txt, teniendo como parámetros de 
   entrada la matriz generada por readMatrixFromFile y como salida el grafo dirigido de los vértices que representan las torres de la ciudad a hundir, y cuyos 
   lados son las conexiones se establecen entre vértices adyacentes en la matriz (arriba, abajo, izquierda, derecha) y siempre en dirección de los vértices de 
   mayor altura a los de menor altura. 
      
   ☆ El Método calculoDeComponentesFuertementeConexas:
   
   Tomado del laboratorio #2, este método fue creado dentro de la clase NextToYou.java, y está siendo reutilizado en este laboratorio por adecuarse a las 
   necesidades que se tenían para la implementación de la solución encontrada:

   Se trata de la implementación del pseudocódigo visto en teoría para el cálculo de las componentes fuertemente conexas por medio de una búsqueda recursiva de 
   DFS, en él se tiene como parámetro de entrada el grafo del que se quieren las componentes fuertemente conexas y se retorna una lista de listas que contiene las 
   componentes fuertemente conexas del grafo.

   En este método se realiza una corrida de VisitaDFS sobre el grafo original, marcando los vértices visitados con un conjunto de visitados y conservando el orden 
   en el que fueron finalizados los vértices en la búsqueda con una lista de finalizados, luego de lo cual se vacía el conjunto de visitados, se invierte el orden 
   de finalizados, se calcula el grafo simétrico del grafo introducido como parámetro y se procede a correr nuevamente una visitaDFS, en la cual el orden para 
   recorrer los vértices coincide con el orden presente en finalizados y del cual se retornarán las componentes fuertemente conexas conseguidas.
   
   ☆ El Método dfs:
   
   Siendo este un método auxiliar del calculoDeComponentesFuertementeConexas, tenemos que también fue reutilizado de la clase NextToYou.java.

   Se trata de la implementación de la función recursiva de DFS presente en el algoritmo de VisitaDFS, en este caso la implementación tiene como parámetros: el 
   grafo en el que se realizará la búsqueda, el vértice actual, el conjunto de vértices visitados, una lista de vértices , que bien puede ser de los que ya fueron 
   finalizados durante la búsqueda o de los presentes en una componente fuertemente conexa y un booleano que indica que DFS se quiere implementar, el que permite 
   mantener los vértices que van finalizando o el que permite calcular las componentes fuertemente conexas.

   El método comienza revisando el booleano del parámetro, si es true, se va a ejecutar la función recursiva de DFS que mantiene los vértices que van siendo f 
   finalizados, necesaria para la primera corrida de VisitaDFS de calculoDeComponentesFuertementeConexas(), mientras que si el booleano es false, el método de dfs 
   va a ir almacenando las componentes fuertemente conexas, lo cual es necesario durante la segunda corrida de VisitaDFS en el método de 
   calculoDeComponentesFuertementeConexas() una vez que se tiene el orden inverso de los finalizados y ya fue calculado el grafo simétrico.
   
   ☆ El Método generateGraphReducido:

   Este método se encarga de generar un grafo reducido fusionando las componentes fuertemente conexas en vértices representativos en el grafo que es introducido, 
   esto mientras se encarga de copiar todas las conexiones particulares de cada vértice de la componente al representante, así como las carteristas que 
   comparten los vértices de la componente, es decir: heigth, spills y tamanoCFC.
   
   ☆ El Método OrdenTopológico y dfsTopologico:
   
   Se trata de la implementación del pseudocódigo visto en teoría para el cálculo de un orden total para los vértices guiándonos por subconjuntos de vértices 
   fuentes que se van presentando en el grafo una vez que van siendo revisadas las precedencias de los mismos por medio de una implementación particular del 
   DFS Recursivo, método que en este caso fue nombrado dfsTopologico() el cual se encarga de ir asignando para cada objeto vertex del grafo su atributo f, siendo 
   este el lugar en el que es almacenado el resultado de la corrida del algoritmo de orden topológico.
   
   ☆ El Método propagacionAgua y derramado:

   Teniendo como entrada el grafo reducido, el cual ya posee en el atributo f de sus vértices el orden que debe ser seguido para realizar las comprobaciones, 
   este método se encarga precisamente de dar respuesta a la pregunta cuánta agua es necesaria para hundir la configuración de la ciudad introducida, y esto lo 
   hace iterando sobre los vértices siguiendo el orden topológico, para ir desplegando en cada uno de ellos el método derramado, que de forma recursiva se encarga 
   de revisar los sucesores de los vértices y verificar si estos desembocan en un borde, siendo esto un condicional para estar seguros de que si es colocada agua 
   en estos recuadros, la misma se derramará.

   Bajo esta idea, el método derramado, se encarga de realizar un recorrido en profundidad en el grafo a partir de una torre1 y en el cual va verificando si ocurre
   que el agua llega a un punto en el que se sabe que va a derramarse, ante lo cual, se tiene el condicional de que si la torre1 se derrama, se cambia el atributo 
   spills de la torre2 a true, es decir, se va propagando el true en spills a todos los vértices afectados.

   Siendo este método, el que permite luego poder verificar cuales vértices con atributo spills en false pueden ser llenados, y con cuánta agua, haciéndose esto 
   ultimo mediante una comparación con los adyacentes de los vértices que sabemos que pueden retener cierto nivel de agua, usándose el contador de la clase para 
   ir acumulando el agua total que va siendo colocada en el grafo.
   
➱ Sobre la implementación de la clase Vertex:
   Se trata de la estructura creada para poder almacenar toda la información relevante con respecto a los vértices para este problema, teniendo métodos getter y 
   setter para cada uno de los siguientes atributos en añadidura al constructor de la clase:

               -> Value: Utilizado como identificador único para cada vértice agregado al grafo, y tiendo relación con el método valueK.
               -> Heigth: Siendo la altura del vértice que se refleja en la matriz de alturas de las torres que es input del problema.
               -> inWardDegree: Almacenando el grado interno del vértice en cuestión.
               -> Spills: Siendo un marcador booleano que permite distinguir si un vértice se derrama o si por el contrario es capaz de retener agua dentro.
               -> f: Entero que representa el orden del vértice dentro de un orden topológico calculado
               -> tamanoCFC: Tamaño de la componente fuertemente conexa a la que pertenece el vértice, esto para que al momento en que sea posible colocar agua 
                  dentro de un vértice representante se pueda saber por cuantas casillas debe multiplicarse el agua que hay dentro de él.

➱ Compilacion: javac AdjacencyListGraph.java Vertex.java AlfonsoJose.java

➱ Ejecucion: java AlfonsoJose

➱ Salida esperada: La cantidad de agua necesaria para hundir la ciudad es: 5



