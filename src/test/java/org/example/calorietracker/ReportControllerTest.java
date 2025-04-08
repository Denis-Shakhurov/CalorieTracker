package org.example.calorietracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.calorietracker.controller.ReportController;
import org.example.calorietracker.dto.DailyReport;
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
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ReportController.class)
public class ReportControllerTest {
    private final String BASE_URL = "/users/{id}/reports";
    private final MediaType JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON;
    private DailyReport dailyReport;
    private final Long userId = 1L;
    private final LocalDate date = LocalDate.now();
    private final LocalDate startDate = LocalDate.now().minusDays(7);
    private final LocalDate endDate = LocalDate.now();
    private MealDTO mealDTO;
    private MealDTO mealDTO2;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MealService mealService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.
                standaloneSetup(new ReportController(mealService))
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        mealDTO = new MealDTO(1L, date, 1L, List.of(1L, 2L));
        mealDTO2 = new MealDTO(2L, date, 1L, List.of(1L, 2L));
        dailyReport = new DailyReport(
                date,
                List.of(mealDTO, mealDTO2),
                2000
        );
    }

    @Test
    @DisplayName("GET /users/{id}/reports/daily - Success")
    void getDailyReport_ShouldReturnReport() throws Exception {
        given(mealService.getDailyReport(userId, date)).willReturn(dailyReport);

        mockMvc.perform(get(BASE_URL + "/daily", userId)
                        .param("date", date.toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.date").value(date.toString()));
    }

    @Test
    @DisplayName("GET /users/{id}/reports/daily - User Not Found")
    void getDailyReport_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        given(mealService.getDailyReport(anyLong(), any(LocalDate.class)))
                .willThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get(BASE_URL + "/daily", 99L)
                        .param("date", date.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /users/{id}/reports/daily - Invalid Date Format")
    void getDailyReport_WhenInvalidDateFormat_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get(BASE_URL + "/daily", userId)
                        .param("date", "invalid-date"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /users/{id}/reports/check-limit - Within Limit")
    void checkDailyLimit_WhenWithinLimit_ShouldReturnTrue() throws Exception {
        given(mealService.isWithinDailyLimit(userId, date)).willReturn(true);

        mockMvc.perform(get(BASE_URL + "/check-limit", userId)
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @DisplayName("GET /users/{id}/reports/check-limit - Exceeded Limit")
    void checkDailyLimit_WhenExceededLimit_ShouldReturnFalse() throws Exception {
        given(mealService.isWithinDailyLimit(userId, date)).willReturn(false);

        mockMvc.perform(get(BASE_URL + "/check-limit", userId)
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    @DisplayName("GET /users/{id}/reports/check-limit - User Not Found")
    void checkDailyLimit_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        given(mealService.isWithinDailyLimit(anyLong(), any(LocalDate.class)))
                .willThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get(BASE_URL + "/check-limit", 99L)
                        .param("date", date.toString()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /users/{id}/reports/history - Success")
    void getNutritionHistory_ShouldReturnReports() throws Exception {
        List<DailyReport> reports = List.of(dailyReport);
        given(mealService.getNutritionHistory(userId, startDate, endDate)).willReturn(reports);

        mockMvc.perform(get(BASE_URL + "/history", userId)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$[0].date").value(date.toString()));
    }

    @Test
    @DisplayName("GET /users/{id}/reports/history - Empty History")
    void getNutritionHistory_WhenNoData_ShouldReturnEmptyList() throws Exception {
        given(mealService.getNutritionHistory(userId, startDate, endDate))
                .willReturn(Collections.emptyList());

        mockMvc.perform(get(BASE_URL + "/history", userId)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("GET /users/{id}/reports/history - User Not Found")
    void getNutritionHistory_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        given(mealService.getNutritionHistory(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .willThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get(BASE_URL + "/history", 99L)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isNotFound());
    }
}
