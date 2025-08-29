package gh.z0736190100.apparelstore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import gh.z0736190100.apparelstore.models.ApparelDto;
import gh.z0736190100.apparelstore.models.ApparelPatchDto;
import gh.z0736190100.apparelstore.services.ApparelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApparelController.class)
class ApparelControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ApparelService apparelService;

    ApparelDto testApparel;

    @BeforeEach
    void setUp() {
        testApparel = ApparelDto.builder()
                .id(1)
                .apparelName("Test Apparel")
                .apparelStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
    }

    @Test
    void testGetAllApparels() throws Exception {
        // Given
        List<ApparelDto> apparels = Arrays.asList(testApparel);
        Page<ApparelDto> apparelPage = new PageImpl<>(apparels, PageRequest.of(0, 20), 1);

        given(apparelService.getAllApparels(eq(null), eq(null), any(Pageable.class))).willReturn(apparelPage);

        // When/Then
        mockMvc.perform(get("/api/v1/apparels")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].apparelName", is("Test Apparel")));
    }

    @Test
    void testGetAllApparelsWithPagination() throws Exception {
        // Given
        List<ApparelDto> apparels = Arrays.asList(testApparel);
        Page<ApparelDto> apparelPage = new PageImpl<>(apparels, PageRequest.of(0, 20), 1);

        given(apparelService.getAllApparels(eq(null), eq(null), any(Pageable.class))).willReturn(apparelPage);

        // When/Then
        mockMvc.perform(get("/api/v1/apparels")
                .param("page", "0")
                .param("size", "20")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].apparelName", is("Test Apparel")))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.size", is(20)))
                .andExpect(jsonPath("$.number", is(0)));
    }

    @Test
    void testGetAllApparelsWithApparelNameFilter() throws Exception {
        // Given
        List<ApparelDto> apparels = Arrays.asList(testApparel);
        Page<ApparelDto> apparelPage = new PageImpl<>(apparels, PageRequest.of(0, 20), 1);

        given(apparelService.getAllApparels(eq("Test"), eq(null), any(Pageable.class))).willReturn(apparelPage);

        // When/Then
        mockMvc.perform(get("/api/v1/apparels")
                .param("apparelName", "Test")
                .param("page", "0")
                .param("size", "20")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].apparelName", is("Test Apparel")))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.size", is(20)))
                .andExpect(jsonPath("$.number", is(0)));
    }

    @Test
    void testGetAllApparelsWithApparelStyleFilter() throws Exception {
        // Given
        List<ApparelDto> apparels = Arrays.asList(testApparel);
        Page<ApparelDto> apparelPage = new PageImpl<>(apparels, PageRequest.of(0, 20), 1);

        given(apparelService.getAllApparels(eq(null), eq("IPA"), any(Pageable.class))).willReturn(apparelPage);

        // When/Then
        mockMvc.perform(get("/api/v1/apparels")
                .param("apparelStyle", "IPA")
                .param("page", "0")
                .param("size", "20")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].apparelName", is("Test Apparel")))
                .andExpect(jsonPath("$.content[0].apparelStyle", is("IPA")))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.size", is(20)))
                .andExpect(jsonPath("$.number", is(0)));
    }

    @Test
    void testGetAllApparelsWithApparelNameAndApparelStyleFilter() throws Exception {
        // Given
        List<ApparelDto> apparels = Arrays.asList(testApparel);
        Page<ApparelDto> apparelPage = new PageImpl<>(apparels, PageRequest.of(0, 20), 1);

        given(apparelService.getAllApparels(eq("Test"), eq("IPA"), any(Pageable.class))).willReturn(apparelPage);

        // When/Then
        mockMvc.perform(get("/api/v1/apparels")
                .param("apparelName", "Test")
                .param("apparelStyle", "IPA")
                .param("page", "0")
                .param("size", "20")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].apparelName", is("Test Apparel")))
                .andExpect(jsonPath("$.content[0].apparelStyle", is("IPA")))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.size", is(20)))
                .andExpect(jsonPath("$.number", is(0)));
    }

    @Test
    void testGetApparelById() throws Exception {
        // Given
        given(apparelService.getApparelById(1)).willReturn(Optional.of(testApparel));

        // When/Then
        mockMvc.perform(get("/api/v1/apparels/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.apparelName", is("Test Apparel")));
    }

    @Test
    void testGetApparelByIdNotFound() throws Exception {
        // Given
        given(apparelService.getApparelById(1)).willReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/v1/apparels/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateApparel() throws Exception {
        // Given
        ApparelDto apparelToCreate = ApparelDto.builder()
                .apparelName("New Apparel")
                .apparelStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        ApparelDto savedApparel = ApparelDto.builder()
                .id(2)
                .apparelName("New Apparel")
                .apparelStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        given(apparelService.saveApparel(any(ApparelDto.class))).willReturn(savedApparel);

        // When/Then
        mockMvc.perform(post("/api/v1/apparels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(apparelToCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.apparelName", is("New Apparel")));
    }

    @Test
    void testUpdateApparel() throws Exception {
        // Given
        ApparelDto apparelToUpdate = ApparelDto.builder()
                .apparelName("Updated Apparel")
                .apparelStyle("Lager")
                .upc("789012")
                .price(new BigDecimal("16.99"))
                .quantityOnHand(150)
                .build();

        ApparelDto updatedApparel = ApparelDto.builder()
                .id(1)
                .apparelName("Updated Apparel")
                .apparelStyle("Lager")
                .upc("789012")
                .price(new BigDecimal("16.99"))
                .quantityOnHand(150)
                .build();

        given(apparelService.getApparelById(1)).willReturn(Optional.of(testApparel));
        given(apparelService.saveApparel(any(ApparelDto.class))).willReturn(updatedApparel);

        // When/Then
        mockMvc.perform(put("/api/v1/apparels/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(apparelToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.apparelName", is("Updated Apparel")))
                .andExpect(jsonPath("$.apparelStyle", is("Lager")));
    }

    @Test
    void testUpdateApparelNotFound() throws Exception {
        // Given
        ApparelDto apparelToUpdate = ApparelDto.builder()
                .apparelName("Updated Apparel")
                .apparelStyle("Lager")
                .upc("789012")
                .price(new BigDecimal("16.99"))
                .quantityOnHand(150)
                .build();

        given(apparelService.getApparelById(1)).willReturn(Optional.empty());

        // When/Then
        mockMvc.perform(put("/api/v1/apparels/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(apparelToUpdate)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteApparel() throws Exception {
        // Given
        given(apparelService.getApparelById(1)).willReturn(Optional.of(testApparel));
        doNothing().when(apparelService).deleteApparelById(1);

        // When/Then
        mockMvc.perform(delete("/api/v1/apparels/1"))
                .andExpect(status().isNoContent());

        verify(apparelService).deleteApparelById(1);
    }

    @Test
    void testDeleteApparelNotFound() throws Exception {
        // Given
        given(apparelService.getApparelById(1)).willReturn(Optional.empty());

        // When/Then
        mockMvc.perform(delete("/api/v1/apparels/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testValidationErrors() throws Exception {
        // Given
        ApparelDto invalidApparel = ApparelDto.builder()
                // Missing required fields
                .apparelName("")
                .apparelStyle("")
                .upc("")
                .price(new BigDecimal("-1.0"))
                .quantityOnHand(-1)
                .build();

        // When/Then
        mockMvc.perform(post("/api/v1/apparels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidApparel)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testPatchApparel() throws Exception {
        // Given
        ApparelPatchDto apparelPatchDto = ApparelPatchDto.builder()
                .apparelName("Patched Apparel")
                .price(new BigDecimal("15.99"))
                .build();

        ApparelDto patchedApparel = ApparelDto.builder()
                .id(1)
                .apparelName("Patched Apparel")
                .apparelStyle("IPA") // Original value
                .upc("123456") // Original value
                .price(new BigDecimal("15.99")) // Updated value
                .quantityOnHand(100) // Original value
                .build();

        given(apparelService.patchApparel(eq(1), any(ApparelPatchDto.class))).willReturn(Optional.of(patchedApparel));

        // When/Then
        mockMvc.perform(patch("/api/v1/apparels/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(apparelPatchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.apparelName", is("Patched Apparel")))
                .andExpect(jsonPath("$.apparelStyle", is("IPA")))
                .andExpect(jsonPath("$.price", is(15.99)));
    }

    @Test
    void testPatchApparelNotFound() throws Exception {
        // Given
        ApparelPatchDto apparelPatchDto = ApparelPatchDto.builder()
                .apparelName("Patched Apparel")
                .build();

        given(apparelService.patchApparel(eq(1), any(ApparelPatchDto.class))).willReturn(Optional.empty());

        // When/Then
        mockMvc.perform(patch("/api/v1/apparels/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(apparelPatchDto)))
                .andExpect(status().isNotFound());
    }
}
