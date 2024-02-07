CREATE TABLE public."user" (
    first_name character varying,
    last_name character varying,
    address character varying,
    phone character varying,
    email character varying,
    id bigint NOT NULL
);


ALTER TABLE public."user" OWNER TO postgres;
--
-- TOC entry 4688 (class 2606 OID 16428)
-- Name: user user_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pk PRIMARY KEY (id);


-- Completed on 2024-02-04 21:43:37

--
-- PostgreSQL database dump complete
--

