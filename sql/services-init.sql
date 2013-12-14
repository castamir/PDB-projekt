DROP TABLE sluzby_rezervace;
DROP TABLE zakaznik;
DROP TABLE sluzby;

/** zakaznik */
CREATE TABLE zakaznik (
  id NUMBER NOT null,
	jmeno VARCHAR(32) NOT null,
	CONSTRAINT pk_zakaznik PRIMARY KEY (id)
);

DROP SEQUENCE zakaznik_seq;

CREATE SEQUENCE zakaznik_seq
START WITH 1 INCREMENT BY 1;
/****************************************/

/** sluzby */
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
/****************************************/

/** rezervace sluzeb */
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
  CONSTRAINT uc_sluzby_rezervace_den_hodina UNIQUE (den,hodina)
);


COMMIT;

create or replace trigger zakaznik_insert
before insert on zakaznik
for each row
begin
    select zakaznik_seq.nextval into :new.id from dual;
end;
/

COMMIT;

create or replace trigger sluzby_rezervace_insert
before insert on sluzby_rezervace
for each row
begin
    select sluzby_rezervace_seq.nextval into :new.id from dual;
end;
/

COMMIT;


/****** init data */
INSERT INTO zakaznik (jmeno) VALUES ('Mira');
INSERT INTO zakaznik (jmeno) VALUES ('Tom');
INSERT INTO zakaznik (jmeno) VALUES ('Pavel');

INSERT INTO sluzby (nazev, objekt, dostupnost_od, dostupnost_do)
VALUES ('Tenisov� kurty', 'Tenisov� kurty', 8, 18);

INSERT INTO sluzby (nazev, objekt, dostupnost_od, dostupnost_do)
VALUES ('Wellness', 'Wellness', 12, 18);

INSERT INTO sluzby (nazev, objekt, dostupnost_od, dostupnost_do)
VALUES ('Hl�d�n� d�t�', 'Hl�d�n� d�t�', 6, 14);

INSERT INTO sluzby (nazev, objekt, dostupnost_od, dostupnost_do)
VALUES ('Golfov� h�i�t�', 'Golfov� h�i�t�', 11, 17);

INSERT INTO sluzby_rezervace (sluzba, zakaznik, den, hodina)
VALUES ('Tenisov� kurty', 2, TO_DATE('2013-12-13', 'yyyy-mm-dd'), '11');

INSERT INTO sluzby_rezervace (sluzba, zakaznik, den, hodina)
VALUES ('Tenisov� kurty', 1, '14.12.13', 12);

INSERT INTO sluzby_rezervace (sluzba, zakaznik, den, hodina)
VALUES ('Tenisov� kurty', 3, '13.12.13', 14);

COMMIT;

select * from zakaznik;
select * from areal;
select * from sluzby;
select * from sluzby_rezervace;