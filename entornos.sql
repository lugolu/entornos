
CREATE TABLE entornos.cliente (
  id_cliente numeric NOT NULL,
  cliente character varying(40) COLLATE pg_catalog."default" NOT NULL
) ;


CREATE TABLE entornos.entorno_cliente (
  id_entorno numeric NOT NULL,
  id_cliente numeric NOT NULL,
  entorno character varying(40) COLLATE pg_catalog."default" NOT NULL
) ;
