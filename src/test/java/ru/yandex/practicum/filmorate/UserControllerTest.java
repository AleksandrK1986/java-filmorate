package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc

public class UserControllerTest {


    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;

    @Test
    void tryCreateUserResponseIsOk() throws Exception {
        User user = new User(1,"a@email.ru", "login", "name", LocalDate.of(1995, 12,12));
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void tryCreateUserWithNegativeIdResponseIsBadRequest() throws Exception {
        User user = new User(-1,"a@email.ru", "login", "name", LocalDate.of(1995, 12,12));
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    //электронная почта не может быть пустой и должна содержать символ @
    @Test
    void tryCreateUserWithBedMailResponseIsBadRequest() throws Exception {
        User user = new User(1,"email.ru", "login", "name", LocalDate.of(1995, 12,12));
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    //логин не может быть пустым и содержать пробелы
    @Test
    void tryCreateUserWithEmptyLoginResponseIsBadRequest() throws Exception {
        User user = new User(1,"1@email.ru", "", "name", LocalDate.of(1995, 12,12));
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    //дата рождения не может быть в будущем
    @Test
    void tryCreateUserWithFutureBirthdayResponseIsBadRequest() throws Exception {
        User user = new User(1,"1@email.ru", "login", "name", LocalDate.of(9995, 12,12));
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}