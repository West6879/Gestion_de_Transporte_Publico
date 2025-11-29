CREATE TYPE tipo AS ENUM ('tren', 'metro', 'bus');

CREATE TABLE estaciones
(
    id	        uuid	    NOT NULL,
    nombre      varchar(50) NOT NULL,
    zona 	    varchar(50) NOT NULL,
    latitud     real        NOT NULL,
    longitud    real        NOT NULL,
    costo 	    real        NOT NULL,
    velocidad   int         NOT NULL,
    tipo        tipo        NOT NULL,
    color       varchar(50) NOT NULL,
    rutas       int         NOT NULL,
    CONSTRAINT estaciones_pkey PRIMARY KEY (id)
);




