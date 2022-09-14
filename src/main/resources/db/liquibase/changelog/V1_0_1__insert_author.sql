-- liquibase formatted sql

-- changeset hakan-aslim:insert-initial-record-author-1
INSERT INTO AUTHOR (ID, FULL_NAME, CREATED_BY, CREATE_DATE)
VALUES ((SELECT uuid_in(md5(random()::text || random()::text)::cstring)), 'Mehmet Gurs', 'admin', CURRENT_DATE);

INSERT INTO AUTHOR (ID, FULL_NAME, CREATED_BY, CREATE_DATE)
VALUES ((SELECT uuid_in(md5(random()::text || random()::text)::cstring)), 'Ali Gungormus', 'admin', CURRENT_DATE);
