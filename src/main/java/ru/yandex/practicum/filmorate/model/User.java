package ru.yandex.practicum.filmorate.model;

import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private static int counter=0;
    @Min(1)
    private int id = getNewId();
    @Email
    private String email;
    @NotBlank
    private String login;
    @Size(min = 0, max = 100)
    private String name;
    private LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();

    private int getNewId(){
        counter=counter+1;
        return counter;
    }

    public void addInFriends(int id) {
        friends.add(id);
    }

    public void removeInFriends(int id) {
        friends.remove(id);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return login.equals(user.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}