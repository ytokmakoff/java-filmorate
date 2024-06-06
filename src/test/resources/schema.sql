CREATE TABLE IF NOT EXISTS genres
(
    genre_id INT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(100) NOT NULL
    );

CREATE TABLE IF NOT EXISTS ratings
(
    rating_id INT AUTO_INCREMENT PRIMARY KEY,
    rating    VARCHAR(10) NOT NULL
    );

CREATE TABLE IF NOT EXISTS users
(
    user_id  INT AUTO_INCREMENT PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    email    VARCHAR(100) NOT NULL,
    login    VARCHAR(100) NOT NULL,
    birthday DATE
    );

CREATE TABLE IF NOT EXISTS films
(
    film_id      INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    description  VARCHAR(250),
    release_date DATE,
    duration     INT,
    rating_id    INT,
    FOREIGN KEY (rating_id) REFERENCES ratings (rating_id)
    );


CREATE TABLE IF NOT EXISTS films_genres
(
    film_genre_id INT AUTO_INCREMENT PRIMARY KEY,
    film_id       INT,
    genre_id      INT,
    FOREIGN KEY (film_id) REFERENCES films (film_id),
    FOREIGN KEY (genre_id) REFERENCES genres (genre_id)
    );

CREATE TABLE IF NOT EXISTS likes
(
    like_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    film_id INT,
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (film_id) REFERENCES films (film_id)
    );

CREATE TABLE IF NOT EXISTS friendship
(
    friendship_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id       INT,
    friend_id     INT,
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (friend_id) REFERENCES users (user_id)
    );