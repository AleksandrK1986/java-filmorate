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

Ольга Екименко : Насчет запроса по подсчету рейтинга. Как я поняла из схемы поле rating таблицы FILMS хранит возрастные категории, и по ним нельзя посчитать рейтинг фильма 
Мне кажется лишним поле relationship_id в таблице USERS. Ведь в таблице relationsip  есть user_id  и по нему можно определить всех друзей. И у пользователя может быть много друзей, тогда в поле relationship_id будет несколько записей?
Поле bithday можно сделать просто date, чаще всего хранят именно дату рождения, а не время
И так же поле  release_date наверное дата, а не точное время (edited) 
Мне очень понравилось Ваше решение по запросу поиска общих друзей. Я нагородила у себя подзапросы.

Александр Кучинский
По пунктам:
Поле rating таблицы films переименовал в rating_mpa, чтобы было понятно речь про рейтинг именно по возрастным категорями из Enum. Рейтинг фильмов по популярности буду высчитваться из количества лайков, отдельного поля для этого не требуется.
Согласен что от поля relationship_id в таблице users нужно избавиться. А вот в самой таблице relationship этот id оставлю, мне кажется на развитие типа связи "дружба" должно будет понадобиться (понимать ситуации когда были другом, потом перестали, потом снова пришел запрос на дружбу, понимать последовательность запросов (кто кому прислал) и т.д.). В этой таблице по одному запросу дружбы буду создавать сразу две записи (например при запросе на дружбу от Васи к Пете: id=1, Вася, Петя, Подтверждено;  id=2, Петя, Вася, Не подтверждено. 
Дату рождения и дату релиза сделал именно датой, согласен
"понравилось Ваше решение по запросу поиска общих друзей" - спринт 11 покажет, заработает это или нет =)
(edited)
Ольга Екименко
По 2 пункту. relationship_id я считаю даже необходим - он является в этой таблице ключом.

Александр Кучинский
думал сделать составным, да, но отдельный id лучше - согласен

Ольга Екименко
И насчет пункта 1 . При тестировании 9 спринта, в запросах были фильмы уже с проставленным рейтингом. Возможно в 11 эту графу надо будет добавить и рейтинг будет как-то по-другому высчитываться
