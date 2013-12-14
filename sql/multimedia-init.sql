DROP TABLE obrazky;

/** zakaznik */
CREATE TABLE obrazky (
  	id NUMBER NOT null primary key,
	img ORDSYS.ORDImage,
	img_si ORDSYS.SI_StillImage,
	img_ac ORDSYS.SI_AverageColor,
	img_ch ORDSYS.SI_ColorHistogram,
	img_pc ORDSYS.SI_PositionalColor,
	img_tx ORDSYS.SI_Texture
);

DROP SEQUENCE obrazky_seq;

CREATE SEQUENCE obrazky_seq
START WITH 1 INCREMENT BY 1;
/****************************************/

COMMIT;

create or replace trigger obrazky_insert
before insert on zakaznik
for each row
begin
    select obrazky_seq.nextval into :new.id from dual;
end;
/

COMMIT;
