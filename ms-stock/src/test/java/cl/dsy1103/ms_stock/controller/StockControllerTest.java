package cl.dsy1103.ms_stock.controller;

import cl.dsy1103.ms_stock.dto.StockCreateDTO;
import cl.dsy1103.ms_stock.dto.StockResponseDTO;
import cl.dsy1103.ms_stock.exception.StockNoEncontradoException;
import cl.dsy1103.ms_stock.exception.StockInsuficienteException;
import cl.dsy1103.ms_stock.service.StockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(StockController.class)
public class StockControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StockService stockService;
    // ============================
    // GET /api/stock
    // ============================
    @Test
    void listarStocks_returns200WithList() throws Exception {
        // Given
        StockResponseDTO dto = new StockResponseDTO();
        dto.setId(1L);
        dto.setProductoId(5L);
        dto.setCantidadDisponible(50);
        dto.setStockMinimo(5);

        when(stockService.listarStocks()).thenReturn(List.of(dto));

        // When / Then
        mockMvc.perform(get("/api/stock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].productoId").value(5L))
                .andExpect(jsonPath("$[0].cantidadDisponible").value(50));

        verify(stockService, times(1)).listarStocks();
    }

    // ============================
    // GET /api/stock/{id}
    // ============================

    @Test
    void obtenerPorId_whenExists_returns200() throws Exception {
        // Given
        StockResponseDTO dto = new StockResponseDTO();
        dto.setId(1L);
        dto.setProductoId(5L);
        dto.setCantidadDisponible(50);

        when(stockService.obtenerPorId(1L)).thenReturn(dto);

        // When / Then
        mockMvc.perform(get("/api/stock/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.productoId").value(5L));

        verify(stockService, times(1)).obtenerPorId(1L);
    }

    @Test
    void obtenerPorId_whenNotFound_returns404() throws Exception {
        // Given
        when(stockService.obtenerPorId(99L))
                .thenThrow(new StockNoEncontradoException("Stock con ID 99 no encontrado"));

        // When / Then
        mockMvc.perform(get("/api/stock/99"))
                .andExpect(status().isNotFound());

        verify(stockService, times(1)).obtenerPorId(99L);
    }

    // ============================
    // POST /api/stock
    // ============================

    @Test
    void crearStock_givenValidBody_returns201() throws Exception {
        // Given
        StockCreateDTO request = new StockCreateDTO();
        request.setProductoId(10L);
        request.setCantidadDisponible(100);
        request.setStockMinimo(5);

        StockResponseDTO response = new StockResponseDTO();
        response.setId(1L);
        response.setProductoId(10L);
        response.setCantidadDisponible(100);

        when(stockService.crearStock(any(StockCreateDTO.class))).thenReturn(response);

        // When / Then
        mockMvc.perform(post("/api/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cantidadDisponible").value(100));

        verify(stockService, times(1)).crearStock(any(StockCreateDTO.class));
    }

    // ============================
    // PUT /api/stock/disminuir
    // ============================

    @Test
    void disminuirStock_whenInsuficiente_returns400() throws Exception {
        // Given
        String body = """
                {
                    "stockId": 1,
                    "cantidad": 999,
                    "observacion": "Test"
                }
                """;

        when(stockService.disminuirStock(1L, 999, "Test"))
                .thenThrow(new StockInsuficienteException("Stock insuficiente", 10, 999));

        // When / Then
        mockMvc.perform(put("/api/stock/disminuir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        verify(stockService, times(1)).disminuirStock(1L, 999, "Test");
    }
}
