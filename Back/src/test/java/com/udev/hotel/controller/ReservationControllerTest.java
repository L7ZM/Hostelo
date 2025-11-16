package com.udev.hotel.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.jayway.jsonpath.JsonPath;
import com.udev.hotel.config.constants.ReservationStatus;
import com.udev.hotel.service.ReservationService;
import com.udev.hotel.service.dto.ReservationResponse;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@WithMockUser(authorities = {"ROLE_ADMIN" , "ROLE_USER"})
class ReservationControllerTest {


    @InjectMocks
    private ReservationController reservationController;

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
	private ReservationService reservationService;
    
    @Mock
    private SecurityContext securityContext;

    @Mock
    private SecurityContextHolder securityContextHolder;
  	
    @Test
    void testGetAllReservation() throws Exception {
    	//given 
    	List<ReservationResponse> mockReservation = List.of(
    			new ReservationResponse(1L, 1L, 120, "elhassani", "anas", 
    					LocalDate.of(2025, 12, 10), LocalDate.of(2025, 12, 12), 
    					ReservationStatus.EN_ATTENTE,true, Arrays.asList("Spa", "Breakfast")),
    			new ReservationResponse(2L, 2L, 130, "elkhalifi", "houssam", 
    					LocalDate.of(2025, 12, 10), LocalDate.of(2025, 12, 12), 
    					ReservationStatus.CONFIRMEE,true, Arrays.asList("Football", "Tennis"))
    			);
        when(reservationService.getAllReservations()).thenReturn(mockReservation);
    	//when & then
        
        mockMvc.perform(get("/api/reservations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].numeroChambre").value(120))
                .andExpect(jsonPath("$[1].numeroChambre").value(130))
                .andExpect(jsonPath("$[0].status").value("EN_ATTENTE"))
                .andExpect(jsonPath("$[1].status").value("CONFIRMEE"));
    }
    
    @Test
    @WithMockUser(username = "anas@gmail.com", roles = {"USER", "ADMIN"})
    void testGetReservationsByUserId() throws Exception {
        // Given
    	  ReservationResponse reservation = new ReservationResponse(
                  1L, 101L, 101, "elhassani", "Anas", 
                  LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 10), 
                  ReservationStatus.CONFIRMEE,true, Arrays.asList("Foot")
          );
          when(reservationService.getReservationsByUsername("anas@gmail.com")).thenReturn(Arrays.asList(reservation));

       
          // When & Then: 
          mockMvc.perform(get("/api/reservations/myBooking")
                  .contentType(MediaType.APPLICATION_JSON))
                  .andDo(print())  
                  .andExpect(status().isOk())  
                  .andExpect(jsonPath("$[0].numeroChambre").value(101))  ;


    }
    
    @Test
    void testCancelReservation() throws Exception {
        // Given
        Long reservationId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/reservations/{idReservation}", reservationId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testCancelReservationByAdmin() throws Exception {
        // Given
        Long reservationId = 1L;
        String username = "admin";

        // When & Then:
        mockMvc.perform(delete("/api/reservations/{idReservation}/{username}", reservationId, username)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());  
    }
    
}
