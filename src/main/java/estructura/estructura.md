# Estructura de Grafo

## Clases:

### GrafoTransporte
Clase principal que representa el grafo de gestion de transporte. Usa \
una lista de adyacencia para el manejo de un grafo dirigido.

### Servicio
Clase controladora que maneja el grafo y contiene hashmaps para estaciones \
y rutas usando el patron singleton.

### MatrizDistMinimas
Clase adicional para manejar una matriz de adyacencia dirigida. \
Se usa como una alternativa a la lista de adyacencia principal.

### Estacion
La clase objetó para representar las estaciones, que son los vertices del grafo.\
Contiene multiples atributos para el uso como estación.

### Ruta
La clase objetó para representar las rutas, que son las aristas del grafo. \
Aparte de sus atributos para el manejo de peso, también contiene su estacion \
origen y destino correspondiente. 

### ResultadoRuta
Clase para representar y guardar una lista de rutas, o sea un camino o \
recorrido total entre dos estaciones.

## Enums:

### TipoEstacion
Enumeracion para representar los tipos de estaciones que hay, contiene 3 \
Bus, Tren y Metro.

