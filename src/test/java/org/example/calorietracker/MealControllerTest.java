package org.example.calorietracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.calorietracker.controller.MealController;
import org.example.calorietracker.dto.meal.MealCreateDTO;
import org.example.calorietracker.dto.meal.MealDTO;
import org.example.calorietracker.exception.ResourceNotFoundException;
import org.example.calorietracker.handler.GlobalExceptionHandler;
import org.example.calorietracker.service.MealService;
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
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MealController.class)
@ExtendWith(MockitoExtension.class)
public class MealControllerTest {
    private static final String BASE_URL = "/meals";
    private static final MediaType JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON;
    private MealDTO mealDTO;
    private MealCreateDTO mealCreateDTO;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MealService mealService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new MealController(mealService))
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        mealDTO = new MealDTO(1L, LocalDate.now(), 1L, List.of(1L, 2L));
        mealCreateDTO = new MealCreateDTO(1L, List.of(1L, 2L));
    }

    @DisplayName("GET /meals/{id} get meals and return status 200 Ok")
    @Test
    public void getMealTest() throws Exception {
        given(mealService.getById(1L)).willReturn(mealDTO);

        mockMvc.perform(get(BASE_URL + "/{id}", 1L)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mealDTO.getId()));
    }

    @DisplayName("GET /meals/{id} get meal not found and return status 404 NOT_FOUND")
    @Test
    public void getMealNotFoundTest() throws Exception {
        given(mealService.getById(anyLong()))
                .willThrow(new ResourceNotFoundException("Meal not found"));

        mockMvc.perform(get(BASE_URL + "/{id}", 99L)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isNotFound());
    }

    @DisplayName("POST /meals create meal and return status 201 CREATED")
    @Test
    public void createMealTest() throws Exception {
        given(mealService.create(any(MealCreateDTO.class))).willReturn(mealDTO);

        mockMvc.perform(post(BASE_URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(mealCreateDTO)))
                .andExpect(status().isCreated());
    }

    @DisplayName("POST /meals create meal with invalid data and return 400 BAD_REQUEST")
    @Test
    public void createMealWithInvalidDataTest() throws Exception {
        MealCreateDTO invalidDTO = new MealCreateDTO(null, List.of(1L, 2L));

        mockMvc.perform(post(BASE_URL)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("GET /meals get all meals and return status 200 OK")
    @Test
    public void getMealsByUserId_ShouldReturnMealList() throws Exception {
        List<MealDTO> meals = List.of(mealDTO);
        given(mealService.getAll()).willReturn(meals);

        mockMvc.perform(get(BASE_URL)
                        .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(mealDTO.getId()))
                .andExpect(jsonPath("$[0].userId").value(mealDTO.getUserId()));
    }

    @DisplayName("DELETE /meals/{id} delete meal and return status 204 NO_CONTENT")
    @Test
    public void deleteMeal_ShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @DisplayName("DELETE /meals/{id} delete not found meal and return status 404 NOT_FOUND")
    @Test
    public void deleteMeal_WhenMealNotFound_ShouldReturnNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Meal not found"))
                .when(mealService).delete(anyLong());

        mockMvc.perform(delete(BASE_URL + "/{id}", 99L))
                .andExpect(status().isNotFound());
    }
}
