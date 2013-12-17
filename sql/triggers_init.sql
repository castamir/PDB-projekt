create or replace trigger obrazky_insert
before insert on obrazky
for each row
begin
    select obrazky_seq.nextval into :new.id from dual;
end;
/

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

create or replace trigger rezervace_insert
before insert on rezervace
for each row
begin
    select rezervace_seq.nextval into :new.id from dual;
end;
/

create or replace trigger zakaznik_insert_i
before insert on zakaznik
for each row
begin
    select zak_seq.nextval into :new.id from dual;
end;
/

create or replace trigger sluzby_rezervace_insert
before insert on sluzby_rezervace
for each row
begin
    select sluzby_rezervace_seq.nextval into :new.id from dual;
end;
/

CREATE OR REPLACE PROCEDURE rezervace_smaz_v_obdobi (
  datum_od DATE,
  datum_do DATE
) IS
new_end DATE;
new_start DATE;
tmp_zakaznik NUMBER;
tmp_pokoj NUMBER;
tmp_od DATE;
tmp_do DATE;
tmp_id NUMBER;
CURSOR cursor1 IS
	SELECT id, zakaznik, pokoj, od, do
	FROM rezervace WHERE od < datum_od AND do > datum_do;
CURSOR cursor2 IS
	SELECT id, zakaznik, pokoj, od, do
	FROM rezervace WHERE od < datum_do AND do > datum_od;

BEGIN
    IF datum_od<=datum_do THEN
        new_end := datum_od - 1;
        new_start := datum_do + 1;
    
        DELETE FROM rezervace WHERE od >= datum_od AND do <= datum_do;
        UPDATE rezervace
            SET do = new_end
            WHERE od < datum_od AND (do BETWEEN datum_od AND datum_do);
        UPDATE rezervace
            SET od = new_start
            WHERE do > datum_do AND (od BETWEEN datum_od AND datum_do);

        OPEN cursor1;
        LOOP
            FETCH cursor1	
                INTO tmp_id, tmp_zakaznik, tmp_pokoj, tmp_od, tmp_do;

            EXIT WHEN cursor1%NOTFOUND;

            INSERT INTO rezervace (zakaznik, pokoj, od, do)
                VALUES(tmp_zakaznik, tmp_pokoj, new_start, tmp_do);

            UPDATE rezervace
                SET do = new_end
                WHERE id = tmp_id;
        END LOOP;
        CLOSE cursor1;
    ELSE
        new_end := datum_do - 1;
        new_start := datum_od + 1;
        
        DELETE FROM rezervace WHERE od >= datum_do AND do <= datum_od;
        UPDATE rezervace
            SET do = new_end
            WHERE od < datum_do AND (do BETWEEN datum_do AND datum_od);
        UPDATE rezervace
            SET od = new_start
            WHERE do > datum_od AND (od BETWEEN datum_do AND datum_od);

        OPEN cursor2;
        LOOP
            FETCH cursor2	
                INTO tmp_id, tmp_zakaznik, tmp_pokoj, tmp_od, tmp_do;

            EXIT WHEN cursor2%NOTFOUND;

            INSERT INTO rezervace (zakaznik, pokoj, od, do)
                VALUES(tmp_zakaznik, tmp_pokoj, new_start, tmp_do);

            UPDATE rezervace
                SET do = new_end
                WHERE id = tmp_id;
        END LOOP;
        CLOSE cursor2;
    END IF;
    COMMIT;
END;
/
