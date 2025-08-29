package gh.z0736190100.apparelstore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gh.z0736190100.apparelstore.models.ApparelOrderDto;
import gh.z0736190100.apparelstore.models.ApparelOrderLineDto;
import gh.z0736190100.apparelstore.models.CustomerDto;
import gh.z0736190100.apparelstore.services.ApparelOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApparelOrderController.class)
class ApparelOrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ApparelOrderService apparelOrderService;

    ApparelOrderDto testApparelOrder;
    ApparelOrderLineDto testApparelOrderLine;
    CustomerDto testCustomerDto;

    @BeforeEach
    void setUp() {
        // Create test customer DTO
        testCustomerDto = CustomerDto.builder()
                .id(1)
                .name("Test Customer")
                .email("test@example.com")
                .phoneNumber("555-123-4567")
                .addressLine1("123 Main St")
                .city("Springfield")
                .state("IL")
                .postalCode("62701")
                .build();

        // Create test apparel order line
        testApparelOrderLine = ApparelOrderLineDto.builder()
                .id(1)
                .apparelId(1)
                .apparelName("Test Apparel")
                .apparelStyle("Loose")
                .upc("123456")
                .orderQuantity(2)
                .quantityAllocated(2)
                .status("ALLOCATED")
                .build();

        // Create test apparel order
        Set<ApparelOrderLineDto> lines = new HashSet<>();
        lines.add(testApparelOrderLine);
        testApparelOrder = ApparelOrderDto.builder()
                .id(1)
                .customer(testCustomerDto)
                .paymentAmount(new BigDecimal("25.98"))
                .status("NEW")
                .apparelOrderLines(lines)
                .build();
    }

    @Test
    void testGetAllApparelOrders() throws Exception {
        // Given
        given(apparelOrderService.getAllApparelOrders()).willReturn(Arrays.asList(testApparelOrder));

        // When/Then
        mockMvc.perform(get("/api/v1/apparel-orders")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].customer.name", is("Test Customer")));
    }

    @Test
    void testGetApparelOrderById() throws Exception {
        // Given
        given(apparelOrderService.getApparelOrderById(1)).willReturn(Optional.of(testApparelOrder));

        // When/Then
        mockMvc.perform(get("/api/v1/apparel-orders/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customer.name", is("Test Customer")))
                .andExpect(jsonPath("$.apparelOrderLines", hasSize(1)));
    }

    @Test
    void testGetApparelOrderByIdNotFound() throws Exception {
        // Given
        given(apparelOrderService.getApparelOrderById(1)).willReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/v1/apparel-orders/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateApparelOrder() throws Exception {
        // Given
        CustomerDto newCustomerDto = CustomerDto.builder()
                .id(2)
                .name("New Customer")
                .email("new@example.com")
                .phoneNumber("555-987-6543")
                .addressLine1("456 Oak Ave")
                .city("Shelbyville")
                .state("IL")
                .postalCode("62565")
                .build();

        ApparelOrderDto apparelOrderToCreate = ApparelOrderDto.builder()
                .customer(newCustomerDto)
                .paymentAmount(new BigDecimal("39.97"))
                .status("NEW")
                .apparelOrderLines(new HashSet<>())
                .build();

        ApparelOrderLineDto lineDto = ApparelOrderLineDto.builder()
                .apparelId(1)
                .apparelName("Test Apparel")
                .apparelStyle("Loose")
                .upc("123456")
                .orderQuantity(3)
                .quantityAllocated(0)
                .status("NEW")
                .build();

        apparelOrderToCreate.getApparelOrderLines().add(lineDto);

        ApparelOrderDto savedApparelOrder = ApparelOrderDto.builder()
                .id(2)
                .customer(newCustomerDto)
                .paymentAmount(new BigDecimal("39.97"))
                .status("NEW")
                .apparelOrderLines(apparelOrderToCreate.getApparelOrderLines())
                .build();

        given(apparelOrderService.saveApparelOrder(any(ApparelOrderDto.class))).willReturn(savedApparelOrder);

        // When/Then
        mockMvc.perform(post("/api/v1/apparel-orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(apparelOrderToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.customer.name", is("New Customer")));
    }

    @Test
    void testUpdateApparelOrder() throws Exception {
        // Given
        CustomerDto updatedCustomerDto = CustomerDto.builder()
                .id(1)
                .name("Updated Customer")
                .email("updated@example.com")
                .phoneNumber("555-111-2222")
                .addressLine1("789 Pine St")
                .city("Capital City")
                .state("IL")
                .postalCode("62701")
                .build();

        ApparelOrderDto apparelOrderToUpdate = ApparelOrderDto.builder()
                .customer(updatedCustomerDto)
                .paymentAmount(new BigDecimal("39.97"))
                .status("PROCESSING")
                .apparelOrderLines(new HashSet<>())
                .build();

        ApparelOrderLineDto lineDto = ApparelOrderLineDto.builder()
                .apparelId(1)
                .apparelName("Test Apparel")
                .apparelStyle("Loose")
                .upc("123456")
                .orderQuantity(3)
                .quantityAllocated(3)
                .status("ALLOCATED")
                .build();

        apparelOrderToUpdate.getApparelOrderLines().add(lineDto);

        ApparelOrderDto updatedApparelOrder = ApparelOrderDto.builder()
                .id(1)
                .customer(updatedCustomerDto)
                .paymentAmount(new BigDecimal("39.97"))
                .status("PROCESSING")
                .apparelOrderLines(apparelOrderToUpdate.getApparelOrderLines())
                .build();

        given(apparelOrderService.getApparelOrderById(1)).willReturn(Optional.of(testApparelOrder));
        given(apparelOrderService.saveApparelOrder(any(ApparelOrderDto.class))).willReturn(updatedApparelOrder);

        // When/Then
        mockMvc.perform(put("/api/v1/apparel-orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(apparelOrderToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customer.name", is("Updated Customer")))
                .andExpect(jsonPath("$.status", is("PROCESSING")));
    }

    @Test
    void testUpdateApparelOrderNotFound() throws Exception {
        // Given
        CustomerDto updatedCustomerDto = CustomerDto.builder()
                .id(1)
                .name("Updated Customer")
                .email("updated@example.com")
                .phoneNumber("555-111-2222")
                .addressLine1("789 Pine St")
                .city("Capital City")
                .state("IL")
                .postalCode("62701")
                .build();

        ApparelOrderDto apparelOrderToUpdate = ApparelOrderDto.builder()
                .customer(updatedCustomerDto)
                .paymentAmount(new BigDecimal("39.97"))
                .status("PROCESSING")
                .apparelOrderLines(new HashSet<>())
                .build();

        // Add a apparel order line to pass validation
        ApparelOrderLineDto lineDto = ApparelOrderLineDto.builder()
                .apparelId(1)
                .apparelName("Test Apparel")
                .apparelStyle("Loose")
                .upc("123456")
                .orderQuantity(3)
                .quantityAllocated(0)
                .status("NEW")
                .build();
        apparelOrderToUpdate.getApparelOrderLines().add(lineDto);

        given(apparelOrderService.getApparelOrderById(1)).willReturn(Optional.empty());

        // When/Then
        mockMvc.perform(put("/api/v1/apparel-orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(apparelOrderToUpdate)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteApparelOrder() throws Exception {
        // Given
        given(apparelOrderService.getApparelOrderById(1)).willReturn(Optional.of(testApparelOrder));
        doNothing().when(apparelOrderService).deleteApparelOrderById(1);

        // When/Then
        mockMvc.perform(delete("/api/v1/apparel-orders/1"))
                .andExpect(status().isNoContent());

        verify(apparelOrderService).deleteApparelOrderById(1);
    }

    @Test
    void testDeleteApparelOrderNotFound() throws Exception {
        // Given
        given(apparelOrderService.getApparelOrderById(1)).willReturn(Optional.empty());

        // When/Then
        mockMvc.perform(delete("/api/v1/apparel-orders/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testValidationErrors() throws Exception {
        // Given
        CustomerDto invalidCustomerDto = CustomerDto.builder()
                .id(3)
                .name("Invalid Customer")
                .email("invalid@example.com")
                .phoneNumber("555-333-4444")
                .addressLine1("999 Invalid St")
                .city("Invalid City")
                .state("XX")
                .postalCode("99999")
                .build();

        ApparelOrderDto invalidApparelOrder = ApparelOrderDto.builder()
                .customer(invalidCustomerDto)
                .paymentAmount(new BigDecimal("-10.00")) // Invalid: negative amount
                .status("NEW")
                .apparelOrderLines(new HashSet<>()) // Invalid: empty order lines
                .build();

        // When/Then
        mockMvc.perform(post("/api/v1/apparel-orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidApparelOrder)))
                .andExpect(status().isBadRequest());
    }
}
