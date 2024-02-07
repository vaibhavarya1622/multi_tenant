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
-- TOC entry 4832 (class 0 OID 16420)
-- Dependencies: 215
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: postgres
--
-- TOC entry 4688 (class 2606 OID 16426)
-- Name: user user_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pk PRIMARY KEY (id);


-- Completed on 2024-02-04 21:42:32

--
-- PostgreSQL database dump complete
--

