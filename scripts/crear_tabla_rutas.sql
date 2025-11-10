CREATE TABLE rutas
(
    id          uuid        PRIMARY KEY,
    distancia   int         NOT NULL,
    tiempo      int         NOT NULL,
    costo       real        NOT NULL,
    ponderacion real        NOT NULL,
    id_origen   uuid        NOT NULL,
    id_destino  uuid        NOT NULL,
    CONSTRAINT fk_origen FOREIGN KEY (id_origen) REFERENCES estaciones(id) ON DELETE CASCADE,
    CONSTRAINT fk_destino FOREIGN KEY (id_destino) REFERENCES estaciones(id) ON DELETE CASCADE
);