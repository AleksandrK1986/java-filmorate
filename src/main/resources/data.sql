MERGE INTO "genre_type"
KEY ("id")
VALUES (1, 'Комедия'), (2, 'Драма'), (3, 'Мультфильм'), (4, 'Триллер'), (5, 'Документальный'), (6, 'Боевик');

MERGE INTO "rating_mpa"
KEY ("id")
VALUES (1, 'g'), (2, 'pg'), (3, 'gp-13'), (4, 'r'), (5, 'nc-17');