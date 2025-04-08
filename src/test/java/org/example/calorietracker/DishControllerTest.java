package org.example.calorietracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.calorietracker.controller.DishController;
import org.example.calorietracker.dto.dish.DishCreateDTO;
import org.example.calorietracker.dto.dish.DishDTO;
import org.example.calorietracker.dto.dish.DishUpdateDTO;
import org.example.calorietracker.exception.ResourceNotFoundException;
import org.example.calorietracker.handler.GlobalExceptionHandler;
import org.example.calorietracker.service.DishService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(DishController.class)
public class DishControllerTest {
    private static final String BASE_URL = "/dishes";
    private static final MediaType JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON;
    private DishDTO sampleDish;
    private DishCreateDTO sampleCreateDTO;
    private DishUpdateDTO sampleUpdateDTO;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DishService dishService;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new DishController(dishService))
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        sampleDish = new DishDTO(1L, "Pasta", 350.0, 12.5, 5.2, 60.0, 1L);
        sampleCreateDTO = new DishCreateDTO("Pasta", 350.0, 12.5, 5.2, 60.0, 1L);
        sampleUpdateDTO = new DishUpdateDTO("Updated Pasta", 400.0, 15.0, 6.0, 65.0, 1L);
    }

    @DisplayName("GET /dishes/{id} get dishes and return status 200 OK")
    @Test
    void getDishTest() throws Exception {
        given(dishService.getById(1L)).willReturn(sampleDish);

        mockMvc.perform(get(BASE_URL + "/{id}", 1L)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Pasta"));
    }

    @DisplayName("GET /dishes/{id} get dishes not found and return status 404 NOT_FOUND")
    @Test
    void getDishNotFoundTest() throws Exception {
        when(dishService.getById(99L))
                .thenThrow(new ResourceNotFoundException("Dish not found"));

        mockMvc.perform(get(BASE_URL + "/{id}", 99L)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isNotFound());
    }

    @DisplayName("GET /dishes get all dishes and return status 200 OK")
    @Test
    void getAllDishesTest() throws Exception {
        List<DishDTO> dishes = Collections.singletonList(sampleDish);
        given(dishService.getAll()).willReturn(dishes);

        mockMvc.perform(get(BASE_URL)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Pasta"));
    }

    @DisplayName("POST /dishes create dish and returns distDTO and status 201 CREATED")
    @Test
    void createDishTest() throws Exception {
        given(dishService.create(any(DishCreateDTO.class))).willReturn(sampleDish);

        mockMvc.perform(post(BASE_URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(sampleCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Pasta"));
    }

    @DisplayName("POST /dishes create dish with invalid data and return status 400 BAD_REQUEST")
    @Test
    void createDishWithInvalidDataTest() throws Exception {
        DishCreateDTO invalidDish = new DishCreateDTO("", -100.0, -1.0, -1.0, -1.0, 1L);

        mockMvc.perform(post(BASE_URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(invalidDish)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("POST /dishes/{id} update dish and return updated dish and status 200 Ok")
    @Test
    void updateDishTest() throws Exception {
        DishDTO updatedDish = new DishDTO(1L, "Updated Pasta", 400.0, 15.0, 6.0, 65.0, 1L);
        given(dishService.update(any(DishUpdateDTO.class), anyLong()))
                .willReturn(updatedDish);

        mockMvc.perform(post(BASE_URL + "/{id}", 1L)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(sampleUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Pasta"))
                .andExpect(jsonPath("$.calories").value(400.0));
    }

    @DisplayName("POST /dishes/{id} update not found dish and return status 404 NOT_FOUND")
    @Test
    void updateDishNotFoundTest() throws Exception {
        when(dishService.update(any(DishUpdateDTO.class),anyLong()))
                .thenThrow(new ResourceNotFoundException("Dish not found"));

        mockMvc.perform(post(BASE_URL + "/{id}", 99L)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(sampleUpdateDTO)))
                .andExpect(status().isNotFound());
    }

    @DisplayName("DELETE /dishes/{id} delete dish and return status 204 NO_CONTENT")
    @Test
    void deleteDishTest() throws Exception {
        doNothing().when(dishService).delete(anyLong());

        mockMvc.perform(delete(BASE_URL + "/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
