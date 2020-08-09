/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases 12.1.0                     */
/* Target DBMS:           PostgreSQL 12                                   */
/* Project file:          model.dez                                       */
/* Project name:                                                          */
/* Author:                                                                */
/* Script type:           Database creation script                        */
/* Created on:            2020-08-09 12:33                                */
/* ---------------------------------------------------------------------- */


/* ---------------------------------------------------------------------- */
/* Add domains                                                            */
/* ---------------------------------------------------------------------- */

CREATE DOMAIN identity AS INTEGER;

/* ---------------------------------------------------------------------- */
/* Add tables                                                             */
/* ---------------------------------------------------------------------- */

/* ---------------------------------------------------------------------- */
/* Add table "ENTORNOS.CLIENTE"                                           */
/* ---------------------------------------------------------------------- */

CREATE TABLE ENTORNOS.CLIENTE (
    ID_CLIENTE identity  NOT NULL,
    CLIENTE CHARACTER VARYING(40)  NOT NULL,
    CONSTRAINT PK_CLIENTE PRIMARY KEY (ID_CLIENTE)
);

CREATE UNIQUE INDEX IDX_CLIENTE_1 ON ENTORNOS.CLIENTE (CLIENTE);

/* ---------------------------------------------------------------------- */
/* Add table "ENTORNOS.ENTORNO_CLIENTE"                                   */
/* ---------------------------------------------------------------------- */

CREATE TABLE ENTORNOS.ENTORNO_CLIENTE (
    ID_CLIENTE identity  NOT NULL,
    ID_ENTORNO identity  NOT NULL,
    ENTORNO CHARACTER VARYING(40)  NOT NULL,
    CONSTRAINT PK_ENTORNO_CLIENTE PRIMARY KEY (ID_CLIENTE, ID_ENTORNO)
);

CREATE UNIQUE INDEX IDX_ENTORNO_CLIENTE_1 ON ENTORNOS.ENTORNO_CLIENTE (ID_CLIENTE,ENTORNO);

/* ---------------------------------------------------------------------- */
/* Add table "ENTORNOS.APLICACION"                                        */
/* ---------------------------------------------------------------------- */

CREATE TABLE ENTORNOS.APLICACION (
    APLICACION CHARACTER VARYING(40)  NOT NULL,
    CONSTRAINT PK_APLICACION PRIMARY KEY (APLICACION)
);

/* ---------------------------------------------------------------------- */
/* Add table "ENTORNOS.SERVIDOR"                                          */
/* ---------------------------------------------------------------------- */

CREATE TABLE ENTORNOS.SERVIDOR (
    ID_CLIENTE identity  NOT NULL,
    ID_SERVIDOR identity  NOT NULL,
    SERVIDOR CHARACTER VARYING(40)  NOT NULL,
    IP CHARACTER VARYING(40)  NOT NULL,
    CONSTRAINT PK_SERVIDOR PRIMARY KEY (ID_CLIENTE, ID_SERVIDOR)
);

CREATE UNIQUE INDEX IDX_SERVIDOR_1 ON ENTORNOS.SERVIDOR (ID_CLIENTE,SERVIDOR);

/* ---------------------------------------------------------------------- */
/* Add table "eNTORNOS.CONTENEDOR"                                        */
/* ---------------------------------------------------------------------- */

CREATE TABLE eNTORNOS.CONTENEDOR (
    ID_CLIENTE identity  NOT NULL,
    ID_SERVIDOR identity  NOT NULL,
    CONTENEDOR CHARACTER VARYING(40)  NOT NULL,
    PUERTO SMALLINT,
    CONSTRAINT PK_CONTENEDOR PRIMARY KEY (ID_CLIENTE, ID_SERVIDOR, CONTENEDOR)
);

/* ---------------------------------------------------------------------- */
/* Add table "ENTORNOS.APLICACION_CLIENTE"                                */
/* ---------------------------------------------------------------------- */

CREATE TABLE ENTORNOS.APLICACION_CLIENTE (
    ID_APLICACION_CLIENTE identity  NOT NULL,
    ID_CLIENTE identity  NOT NULL,
    ID_SERVIDOR INTEGER  NOT NULL,
    CONTENEDOR CHARACTER VARYING(40)  NOT NULL,
    ID_ENTORNO identity  NOT NULL,
    APLICACION CHARACTER VARYING(40)  NOT NULL,
    PUERTO SMALLINT,
    CONSTRAINT PK_APLICACION_CLIENTE PRIMARY KEY (ID_APLICACION_CLIENTE)
);

CREATE UNIQUE INDEX IDX_APLICACION_CLIENTE_1 ON ENTORNOS.APLICACION_CLIENTE (ID_CLIENTE,ID_SERVIDOR,CONTENEDOR,ID_ENTORNO,APLICACION);

/* ---------------------------------------------------------------------- */
/* Add table "ENTORNOS.DEPENDENCIA_APLICACION"                            */
/* ---------------------------------------------------------------------- */

CREATE TABLE ENTORNOS.DEPENDENCIA_APLICACION (
    ID_APLICACION_CLIENTE identity  NOT NULL,
    ID_APLICACION_DEPENDE identity  NOT NULL,
    NOMBRE CHARACTER VARYING(40)  NOT NULL,
    CONSTRAINT PK_DEPENDENCIA_APLICACION PRIMARY KEY (ID_APLICACION_CLIENTE, ID_APLICACION_DEPENDE)
);

/* ---------------------------------------------------------------------- */
/* Add foreign key constraints                                            */
/* ---------------------------------------------------------------------- */

ALTER TABLE ENTORNOS.ENTORNO_CLIENTE ADD CONSTRAINT FK_1_ENTORNO_CLIENTE 
    FOREIGN KEY (ID_CLIENTE) REFERENCES ENTORNOS.CLIENTE (ID_CLIENTE);

ALTER TABLE ENTORNOS.SERVIDOR ADD CONSTRAINT FK_1_SERVIDOR 
    FOREIGN KEY (ID_CLIENTE) REFERENCES ENTORNOS.CLIENTE (ID_CLIENTE);

ALTER TABLE eNTORNOS.CONTENEDOR ADD CONSTRAINT FK_1_CONTENEDOR 
    FOREIGN KEY (ID_CLIENTE, ID_SERVIDOR) REFERENCES ENTORNOS.SERVIDOR (ID_CLIENTE,ID_SERVIDOR);

ALTER TABLE ENTORNOS.APLICACION_CLIENTE ADD CONSTRAINT FK_1_APLICACION_CLIENTE 
    FOREIGN KEY (ID_CLIENTE, ID_SERVIDOR, CONTENEDOR) REFERENCES eNTORNOS.CONTENEDOR (ID_CLIENTE,ID_SERVIDOR,CONTENEDOR);

ALTER TABLE ENTORNOS.APLICACION_CLIENTE ADD CONSTRAINT FK_2_APLICACION_CLIENTE 
    FOREIGN KEY (APLICACION) REFERENCES ENTORNOS.APLICACION (APLICACION);

ALTER TABLE ENTORNOS.APLICACION_CLIENTE ADD CONSTRAINT FK_3_APLICACION_CLIENTE 
    FOREIGN KEY (ID_CLIENTE, ID_ENTORNO) REFERENCES ENTORNOS.ENTORNO_CLIENTE (ID_CLIENTE,ID_ENTORNO);

ALTER TABLE ENTORNOS.DEPENDENCIA_APLICACION ADD CONSTRAINT FK_1_DEPENDENCIA_APLICACION 
    FOREIGN KEY (ID_APLICACION_CLIENTE) REFERENCES ENTORNOS.APLICACION_CLIENTE (ID_APLICACION_CLIENTE);

ALTER TABLE ENTORNOS.DEPENDENCIA_APLICACION ADD CONSTRAINT FK_2_DEPENDENCIA_APLICACION 
    FOREIGN KEY (ID_APLICACION_DEPENDE) REFERENCES ENTORNOS.APLICACION_CLIENTE (ID_APLICACION_CLIENTE);
