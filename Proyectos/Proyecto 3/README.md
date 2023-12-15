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

➱ Sobre la implementación de CartaMostro:
