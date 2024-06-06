INSERT INTO genres(name)
VALUES ('Comedy'),
       ('Drama'),
       ('Cartoon'),
       ('Thriller'),
       ('Documentary'),
       ('Action');

INSERT INTO RATINGS(RATING)
VALUES ('G'),
       ('PG'),
       ('PG13'),
       ('R'),
       ('NC17');

INSERT INTO FILMS(name, description, release_date, duration)
VALUES ('test name', 'test description', '2000-10-9', 100);

INSERT INTO FILMS(name, description, release_date, duration, rating_id)
VALUES ('test name2', 'test description2', '1923-8-10', 100, 2);

INSERT INTO USERS(name, email, login, birthday)
VALUES ('name', 'email', 'login', '2000-1-1');

INSERT INTO USERS(NAME, EMAIL, LOGIN, BIRTHDAY)
VALUES ('name2', 'email2', 'login2', '2045-1-1');

INSERT INTO USERS(NAME, EMAIL, LOGIN, BIRTHDAY)
VALUES ('name3', 'email', 'login', '2012-12-2');