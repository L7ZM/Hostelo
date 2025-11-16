package com.udev.hotel.controller;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udev.hotel.config.constants.EtatChambre;
import com.udev.hotel.config.constants.TypeChambre;
import com.udev.hotel.domain.entity.Chambre;
import com.udev.hotel.service.ChambreService;
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ChambreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChambreService chambreService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetAllChambres() throws Exception {
        // Given
        List<Chambre> mockChambres = List.of(
                new Chambre(101, EtatChambre.DISPONIBLE, TypeChambre.SINGLE, 100.0, "Single Room", new ArrayList<>()),
                new Chambre(102, EtatChambre.EN_ENTRETIEN, TypeChambre.DELUXE, 200.0, "Deluxe Room", new ArrayList<>())
        );
        when(chambreService.getAllChambres()).thenReturn(mockChambres);

        // When & Then
        mockMvc.perform(get("/api/chambres")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].numeroChambre").value(101))
                .andExpect(jsonPath("$[0].etat").value("DISPONIBLE"))
                .andExpect(jsonPath("$[0].type").value("SINGLE"))
                .andExpect(jsonPath("$[1].type").value("DELUXE"));
    }

    @Test
    void testGetChambreById() throws Exception {
        // Given
        Chambre chambre = new Chambre(101, EtatChambre.DISPONIBLE, TypeChambre.DELUXE, 300.0, "Suite Room", new ArrayList<>());
        when(chambreService.getChambreById(1L)).thenReturn(Optional.of(chambre));

        // When & Then
        mockMvc.perform(get("/api/chambres/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroChambre").value(101))
                .andExpect(jsonPath("$.etat").value("DISPONIBLE"))
                .andExpect(jsonPath("$.type").value("DELUXE"));
    }

    @Test
    void testCreateChambre() throws Exception {
        // Given
        Chambre chambre = new Chambre(103, EtatChambre.DISPONIBLE, TypeChambre.DELUXE, 150.0, "New Room", new ArrayList<>());
        Chambre savedChambre = new Chambre(103, EtatChambre.DISPONIBLE, TypeChambre.DELUXE, 150.0, "New Room", new ArrayList<>());

        MockMultipartFile chambreJson = new MockMultipartFile(
                "chambre", "chambre.json", "application/json",
                objectMapper.writeValueAsBytes(chambre)
        );

        MockMultipartFile photo = new MockMultipartFile(
                "photo", "photo.jpg", "image/jpeg", "mock photo".getBytes()
        );

        when(chambreService.createChambre(ArgumentMatchers.<MultipartFile>anyList(), ArgumentMatchers.any(Chambre.class)))
        .thenReturn(savedChambre);


        // When & Then
        mockMvc.perform(multipart("/api/chambres")
                .file(chambreJson)
                .file(photo)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroChambre").value(103))
                .andExpect(jsonPath("$.etat").value("DISPONIBLE"))
                .andExpect(jsonPath("$.type").value("DELUXE"));
    }

    @Test
    void testDeleteChambre() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/chambres/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(chambreService, times(1)).deleteChambre(1L);
    }

    @Test
    void testGetAvailableChambres() throws Exception {
        // Given
        List<Chambre> availableChambres = List.of(
                new Chambre(104, EtatChambre.DISPONIBLE, TypeChambre.DELUXE, 250.0, "Available Room", new ArrayList<>())
        );
        when(chambreService.getAvailableChambres()).thenReturn(availableChambres);

        // When & Then
        mockMvc.perform(get("/api/chambres/disponibles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].numeroChambre").value(104))
                .andExpect(jsonPath("$[0].etat").value("DISPONIBLE"))
                .andExpect(jsonPath("$[0].type").value("DELUXE"));
    }
}
