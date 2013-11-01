DROP TABLE mapa;

CREATE TABLE mapa (
	nazev VARCHAR(32),
	geometrie SDO_GEOMETRY
);

COMMIT;

quit