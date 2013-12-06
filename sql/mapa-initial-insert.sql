-- poloha bodu [0,0] je zvolena do leveho dolniho rohu mapy

DELETE FROM mapa;

INSERT INTO mapa VALUES (
	'Hotel',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 3), -- exterior rectangle (left-up, right-bottom)
		SDO_ORDINATE_ARRAY(20,20, 140,200)
	)
);

INSERT INTO mapa VALUES (
	'Služby u bazénu',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 1), -- exterior polygon (counterclockwise)
		SDO_ORDINATE_ARRAY(20,220, 140,220, 140,380, 20,380, 20,360, 100,360, 100,240, 20,240, 20,220)
	)
);

INSERT INTO mapa VALUES (
	'Bazén',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 3), -- exterior rectangle (left-bottom, right-top)
		SDO_ORDINATE_ARRAY(20,260, 80,340)
	)
);

INSERT INTO mapa VALUES (
	'Bar+Disko',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 3), -- exterior rectangle (left-bottom, right-top)
		SDO_ORDINATE_ARRAY(160,20, 280,80)
	)
);

INSERT INTO mapa VALUES (
	'Tenisové kurty',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 3), -- exterior rectangle (left-bottom, right-top)
		SDO_ORDINATE_ARRAY(160,100, 280,200)
	)
);


INSERT INTO mapa VALUES (
	'Wellness',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 3), -- exterior rectangle (left-bottom, right-top)
		SDO_ORDINATE_ARRAY(320,20, 380,180)
	)
);

INSERT INTO mapa VALUES (
	'Hlídání dětí',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 3), -- exterior rectangle (left-bottom, right-top)
		SDO_ORDINATE_ARRAY(320,200, 380,240)
	)
);

INSERT INTO mapa VALUES (
	'Golfové hřiště',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 1), -- exterior polygon (counterclockwise)
		SDO_ORDINATE_ARRAY(280,260, 380,260, 380,380, 200,380, 200,300, 280,300, 280,260)
	)
);


COMMIT;

-- kontrola validity (na zacatku "valid" muze byt cislo chyby, vizte http://www.ora-code.com/)
SELECT nazev, SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(geometrie, 1) valid -- 1=presnost
FROM mapa;