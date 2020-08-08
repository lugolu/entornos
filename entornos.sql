
CREATE TABLE entornos.cliente (
  id_cliente numeric NOT NULL,
  cliente character varying(40) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT cliente_pkey PRIMARY KEY (id_cliente)
) ;


CREATE TABLE entornos.entorno_cliente (
  id_entorno numeric NOT NULL,
  id_cliente numeric NOT NULL,
  entorno character varying(40) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT entorno_cliente_pkey PRIMARY KEY (id_entorno, id_cliente),
    CONSTRAINT "FK_1_ENTORNO_CLIENTE" FOREIGN KEY (id_cliente)
        REFERENCES entornos.cliente (id_cliente) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
) ;
