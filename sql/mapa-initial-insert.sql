-- poloha bodu [0,0] je zvolena do leveho dolniho rohu mapy

DELETE FROM mapa;
/*
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
		SDO_ELEM_INFO_ARRAY(1, 1003, 1), -- exterior polygon (clockwise)
		SDO_ORDINATE_ARRAY(20,220, 140,220, 140,380, 20,380, 20,360, 100,360, 100,240, 20,240, 20,220)
	)
);

INSERT INTO mapa VALUES (
	'Bazén',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 3), -- exterior rectangle (left-up, right-bottom)
		SDO_ORDINATE_ARRAY(20,260, 80,340)
	)
);

INSERT INTO mapa VALUES (
	'Bar+Disko',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 3), -- exterior rectangle (left-up, right-bottom)
		SDO_ORDINATE_ARRAY(160,20, 280,80)
	)
);

INSERT INTO mapa VALUES (
	'Tenisové kurty',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 3), -- exterior rectangle (left-up, right-bottom)
		SDO_ORDINATE_ARRAY(160,100, 280,200)
	)
);


INSERT INTO mapa VALUES (
	'Wellness',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 3), -- exterior rectangle (left-up, right-bottom)
		SDO_ORDINATE_ARRAY(320,20, 380,180)
	)
);

INSERT INTO mapa VALUES (
	'Hlídání dětí',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 3), -- exterior rectangle (left-up, right-bottom)
		SDO_ORDINATE_ARRAY(320,200, 380,240)
	)
);

INSERT INTO mapa VALUES (
	'Golfové hřiště',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 1), -- exterior polygon (clockwise)
		SDO_ORDINATE_ARRAY(280,260, 380,260, 380,380, 200,380, 200,300, 280,300, 280,260)
	)
);
*/
/*      od 8 bodu - 
        162,100, 187,100, 200,75,
        237,75, 237,62, 287,62, 300,50, 325,50, 350,37, 362,37, 350,25, 350,0, 362,0, 400,12, 400,37,
        412,37, 412,50,*/
INSERT INTO mapa VALUES (
	'Karlovarsky',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 1), -- exterior polygon (clockwise)
		SDO_ORDINATE_ARRAY(
        12,125, 25,112, 37,112, 50,138, 87,112, 112,112, 137,100, 150,112, 175,137, 187,137, 187,200,
        175,225, 162,212, 150,212, 125,237, 112,225, 100,225, 87,237, 50,200, 25,162, 25,150, 12,125 
        )
	)
);

INSERT INTO mapa VALUES (
	'Ustecky',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 1), -- exterior polygon (clockwise)
		SDO_ORDINATE_ARRAY(
        150,112, 162,100, 187,100, 200,75, 237,75, 237,62, 287,62, 300,50, 325,50, 350,37, 362,37, 350,25, 
        350,0, 362,0, 400,12, 400,37, 412,37, 412,50, 362,87, 362,112, 375,120, 362,150, 350,162, 300,162,
        225,187, 200,200, 187,200, 187,137, 175,137, 150,112
        )
	)
);

INSERT INTO mapa VALUES (
	'Liberecky',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 1), -- exterior polygon (clockwise)
		SDO_ORDINATE_ARRAY(
        412,50, 450,50, 450,25, 487,25, 500,50, 500,62, 512,75, 537,87, 537,150, 475,150, 437,125, 425,125,
        412,137, 387,137, 375,120, 362,112, 362,87, 412,50
        )
	)
);

INSERT INTO mapa VALUES (
	'Kralovehradecky',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 1), -- exterior polygon (clockwise)
		SDO_ORDINATE_ARRAY(
        537,87, 562,100, 575,100, 575,112, 587,112, 600,125, 637,125, 650,150, 625,175, 625,187, 637,187, 
        675,250, 650,250, 625,275, 562,237, 500,237, 487,225, 487,200, 462,187, 450,175, 475,150, 537,150,
        537,87
        )
	)
);

INSERT INTO mapa VALUES (
	'Pardubicky',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 1), -- exterior polygon (clockwise)
		SDO_ORDINATE_ARRAY(
        675,250, 687,250, 712,225, 675,325, 687,350, 687,375, 625,375, 562,337, 550,337, 500,300, 500,275,
        475,250, 500,237, 562,237, 625,275, 650,250, 675,250
        )
	)
);

INSERT INTO mapa VALUES (
	'Olomoucky',
	SDO_GEOMETRY(2003, NULL, NULL, -- 2D polygon
		SDO_ELEM_INFO_ARRAY(1, 1003, 1), -- exterior polygon (clockwise)
		SDO_ORDINATE_ARRAY(
        712,225, 712,200, 700,187, 712,175, 787,225, 787,237, 762,262, 750,287, 750,325, 800,350, 837,387,
        837,412, 775,437, 762,437, 725,450, 700,412, 687,425, 675,412, 675,387, 687,375, 687,350, 675,325, 
        712,225
        )
	)
);
--787,225
COMMIT;

-- kontrola validity (na zacatku "valid" muze byt cislo chyby, vizte http://www.ora-code.com/)
SELECT nazev, SDO_GEOM.VALIDATE_GEOMETRY_WITH_CONTEXT(geometrie, 1) valid -- 1=presnost
FROM mapa;