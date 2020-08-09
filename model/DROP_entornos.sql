/* ---------------------------------------------------------------------- */
/* Script generated with: DeZign for Databases 12.1.0                     */
/* Target DBMS:           PostgreSQL 12                                   */
/* Project file:          model.dez                                       */
/* Project name:                                                          */
/* Author:                                                                */
/* Script type:           Database drop script                            */
/* Created on:            2020-08-09 02:59                                */
/* ---------------------------------------------------------------------- */


/* ---------------------------------------------------------------------- */
/* Drop foreign key constraints                                           */
/* ---------------------------------------------------------------------- */

ALTER TABLE ENTORNOS.ENTORNO_CLIENTE DROP CONSTRAINT FK_1_%table%;

ALTER TABLE ENTORNOS.SERVIDOR DROP CONSTRAINT FK_1_%table%;

ALTER TABLE eNTORNOS.CONTENEDOR DROP CONSTRAINT FK_1_%table%;

ALTER TABLE ENTORNOS.APLICACION_CLIENTE DROP CONSTRAINT FK_1_%table%;

ALTER TABLE ENTORNOS.APLICACION_CLIENTE DROP CONSTRAINT FK_2_%table%;

ALTER TABLE ENTORNOS.APLICACION_CLIENTE DROP CONSTRAINT FK_3_%table%;

/* ---------------------------------------------------------------------- */
/* Drop table "ENTORNOS.APLICACION_CLIENTE"                               */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE ENTORNOS.APLICACION_CLIENTE DROP CONSTRAINT PK_APLICACION_CLIENTE;

DROP TABLE ENTORNOS.APLICACION_CLIENTE;

/* ---------------------------------------------------------------------- */
/* Drop table "eNTORNOS.CONTENEDOR"                                       */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE eNTORNOS.CONTENEDOR DROP CONSTRAINT PK_CONTENEDOR;

DROP TABLE eNTORNOS.CONTENEDOR;

/* ---------------------------------------------------------------------- */
/* Drop table "ENTORNOS.SERVIDOR"                                         */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE ENTORNOS.SERVIDOR DROP CONSTRAINT PK_SERVIDOR;

DROP TABLE ENTORNOS.SERVIDOR;

/* ---------------------------------------------------------------------- */
/* Drop table "ENTORNOS.APLICACION"                                       */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE ENTORNOS.APLICACION DROP CONSTRAINT PK_APLICACION;

DROP TABLE ENTORNOS.APLICACION;

/* ---------------------------------------------------------------------- */
/* Drop table "ENTORNOS.ENTORNO_CLIENTE"                                  */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE ENTORNOS.ENTORNO_CLIENTE DROP CONSTRAINT PK_ENTORNO_CLIENTE;

DROP TABLE ENTORNOS.ENTORNO_CLIENTE;

/* ---------------------------------------------------------------------- */
/* Drop table "ENTORNOS.CLIENTE"                                          */
/* ---------------------------------------------------------------------- */

/* Drop constraints */

ALTER TABLE ENTORNOS.CLIENTE DROP CONSTRAINT PK_CLIENTE;

DROP TABLE ENTORNOS.CLIENTE;

/* ---------------------------------------------------------------------- */
/* Drop domains                                                           */
/* ---------------------------------------------------------------------- */

DROP DOMAIN identity;
