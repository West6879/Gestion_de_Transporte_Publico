# Implementaciones de los Algoritmos

## Clases:

### Dijkstra
Implementación del algoritmo de Dijkstra, este es el algoritmo principalmente \
usado a través del programa, puede manejar los diferentes criterios y también \
retorna el top 3 de mejores rutas.

### Bellman_Ford
Implementación del algoritmo de Bellman Ford, este algoritmo es usado solo \ 
para calcular la mejor ruta basada en costo, no está siendo utilizando \
actualmente en el programa.

### FloydWarshall
Implementación del algoritmo de Floyd Warshall, este algoritmo es usado \
para crear la matriz de distancias minimas, no es utilizado en la busqueda \
de rutas principal.

### Prim
Implementación del algoritmo de Prim, utiliza una clase extra que extiende de \
DatoCamino para detectar ciclos a traves de las iteraciones del algoritmo. \
No es utilizado actualmente en el programa, solo sirve como demostración.

### Kruskal
Implementación del algoritmo de Kruskal, utiliza una clase adicional llamada \
Union que es una implementación basica de un Disjoint Union Set, para \
manejar la detección de ciclos de manera eficiente.
No es utilizado actualmente en el programa, solo sirve como demostración.

### Caminos
Una clase adicional que contiene metodos más generales para ayudar con las \
implementaciones de los algoritmos.

### DatoCamino
Una clase de objeto que permite guardar caminos para poder manejar los \
algoritmos de manera más eficiente.

### Randomizacion
Clase adicional para manejar la randomización de eventos que pueden ocurrir \
en el sistema.