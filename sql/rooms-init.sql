DROP TABLE rooms;

CREATE TABLE rooms (
	nazev VARCHAR(32),
	geometrie SDO_GEOMETRY
);

-- nazvy tabulky a sloupce musi byt velkymi pismeny
DELETE FROM USER_SDO_GEOM_METADATA WHERE
	TABLE_NAME = 'ROOMS' AND COLUMN_NAME = 'GEOMETRIE';

INSERT INTO USER_SDO_GEOM_METADATA VALUES (
	'rooms', 'geometrie',
	-- souradnice X,Y s hodnotami 0-30 a presnosti 1 bod
	SDO_DIM_ARRAY(SDO_DIM_ELEMENT('X', 0, 30, 1), SDO_DIM_ELEMENT('Y', 0, 30, 1)),
	-- lokalni (negeograficky) souradnicovy system (v analytickych fcich neuvadet jednotky)
	NULL
);

COMMIT;

-- kontrola validity (na zacatku "valid" muze byt cislo chyby, vizte http://www.ora-code.com/)
-- s udanim presnosti
SELECT nazev, SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(geometrie, 1) valid -- 1=presnost
FROM rooms;
-- bez udani presnosti (presne dle nastaveni v metadatech)
SELECT m.nazev, m.geometrie.ST_IsValid()
FROM rooms m;