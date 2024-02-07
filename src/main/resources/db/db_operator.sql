CREATE TABLE public.config (
    name character varying,
    url character varying,
    username character varying,
    password character varying,
    driverclassname character varying,
    dialect character varying,
    id bigint
);


ALTER TABLE public.config OWNER TO postgres;

INSERT INTO public.config VALUES ('usa', 'jdbc:postgresql://localhost:5432/usa_db', 'postgres', 'secret', 'org.postgresql.Driver', 'org.hibernate.dialect.PostgreSQL95Dialect', 1);
INSERT INTO public.config VALUES ('india', 'jdbc:postgresql://localhost:5432/india_db', 'postgres', 'secret', 'org.postgresql.Driver', 'org.hibernate.dialect.PostgreSQL95Dialect', 2);

-- Completed on 2024-02-04 21:40:18

--
-- PostgreSQL database dump complete
--

