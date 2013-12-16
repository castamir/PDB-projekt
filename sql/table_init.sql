DROP TABLE sluzby_rezervace CASCADE CONSTRAINTS;
DROP TABLE sluzby CASCADE CONSTRAINTS;
DROP TABLE areal CASCADE CONSTRAINTS;
DROP TABLE rezervace CASCADE CONSTRAINTS;
DROP TABLE obrazky CASCADE CONSTRAINTS;
DROP TABLE zakaznik CASCADE CONSTRAINTS;
DROP TABLE pokoje CASCADE CONSTRAINTS;
DROP TABLE mapa CASCADE CONSTRAINTS;

--==========================================
-- mapa
--==========================================
CREATE TABLE mapa (
	nazev VARCHAR(32),
	geometrie SDO_GEOMETRY
);

-- nazvy tabulky a sloupce musi byt velkymi pismeny
DELETE FROM USER_SDO_GEOM_METADATA WHERE
	TABLE_NAME = 'MAPA' AND COLUMN_NAME = 'GEOMETRIE';

INSERT INTO USER_SDO_GEOM_METADATA VALUES (
	'mapa', 'geometrie',
	-- souradnice X,Y s hodnotami 0-30 a presnosti 1 bod
	SDO_DIM_ARRAY(SDO_DIM_ELEMENT('X', 0, 30, 1), SDO_DIM_ELEMENT('Y', 0, 30, 1)),
	-- lokalni (negeograficky) souradnicovy system (v analytickych fcich neuvadet jednotky)
	NULL
);

COMMIT;

-- kontrola validity (na zacatku "valid" muze byt cislo chyby, vizte http://www.ora-code.com/)
-- s udanim presnosti
SELECT nazev, SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(geometrie, 1) valid -- 1=presnost
FROM mapa;
-- bez udani presnosti (presne dle nastaveni v metadatech)
SELECT m.nazev, m.geometrie.ST_IsValid()
FROM mapa m;


--==========================================
-- pokoje
--==========================================
CREATE TABLE pokoje (
	id NUMBER NOT null,
	nazev VARCHAR(32),
	CONSTRAINT pk_pokoje PRIMARY KEY (id)
);
--==========================================
-- zakaznik
--==========================================

CREATE TABLE zakaznik (
  id NUMBER NOT null,
	jmeno VARCHAR(32) NOT null,
	prijmeni VARCHAR(32) NOT null,
	adresa VARCHAR(32) NOT null,
	mesto VARCHAR(32) NOT null,
	psc VARCHAR(32) NOT null,
	kraj VARCHAR(32) NOT null,
	telefon VARCHAR(32) NOT null,
	email VARCHAR(32) NOT null,
	CONSTRAINT pk_zakaznik PRIMARY KEY (id)
);

DROP SEQUENCE zakaznik_seq;

CREATE SEQUENCE zakaznik_seq
START WITH 1001 INCREMENT BY 1;
--==========================================
-- obrazky
--==========================================
CREATE TABLE obrazky (
  	id NUMBER NOT null,
  	zakaznik NUMBER NOT null,
	img ORDSYS.ORDImage,
	img_si ORDSYS.SI_StillImage,
	img_ac ORDSYS.SI_AverageColor,
	img_ch ORDSYS.SI_ColorHistogram,
	img_pc ORDSYS.SI_PositionalColor,
	img_tx ORDSYS.SI_Texture,
	CONSTRAINT pk_obrazky PRIMARY KEY (id),
	CONSTRAINT fk_obr_zakaznik FOREIGN KEY (zakaznik) REFERENCES zakaznik(id) on delete cascade
);

DROP SEQUENCE obrazky_seq;

CREATE SEQUENCE obrazky_seq
START WITH 1 INCREMENT BY 1;

COMMIT;
--==========================================
-- rezervace
--==========================================
CREATE TABLE rezervace (
  	id NUMBER NOT null,
	zakaznik NUMBER NOT null,
	pokoj NUMBER NOT null,
	od DATE NOT null,
	do DATE NOT null,
	CONSTRAINT pk_rezervace PRIMARY KEY (id),
	CONSTRAINT fk_rezervace_zakaznik FOREIGN KEY (zakaznik) REFERENCES zakaznik(id) on delete cascade,
	CONSTRAINT fk_rezervace_pokoj FOREIGN KEY (pokoj) REFERENCES pokoje(id) on delete cascade,
	CONSTRAINT uc_rezervace_pokoj_od_do UNIQUE (pokoj,od,do)
);

DROP SEQUENCE rezervace_seq;

CREATE SEQUENCE rezervace_seq
START WITH 1 INCREMENT BY 1;
--==========================================
-- areal
--==========================================
CREATE TABLE areal (
	nazev VARCHAR(32),
	geometrie SDO_GEOMETRY,
	CONSTRAINT pk_areal_nazev
	PRIMARY KEY (nazev)
);

-- nazvy tabulky a sloupce musi byt velkymi pismeny
DELETE FROM USER_SDO_GEOM_METADATA WHERE
	TABLE_NAME = 'AREAL' AND COLUMN_NAME = 'GEOMETRIE';

INSERT INTO USER_SDO_GEOM_METADATA VALUES (
	'areal', 'geometrie',
	-- souradnice X,Y s hodnotami 0-30 a presnosti 1 bod
	SDO_DIM_ARRAY(SDO_DIM_ELEMENT('X', 0, 30, 1), SDO_DIM_ELEMENT('Y', 0, 30, 1)),
	-- lokalni (negeograficky) souradnicovy system (v analytickych fcich neuvadet jednotky)
	NULL
);

CREATE INDEX areal_geometrie_sidx ON areal(geometrie) indextype is MDSYS.SPATIAL_INDEX;

COMMIT;

-- kontrola validity (na zacatku "valid" muze byt cislo chyby, vizte http://www.ora-code.com/)
-- s udanim presnosti
SELECT nazev, SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(geometrie, 1) valid -- 1=presnost
FROM areal;
-- bez udani presnosti (presne dle nastaveni v metadatech)
SELECT m.nazev, m.geometrie.ST_IsValid()
FROM areal m;
--==========================================
-- sluzby
--==========================================
CREATE TABLE sluzby (
	nazev VARCHAR(32) NOT null,
	objekt VARCHAR(32) NOT null,
	dostupnost_od NUMBER NOT null,
	dostupnost_do NUMBER NOT null,
	CONSTRAINT pk_sluzby_nazev PRIMARY KEY (nazev),
	CONSTRAINT fk_sluzby_objekt FOREIGN KEY (objekt) REFERENCES areal (nazev) on delete cascade
);

DROP SEQUENCE sluzby_rezervace_seq;

CREATE SEQUENCE sluzby_rezervace_seq
START WITH 1 INCREMENT BY 1;
--==========================================
-- sluzby_rezervace
--==========================================
CREATE TABLE sluzby_rezervace (
	id NUMBER NOT null,
	sluzba VARCHAR(32) NOT null,
	zakaznik INT NOT null,
	den DATE NOT null,
	hodina INT NOT null,
	poznamka VARCHAR(250),
	CONSTRAINT pk_sluzby_rezervace_id PRIMARY KEY (id),
	CONSTRAINT fk_sluzby_rezervace_sluzba FOREIGN KEY (sluzba) REFERENCES sluzby(nazev) on delete cascade,
	CONSTRAINT fk_sluzby_rezervace_zakaznik FOREIGN KEY (zakaznik) REFERENCES zakaznik(id) on delete cascade,
  CONSTRAINT uc_sluzby_rezervace_den_hodina UNIQUE (den,hodina,sluzba)
);

