Universidad Simón Bolívar, Sartenejas

Departamento de Computación y Tecnología de la Información.

CI–2693 – Laboratorio de Algoritmos y Estructuras III.

Septiembre–Diciembre 2023

Alejandro Zambrano 17-10684

Anya Marcano 19-10336


                                                            Proyecto II:
                                                           Mundo Chiquito


A continuacion se dara una breve explicacion de cada una de las clases y métodos que fueron diseñados para la solución del problema
propuesto:


➱ Sobre la implementación de CartaMostro:


  La Clase CartaMostro permite crear una carta mostro con cada una de sus características: nombre, nivel, poder y atributo.
 Adicionalmente, esta clase fue diseñada considerando atributo de clase privado llamado cartasExistentes, el cual va almacenando
 todos los nombres de las instancias de la clase, es decir, todos los nombres de las cartas que van siendo agregadas; esto con la
 finalidad de poder evitar la creacion de varias cartas que posean el mismo nombre. 
 
 Así, el constructor se encarga de que cada instancia de CartaMostro creada cumpla con las reglas de las características; 
 en orden del constructor se verifica que: 
 
      El nombre de la carta no sea vacío.
      El nivel del mostro sea un entero entre 1 y 12.
      El poder del mostro sea múltiplo de 50.
      El atributo pueda ser solamente alguno de los siguientes: AGUA o FUEGO o VIENTO o TIERRA o LUZ u OSCURIDAD o DIVINO.
      
Por último, se usa el método contains de HashSeht para confirmar que el nombre de la carta no exista ya entre el resto de 
cartas creadas; si todo esto se cumple, el constructor crea la instancia de CartaMostro.

En cuanto a complejidad, podemos decir que como el método contains tiene una complejidad de O(1) y el resto de comparaciones
a realizar también son constantes, entonces cada carta se crea en tiempo constante.

 Por otro lado, como se solicitaba en las instrucciones del proyecto, dentro de la clase CartaMostro, por cada característica
 que poseen las cartas fue creado un método get"Característica", cada uno de los cuales se encarga de devolver la característica
 a la que se relaciona, siendo cada uno de ellos de una complejidad O(1):

               getNombre()
               getPoder()
               getNivel()
               getAtributo()

➱Sobre la implementación de MundoChiquito:

  La Clase MundoChiquito permite conseguir todas las ternas generadas por el efecto de la carta de mismo nombre dado un mazo de cartas
  mostro. Posee tres métodos diferentes: main, ternaChiquita y compartenCaracterística.

  ☆ Sobre la implementación de ternaChiquita:
  
  Empecemos por el método ternaChiquita que es el corazón de la solución. Este recibe un Digrafo con V = {las cartas mostro de un mazo}
  y E = {arcos pertenecientes a VxV | dado v,w pertenecientes a V, el arco conecta a v con w si y solo si comparten una única característica
  en común (nivel, poder, atributo)} como entrada.
  
  Este metodo, sencillamente inicia un bucle for sobre todo el conjunto V, haciendo uso del método getAllVertices de la Clase 
  AdjacencyListGraph, anidando otro bucle for pero esta vez sobre los sucesores adyacentes de v perteneciente a V, a través del 
  método getOutwardEdges de la misma Clase, a lo cual es abierto otro bucle for anidado, el cual itera sobre los sucesores adyacentes 
  del w creado en el bucle anterior (si es que existía) haciendo uso del mismo método del bucle anterior, es decir el getOutwardEdges(),
  donde la única línea de comando dentro de los tres bucles es un System.out.println con los nombres de cada mostro, obtenidos a través
  del método getNombre de la Clase CartaMostro. 
  
  Siendo así, el método solo imprime por la salida estándar las ternas de cartas mostro que cumplen con la condición de Mundo Chiquito, 
  es decir, mostros que solo compartan una única característica con el otro de uno en uno: si mostro1 comparte una única característica 
  con mostro2, este se encontrará en la lista de sucesores adyacentes de mostro1 y viceversa, y si mostro2 comparte una única característica 
  con mostro3, este se encontrará en la lista de adyacencia de mostro2. 
  
  Luego, en cuanto a complejidad, tenemos que, como getAllVertices y getOutwardEdges tienen complejidad O(1), y como el primer bucle for 
  iterará un total de |V| veces, el segundo bucle en el peor de los casos un total de |V-1| veces (si todos los vértices están conectados), 
  y el tercer bucle |V-1| veces también (al ser que para cada arista de E, v y w siempre son mutuamente alcanzables), entonces, podemos 
  decir que el método ternaChiquita tiene una complejidad en el tiempo de O(|V|^3) en el peor de los casos, y O(|V|) en el caso borde donde 
  ningún vértice está conectado, ya que solo se entrará en el primer bucle for y no se iterará en ninguno de los otros dos.

  Otras posibles implementaciones:
  
   Al diseñar la solución, nos percatamos de que una de las posibles estrategias para hacerlo hubiese podido ser por medio de backtracking, 
   pero debido a su naturaleza recursiva, se consumiría una cantidad considerable de espacio al almacenar copias de las respuestas parciales,
   que en la implementación realizada no son necesarias.
   
   Por otra parte, en términos de complejidad en el tiempo, decidimos no optar por backtracking debido no solo a su gasto de espacio, sino 
   también al hecho de que ambas implementaciones terminarían siendo semejantes en cuanto a complejidad, ya que no sería si no hasta la 2da 
   llamada recursiva del backtracking que se encontraría la solución, si es que existe alguna, donde el método elegirAcción tomaría un vértice 
   en la lista de sucesores adyacentes de un vértice inicial, aplicar conectaría los vértices en algún tipo de estructura de elección como una 
   Lista o un String concatenado; y al no haber solución inicial, sino que hay que observar cada vértice para conseguir cada terna, antes de la
   primera llamada recursiva del backtracking habría un bucle for para iterar sobre todos los vértices del Digrafo, de ahí vendría nuestro 
   vértice inicial. En el peor caso, la primera llamada se haría |V| veces, la llamada de aplicar |V-1|^2 veces, ya que hay que ingresar en 
   al menos dos listas de adyacencias para conseguir una terna. Una vez se llega a una terna se le debe indicar a backtracking que se detenga 
   y devuelva esa solución, con lo cual estaríamos en presencia de un peor caso que al igual que la implementacion realizada es cúbica acarreando
   con ella un gasto adicional de estructuras en memoria.

  ☆ Sobre la implementación de compartenUnaCaracterística:
  
  Es un método sencillo que recibe dos instancias diferentes de CartaMostro y simplemente suma 1 a un contador cada vez que consigue
  una característica en común (nivel, poder, atributo), usando los métodos getNivel, getPoder y getAtributo de la Clase CartaMostro.
  Luego, si el contador es igual a 1 devuelve verdadero y lo contrario en todos los otros casos; así es seguro que solo tengan una
  característica en común. Los métodos get, las comparaciones y la suma son todas O(1) y por ende este método tiene esa complejidad.

  ☆ Sobre la implementación de main:
  
  En el main se crea una nueva instancia de AdjacencyListGraph, luego a través de un Scanner lee las líneas de un archivo deck.csv
  con los datos de las cartas mostro del mazo del jugador. Se ignora la primera línea de header y se crea una nueva instancia de
  CartaMostro por cada línea del archivo que contenga la información en el formato correcto de la carta, es decir, 
  "nombre,nivel,atributo,poder". Estas instancias de CartaMostro serán añadidas al grafo como vértices. Luego, se llevarán a cabo dos
  bucles for sobre todos los vértices del grafo, conectando a v con w y w con v si se cumple compartenUnaCaracterística(v,w). Con el
   grafo poblado y conectado, se hace la llamda a ternaChiquita.
