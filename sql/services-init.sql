DROP TABLE sluzby;

CREATE TABLE sluzby (
	nazev VARCHAR(32),
	objekt VARCHAR(32),
	dostupnost_od NUMBER,
	dostupnost_do NUMBER,
	CONSTRAINT pk_nazev
	PRIMARY KEY (nazev),
	CONSTRAINT fk_objekt
	FOREIGN KEY (objekt)
	REFERENCES areal (nazev)
);


DROP TABLE sluzby_rezervace;
DROP SEQUENCE sluzby_rezervace_seq;

CREATE SEQUENCE sluzby_rezervace_seq
START WITH 1 INCREMENT BY 1;


CREATE TABLE sluzby_rezervace (
	id NUMBER,
	sluzba VARCHAR(32),
	zakaznik VARCHAR(32),
	zacatek DATE,
	konec DATE,
	CONSTRAINT pk_id
	PRIMARY KEY (id),
	CONSTRAINT fk_sluzba
	FOREIGN KEY (sluzba)
	REFERENCES sluzby(nazev)
);

COMMIT;