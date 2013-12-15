

create or replace trigger obrazky_insert
before insert on zakaznik
for each row
begin
    select obrazky_seq.nextval into :new.id from dual;
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
