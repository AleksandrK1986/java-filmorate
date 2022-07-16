package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingMPA {
    @NonNull
    int id;
    public String getName(){
        String name = null;
        switch (this.id){
            case 1: name = "G"; break;
            case 2: name = "PG"; break;
            case 3: name = "PG-13"; break;
            case 4: name = "R"; break;
            case 5: name = "NC-17"; break;
        }
        return name;
    }
}
    /*
    G — у фильма нет возрастных ограничений,
    PG — детям рекомендуется смотреть фильм с родителями,
    PG-13 — детям до 13 лет просмотр не желателен,
    R — лицам до 17 лет просматривать фильм можно только в присутствии взрослого,
    NC-17 — лицам до 18 лет просмотр запрещён.
    */


