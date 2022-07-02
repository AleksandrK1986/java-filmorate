# java-filmorate
![ER_Filmorate](https://github.com/AleksandrK1986/java-filmorate/blob/main/ER_Filmorate.png)

Небольшое пояснение к ER диаграмме.
- Две основные таблицы users и films.
- Вспомогательная таблица likes для связи id фильма и id пользователя.
- Отдельная таблица relationship для хранения связей дружбы и статуса по ней. 
Планирую при формировании запроса на дружбу в этой таблице создавать 
сразу две строки: первая для пользователя направившего приглашения на дружбу 
(статус будет подтвержденный - true) и вторая для того кому направили (статус будет не подтверждена 
false, до момента подтверждения).
- Рейтинг и жанр оформлен в виде Enum (ниже приведены их значения).
Также ниже приведены примеры некоторых запросов.

Enum genre_type {
comedy
drama
cartoon
thriller
documentary
fighter
}

Enum rating_type {
g
pg
gp_13
r
nc_17
}

-- getAllFilms:
SELECT *
FROM films;

-- getAllUsers:
SELECT *
FROM users;

-- getTopNFilms:
SELECT *
FROM films
ORDER BY rating DESC
LIMIT N;

-- getCommonFriends:
SELECT
r.friend_id
FROM relationship AS r
JOIN relationship AS rr ON r.friend_id = rr.friend_id
WHERE r.user_id = userId
AND rr.user_id = otherUserId;  

