-- poloha bodu [0,0] je zvolena do leveho dolniho rohu mapy

DELETE FROM mapa;

INSERT INTO mapa VALUES (
	'Hotel',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 3), -- exterior rectangle (left-bottom, right-top)
		SDO_ORDINATE_ARRAY(50,500, 350,950)
	)
);

INSERT INTO mapa VALUES (
	'Služby u bazénu',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 1), -- exterior polygon (counterclockwise)
		SDO_ORDINATE_ARRAY(50,50, 350,50, 350,450, 50,450, 50,400, 250,400, 250,100, 50,100, 50,50)
	)
);

INSERT INTO mapa VALUES (
	'Bazén',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 3), -- exterior rectangle (left-bottom, right-top)
		SDO_ORDINATE_ARRAY(50,200, 200,350)
	)
);

INSERT INTO mapa VALUES (
	'Bar+Disko',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 3), -- exterior rectangle (left-bottom, right-top)
		SDO_ORDINATE_ARRAY(400,800, 700,950)
	)
);

INSERT INTO mapa VALUES (
	'Tenisové kurty',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 3), -- exterior rectangle (left-bottom, right-top)
		SDO_ORDINATE_ARRAY(400,500, 700,750)
	)
);


INSERT INTO mapa VALUES (
	'Wellness',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 3), -- exterior rectangle (left-bottom, right-top)
		SDO_ORDINATE_ARRAY(800,550, 950,950)
	)
);

INSERT INTO mapa VALUES (
	'Hlídání dětí',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 3), -- exterior rectangle (left-bottom, right-top)
		SDO_ORDINATE_ARRAY(800,400, 950,500)
	)
);

INSERT INTO mapa VALUES (
	'Golfové hřiště',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 1), -- exterior polygon (counterclockwise)
		SDO_ORDINATE_ARRAY(500,50, 950,50, 950,350, 650,350, 650,250, 500,250, 500,50)
	)
);


COMMIT;

-- kontrola validity (na zacatku "valid" muze byt cislo chyby, vizte http://www.ora-code.com/)
SELECT nazev, SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(geometrie, 1) valid -- 1=presnost
FROM mapa;