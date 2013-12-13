DROP TABLE areal;

CREATE TABLE areal (
	nazev VARCHAR(32),
	geometrie SDO_GEOMETRY,
	CONSTRAINT pk_nazev
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

DROP INDEX areal_geometrie_sidx;
CREATE INDEX areal_geometrie_sidx ON areal(geometrie) indextype is MDSYS.SPATIAL_INDEX;

COMMIT;

-- kontrola validity (na zacatku "valid" muze byt cislo chyby, vizte http://www.ora-code.com/)
-- s udanim presnosti
SELECT nazev, SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(geometrie, 1) valid -- 1=presnost
FROM areal;
-- bez udani presnosti (presne dle nastaveni v metadatech)
SELECT m.nazev, m.geometrie.ST_IsValid()
FROM areal m;