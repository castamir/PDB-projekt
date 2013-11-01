DROP TABLE mapa;

CREATE TABLE mapa (
	nazev VARCHAR(32),
	geometrie SDO_GEOMETRY
);

-- nazvy tabulky a sloupce musi byt velkymi pismeny
DELETE FROM USER_SDO_GEOM_METADATA WHERE
	TABLE_NAME = 'MAPA' AND COLUMN_NAME = 'GEOMETRIE';

INSERT INTO USER_SDO_GEOM_METADATA VALUES (
	'mapa', 'geometrie',
	-- souradnice X,Y s hodnotami 0-150 a presnosti 0.1 bod (velikost mrizky a hustota budu v planu z prikladu, napr. kulate rohy komunikace s presnosti 1 bod a stromy v zeleni s presnosti 0.1 bod)
	SDO_DIM_ARRAY(SDO_DIM_ELEMENT('X', 0, 150, 0.1), SDO_DIM_ELEMENT('Y', 0, 150, 0.1)),
	-- lokalni (negeograficky) souradnicovy system (v analytickych fcich neuvadet jednotky)
	NULL
);

COMMIT;

-- kontrola validity (na zacatku "valid" muze byt cislo chyby, vizte http://www.ora-code.com/)
-- s udanim presnosti
SELECT nazev, SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(geometrie, 0.1) valid -- 0.1=presnost
FROM mapa;
-- bez udani presnosti (presne dle nastaveni v metadatech)
SELECT m.nazev, m.geometrie.ST_IsValid()
FROM mapa m;

quit