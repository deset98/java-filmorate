INSERT INTO genres (genre_name)
SELECT 'Комедия'
WHERE NOT EXISTS (SELECT genre_name FROM genres WHERE genre_name = 'Комедия');

INSERT INTO genres (genre_name)
SELECT 'Драма'
WHERE NOT EXISTS (SELECT genre_name FROM genres WHERE genre_name = 'Драма');

INSERT INTO genres (genre_name)
SELECT 'Мультфильм'
WHERE NOT EXISTS (SELECT genre_name FROM genres WHERE genre_name = 'Мультфильм');

INSERT INTO genres (genre_name)
SELECT 'Триллер'
WHERE NOT EXISTS (SELECT genre_name FROM genres WHERE genre_name = 'Триллер');

INSERT INTO genres (genre_name)
SELECT 'Документальный'
WHERE NOT EXISTS (SELECT genre_name FROM genres WHERE genre_name = 'Документальный');

INSERT INTO genres (genre_name)
SELECT 'Боевик'
WHERE NOT EXISTS (SELECT genre_name FROM genres WHERE genre_name = 'Боевик');


INSERT INTO mpa (mpa_name, description)
SELECT 'G', 'У фильма нет возрастных ограничений'
WHERE NOT EXISTS (SELECT mpa_name FROM mpa WHERE mpa_name = 'G');

INSERT INTO mpa (mpa_name, description)
SELECT 'PG', 'Детям рекомендуется смотреть фильм с родителями'
WHERE NOT EXISTS (SELECT mpa_name FROM mpa WHERE mpa_name = 'PG');

INSERT INTO mpa (mpa_name, description)
SELECT 'PG-13', 'Детям до 13 лет просмотр не желателен'
WHERE NOT EXISTS (SELECT mpa_name FROM mpa WHERE mpa_name = 'PG-13');

INSERT INTO mpa (mpa_name, description)
SELECT 'R', 'Лицам до 17 лет просматривать фильм можно только в присутствии взрослого'
WHERE NOT EXISTS (SELECT mpa_name FROM mpa WHERE mpa_name = 'R');

INSERT INTO mpa (mpa_name, description)
SELECT 'NC-17', 'Лицам до 18 лет просмотр запрещён'
WHERE NOT EXISTS (SELECT mpa_name FROM mpa WHERE mpa_name = 'NC-17');