-- liquibase formatted sql

-- changeset hakan-aslim:insert-initial-record-recipe-1
INSERT INTO RECIPE (ID, NAME, DESCRIPTION, SERVANT, COURSE, SPECIAL_DIET, CREATED_BY, CREATE_DATE)
VALUES ((SELECT uuid_in(md5(random()::text || random()::text)::cstring)),
        'Baklava',
        'Cevizli Baklava',
        '1 serving',
        'DESSERT',
        'VEGETARIAN',
        'ADMIN',
        CURRENT_DATE);

INSERT INTO RECIPE_INGREDIENT (ID, RECIPE_ID, TEXT)
VALUES ((SELECT uuid_in(md5(random()::text || random()::text)::cstring)),
        (SELECT MAX(ID) FROM RECIPE WHERE NAME = 'Baklava'),
        'juice');

INSERT INTO RECIPE_INGREDIENT (ID, RECIPE_ID, TEXT)
VALUES ((SELECT uuid_in(md5(random()::text || random()::text)::cstring)),
        (SELECT MAX(ID) FROM RECIPE WHERE NAME = 'Baklava'),
        'coconut');

INSERT INTO RECIPE_INGREDIENT (ID, RECIPE_ID, TEXT)
VALUES ((SELECT uuid_in(md5(random()::text || random()::text)::cstring)),
        (SELECT MAX(ID) FROM RECIPE WHERE NAME = 'Baklava'),
        'ice');

INSERT INTO RECIPE_INSTRUCTION (ID, RECIPE_ID, TEXT)
VALUES ((SELECT uuid_in(md5(random()::text || random()::text)::cstring)),
        (SELECT MAX(ID) FROM RECIPE WHERE NAME = 'Baklava'),
        'Blend 2 cups of pineapple juice and 5/8 cup cream of coconut until smooth.');

INSERT INTO RECIPE_INSTRUCTION (ID, RECIPE_ID, TEXT)
VALUES ((SELECT uuid_in(md5(random()::text || random()::text)::cstring)),
        (SELECT MAX(ID) FROM RECIPE WHERE NAME = 'Baklava'),
        'Fill a glass with ice.');

INSERT INTO RECIPE_INSTRUCTION (ID, RECIPE_ID, TEXT)
VALUES ((SELECT uuid_in(md5(random()::text || random()::text)::cstring)),
        (SELECT MAX(ID) FROM RECIPE WHERE NAME = 'Baklava'),
        'Pour the pineapple juice and coconut mixture over ice.');



