# java-filmorate
![ER_Filmorate](https://github.com/AleksandrK1986/java-filmorate/blob/main/ER_Filmorate_v3.png)

Небольшое пояснение к ER диаграмме.
- Две основные таблицы users и films.
- Вспомогательная таблица likes для связи id фильма и id пользователя.
- Отдельная таблица relationship для хранения связей дружбы и статуса по ней. 
Планирую при формировании запроса на дружбу в этой таблице создавать 
сразу две строки: первая для пользователя направившего приглашения на дружбу 
(статус будет подтвержденный - true) и вторая для того кому направили (статус будет не подтверждена 
false, до момента подтверждения).
- Рейтинг и жанр оформлены в виде отдельных таблиц.
Также ниже приведены примеры некоторых запросов.
- 
-- getAllFilms:
SELECT *
FROM films;

-- getAllUsers:
SELECT *
FROM users;

-- getTopNFilms:
SELECT 
    f.name,
    COUNT(l.film_id) AS count_likes
FROM likes AS l
LEFT JOIN films AS f ON l.film_id = f.id
GROUP BY f.name
ORDER BY count_likes DESC
LIMIT N;

-- getCommonFriends:
SELECT
r.friend_id
FROM relationship AS r
JOIN relationship AS rr ON r.friend_id = rr.friend_id
WHERE r.user_id = userId
AND rr.user_id = otherUserId;  

