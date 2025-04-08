package org.example.calorietracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.calorietracker.controller.UserController;
import org.example.calorietracker.dto.user.UserCreateDTO;
import org.example.calorietracker.dto.user.UserDTO;
import org.example.calorietracker.dto.user.UserUpdateDTO;
import org.example.calorietracker.exception.ResourceNotFoundException;
import org.example.calorietracker.handler.GlobalExceptionHandler;
import org.example.calorietracker.model.GenderType;
import org.example.calorietracker.model.GoalType;
import org.example.calorietracker.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private static final String BASE_URL = "/users";
    private static final MediaType JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON;
    private UserDTO userDTO;
    private UserCreateDTO createDTO;
    private UserUpdateDTO updateDTO;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new UserController(userService))
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        userDTO = new UserDTO(1L,
                "testuser",
                "test@example.com",
                22,
                72.3,
                171.0,
                1300.0,
                GenderType.MALE,
                GoalType.WEIGHT_GAIN,
                List.of(1L, 2L));
        createDTO = new UserCreateDTO(
                "testuser",
                "test@example.com",
                22,
                72.3,
                171.0,
                GenderType.MALE,
                GoalType.WEIGHT_GAIN);
        updateDTO = new UserUpdateDTO(
                "updateduser",
                "updated@example.com",
                22,
                72.3,
                171.0,
                GenderType.MALE,
                GoalType.WEIGHT_GAIN);
    }

    @Test
    @DisplayName("GET /users get all users and return status 200 OK")
    void getAllUsersTest() throws Exception {
        List<UserDTO> users = List.of(userDTO);
        given(userService.getAll()).willReturn(users);

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$[0].id").value(userDTO.getId()))
                .andExpect(jsonPath("$[0].name").value(userDTO.getName()))
                .andExpect(jsonPath("$[0].email").value(userDTO.getEmail()));
    }

    @Test
    @DisplayName("GET /users get not found users and return empty list users, status 200 Ok")
    void getAllUsersWhenNoUsersTest() throws Exception {
        given(userService.getAll()).willReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("GET /users/{id} get user and return status 200 OK")
    void getUserTest() throws Exception {
        given(userService.getById(1L)).willReturn(userDTO);

        mockMvc.perform(get(BASE_URL + "/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.name").value(userDTO.getName()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));
    }

    @Test
    @DisplayName("GET /users/{id} get not found user and return status 404 NOT_FOUND")
    void getUserWhenUserNotFoundTest() throws Exception {
        given(userService.getById(anyLong()))
                .willThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get(BASE_URL + "/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /users create user and return status 201 CREATED")
    void createUserTest() throws Exception {
        given(userService.create(any(UserCreateDTO.class))).willReturn(userDTO);

        mockMvc.perform(post(BASE_URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.name").value(userDTO.getName()))
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));
    }

    @Test
    @DisplayName("POST /users create user with invalid data and return status 400 BAD_REQUEST")
    void createUserWhenInvalidDataTest() throws Exception {
        UserCreateDTO invalidDTO = new UserCreateDTO(
                "", "invalid-email", 22, 23.0, 45.0, GenderType.MALE, GoalType.WEIGHT_GAIN);

        mockMvc.perform(post(BASE_URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /users/{id} update user and return status 200 OK")
    void updateUserTest() throws Exception {
        UserDTO updatedUser = new UserDTO(1L,
                "updateduser",
                "updated@example.com",
                22,
                23.0,
                45.0,
                1200.0,
                GenderType.MALE,
                GoalType.WEIGHT_GAIN,
                List.of(1L, 2L));
        given(userService.update(any(UserUpdateDTO.class), anyLong())).willReturn(updatedUser);

        mockMvc.perform(post(BASE_URL + "/{id}", 1L)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.name").value(updatedUser.getName()))
                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()));
    }

    @Test
    @DisplayName("POST /users/{id} update user not found and return status 400 NOT_FOUND")
    void updateUser_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        given(userService.update(any(UserUpdateDTO.class), anyLong()))
                .willThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(post(BASE_URL + "/{id}", 99L)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /users/{id} delete user and return status 204 NO_CONTENT")
    void deleteUser_ShouldReturnNoContent() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete(BASE_URL + "/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /users/{id} delete not found user and return status 400 NO_FOUND")
    void deleteUser_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("User not found"))
                .when(userService).delete(anyLong());

        mockMvc.perform(delete(BASE_URL + "/{id}", 99L))
                .andExpect(status().isNotFound());
    }
}
