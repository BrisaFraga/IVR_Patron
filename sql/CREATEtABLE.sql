
CREATE TABLE Cliente (
    dni        INT PRIMARY KEY,
    nombre     VARCHAR(255),
    nrocelular VARCHAR(255)
);
CREATE TABLE Estado (
    id     INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255)
);

CREATE TABLE Encuesta (
    id               INT PRIMARY KEY AUTO_INCREMENT,
    descripcion      VARCHAR(255),
    fechaFinVigencia DATETIME
);

CREATE TABLE Pregunta (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    pregunta    VARCHAR(255),
    id_encuesta INT NOT NULL,
    FOREIGN KEY (id_encuesta)
    REFERENCES Encuesta(id)
    ON DELETE CASCADE
);

CREATE TABLE RespuestaPosible (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    descripcion VARCHAR(255),
    valor       INT,
    id_pregunta INT NOT NULL,
    FOREIGN KEY (id_pregunta)
    REFERENCES Pregunta(id)
    ON DELETE CASCADE
);

CREATE TABLE Llamada (
    id                     INT PRIMARY KEY AUTO_INCREMENT,
    descripcionOperador    VARCHAR(255),
    detalleAccionRequerida VARCHAR(255),
    duracion float,
    encuestaEnviada        INT,
    observacionAuditor     VARCHAR(255),
    dni_cliente            INT NOT NULL,
    FOREIGN KEY (dni_cliente)
    REFERENCES Cliente(dni)
);

CREATE TABLE RespuestasDeCliente (
    id                   INT PRIMARY KEY AUTO_INCREMENT,
    fechaEncuesta        DATETIME,
    id_llamada           INT NOT NULL,
    id_respuesta_posible INT NOT NULL,
    FOREIGN KEY (id_llamada)
    REFERENCES Llamada(id),
    FOREIGN KEY (id_respuesta_posible)
    REFERENCES RespuestaPosible(id)
);

CREATE TABLE CambioEstado (
    id              INT PRIMARY KEY AUTO_INCREMENT,
    fechaHoraInicio DATETIME,
    id_estado       INT NOT NULL,
    id_llamada      INT NOT NULL,
    FOREIGN KEY (id_estado)
    REFERENCES Estado(id),
    FOREIGN KEY (id_llamada)
    REFERENCES Llamada(id)
);