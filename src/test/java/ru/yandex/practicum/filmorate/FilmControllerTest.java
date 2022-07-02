package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc

public class FilmControllerTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void tryCreateFilmResponseIsOk() throws Exception {
        Film film = new Film(1, "name", "description", LocalDate.of(1985,12,28),
                120, null, null, null);
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void tryCreateFilmWithNegativeIdResponseIsBadRequest() throws Exception {
        Film film = new Film(-1, "name", "description", LocalDate.of(1986,12,12),
                120, null, null, null);
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //название не может быть пустым
    @Test
    void tryCreateFilmWithEmptyNameIsBadRequest() throws Exception {
        Film film = new Film(1, "", "description", LocalDate.of(1986,12,12),
                120, null, null, null);
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    //максимальная длина описания — 200 символов
    @Test
    void tryCreateFilmLongDescriptionIsBadRequest() throws Exception {
        String description = RandomString.make(201);
        Film film = new Film(1, "name", description, LocalDate.of(1986,12,12),
                120, null, null, null);
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void tryCreateFilmGoodDescriptionIsOk() throws Exception {
        String description = RandomString.make(200);
        Film film = new Film(1, "name", description, LocalDate.of(1986,12,12),
                120, null, null, null);
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    //дата релиза — не раньше 28 декабря 1895 года
    @Test
    void tryCreateFilmWithBadReleaseDateIsBadRequest() throws Exception {
        Film film = new Film(1, "", "description", LocalDate.of(1985,12,27),
                120, null, null, null);
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    //продолжительность фильма должна быть положительной
    @Test
    void tryCreateFilmWithNegativeDurationIsBadRequest() throws Exception {
        Film film = new Film(1, "", "description", LocalDate.of(1985,12,28),
                -120, null, null, null);
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
