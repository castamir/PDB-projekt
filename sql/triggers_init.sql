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
/

COMMIT;

create or replace trigger rezervace_insert
before insert on rezervace
for each row
begin
    select rezervace_seq.nextval into :new.id from dual;
end;
/

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
