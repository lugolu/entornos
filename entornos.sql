
CREATE TABLE entornos.cliente (
  id_cliente integer NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1 ),
  cliente character varying(40) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT cliente_pkey PRIMARY KEY (id_cliente)
) ;


CREATE TABLE entornos.entorno_cliente (
  id_entorno integer NOT NULL,
  id_cliente integer NOT NULL,
  entorno character varying(40) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT entorno_cliente_pkey PRIMARY KEY (id_entorno, id_cliente),
    CONSTRAINT "FK_1_ENTORNO_CLIENTE" FOREIGN KEY (id_cliente)
        REFERENCES entornos.cliente (id_cliente) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
) ;
