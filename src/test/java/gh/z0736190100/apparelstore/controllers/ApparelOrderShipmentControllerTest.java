package gh.z0736190100.apparelstore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gh.z0736190100.apparelstore.models.ApparelOrderShipmentDto;
import gh.z0736190100.apparelstore.services.ApparelOrderService;
import gh.z0736190100.apparelstore.services.ApparelOrderShipmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests for ApparelOrderShipmentController
 */
@WebMvcTest(ApparelOrderShipmentController.class)
class ApparelOrderShipmentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ApparelOrderShipmentService apparelOrderShipmentService;

    @MockBean
    ApparelOrderService apparelOrderService;

    ApparelOrderShipmentDto testShipment;
    LocalDateTime testShipmentDate;

    @BeforeEach
    void setUp() {
        // Create test shipment date
        testShipmentDate = LocalDateTime.now();

        // Create test shipment
        testShipment = ApparelOrderShipmentDto.builder()
                .id(1)
                .shipmentDate(testShipmentDate)
                .carrier("FedEx")
                .trackingNumber("123456789")
                .build();
    }

    @Test
    void testGetAllShipments() throws Exception {
        // Given
        List<ApparelOrderShipmentDto> shipments = Arrays.asList(testShipment);
        given(apparelOrderShipmentService.getAllShipments(1)).willReturn(shipments);

        // When/Then
        mockMvc.perform(get("/api/v1/apparel-orders/1/shipments")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].carrier", is("FedEx")))
                .andExpect(jsonPath("$[0].trackingNumber", is("123456789")));
    }

    @Test
    void testGetShipmentById() throws Exception {
        // Given
        given(apparelOrderShipmentService.getShipmentById(1, 1)).willReturn(testShipment);

        // When/Then
        mockMvc.perform(get("/api/v1/apparel-orders/1/shipments/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.carrier", is("FedEx")))
                .andExpect(jsonPath("$.trackingNumber", is("123456789")));
    }

    @Test
    void testCreateShipment() throws Exception {
        // Given
        ApparelOrderShipmentDto shipmentToCreate = ApparelOrderShipmentDto.builder()
                .shipmentDate(testShipmentDate)
                .carrier("UPS")
                .trackingNumber("987654321")
                .build();

        ApparelOrderShipmentDto createdShipment = ApparelOrderShipmentDto.builder()
                .id(2)
                .shipmentDate(testShipmentDate)
                .carrier("UPS")
                .trackingNumber("987654321")
                .build();

        given(apparelOrderShipmentService.createShipment(anyInt(), any(ApparelOrderShipmentDto.class))).willReturn(createdShipment);

        // When/Then
        mockMvc.perform(post("/api/v1/apparel-orders/1/shipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shipmentToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.carrier", is("UPS")))
                .andExpect(jsonPath("$.trackingNumber", is("987654321")));
    }

    @Test
    void testUpdateShipment() throws Exception {
        // Given
        ApparelOrderShipmentDto shipmentToUpdate = ApparelOrderShipmentDto.builder()
                .shipmentDate(testShipmentDate)
                .carrier("DHL")
                .trackingNumber("UPDATED123")
                .build();

        ApparelOrderShipmentDto updatedShipment = ApparelOrderShipmentDto.builder()
                .id(1)
                .shipmentDate(testShipmentDate)
                .carrier("DHL")
                .trackingNumber("UPDATED123")
                .build();

        given(apparelOrderShipmentService.updateShipment(anyInt(), anyInt(), any(ApparelOrderShipmentDto.class))).willReturn(updatedShipment);

        // When/Then
        mockMvc.perform(put("/api/v1/apparel-orders/1/shipments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(shipmentToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.carrier", is("DHL")))
                .andExpect(jsonPath("$.trackingNumber", is("UPDATED123")));
    }

    @Test
    void testDeleteShipment() throws Exception {
        // Given
        doNothing().when(apparelOrderShipmentService).deleteShipment(1, 1);

        // When/Then
        mockMvc.perform(delete("/api/v1/apparel-orders/1/shipments/1"))
                .andExpect(status().isNoContent());

        verify(apparelOrderShipmentService).deleteShipment(1, 1);
    }

    @Test
    void testValidationErrors() throws Exception {
        // Given
        ApparelOrderShipmentDto invalidShipment = ApparelOrderShipmentDto.builder()
                .carrier("FedEx")
                .trackingNumber("123456789")
                // Missing required shipmentDate
                .build();

        // When/Then
        mockMvc.perform(post("/api/v1/apparel-orders/1/shipments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidShipment)))
                .andExpect(status().isBadRequest());
    }
}