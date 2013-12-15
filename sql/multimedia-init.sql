DROP TABLE obrazky;

/** zakaznik */
CREATE TABLE obrazky (
  	id NUMBER NOT null,
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

CREATE OR REPLACE PROCEDURE Rotate_image
    (img_id IN NUMBER)
IS
    obj ORDSYS.ORDImage;

BEGIN
    SELECT img INTO obj FROM obrazky
    WHERE id = img_id FOR UPDATE;

    obj.process('rotate=90');

    UPDATE obrazky SET img = obj WHERE id = img_id;

    COMMIT;
END;

COMMIT;
