# Implementación Visual

## Clases:

### Main
Clase principal donde se inicializa y corre el programa.

### PrincipalController
El controlador de la pantalla principal, maneja todos los botones del \
menu principal para abrir las demás ventanas.

### MapaController
El controlador del mapa, que maneja como se dibuja el mapa dentro de un \
panel, tambien maneja el zoom y el movimiento sobre el mapa.

### BusquedaRutaController
El controlador para la ventana de busqueda de rutas, maneja los botones \ 
para buscar las rutas más cortas, baratas, etc.

### EstacionController
El controlador para el ingreso de estaciones, maneja las validaciones y \
la funcionalidad visual de este.

### RutaController
El controlador para el ingreso de rutas, maneja las validaciones y \
la funcionalidad visual de este.

### ListEstacionController
El controlador para la lista de estaciones, contiene botones para modificar \
y eliminar estaciones.

### ListRutaController
El controlador para la lista de rutas, contiene botones para modificar \
y eliminar estaciones.

### EstadisticasController
El controlador para la ventana de estadisticas, muestra estadisticas \
del sistema en el momento actual.

### MatrizController
El controlador para la ventana que muestra la matriz de minimas distancias.

### Setups
Clase que maneja todo el codigo de por atras para setear cada ventana, \
también setups para alertas y otras conveniencias. Todos los metodos de \ 
esta clase son llamados desde otras clases.