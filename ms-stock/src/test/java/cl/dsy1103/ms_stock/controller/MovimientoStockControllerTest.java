package cl.dsy1103.ms_stock.controller;

import cl.dsy1103.ms_stock.dto.MovimientoStockResponseDTO;
import cl.dsy1103.ms_stock.model.TipoMovimiento;
import cl.dsy1103.ms_stock.service.MovimientoStockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

    @WebMvcTest(MovimientoStockController.class)
    class MovimientoStockControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private MovimientoStockService movimientoService;

        // ============================
        // GET /movimiento
        // ============================

        @Test
        void listarMovimientos_returns200WithList() throws Exception {
            // Given
            MovimientoStockResponseDTO dto = new MovimientoStockResponseDTO();
            dto.setId(1L);
            dto.setStockId(1L);
            dto.setTipoMovimiento(TipoMovimiento.ENTRADA);
            dto.setCantidad(50);
            dto.setObservacion("Compra proveedor");

            when(movimientoService.listarMovimientos()).thenReturn(List.of(dto));

            // When / Then
            mockMvc.perform(get("/movimiento"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(1L))
                    .andExpect(jsonPath("$[0].stockId").value(1L))
                    .andExpect(jsonPath("$[0].tipoMovimiento").value("ENTRADA"))
                    .andExpect(jsonPath("$[0].cantidad").value(50));

            verify(movimientoService, times(1)).listarMovimientos();
        }

        @Test
        void listarMovimientos_whenEmpty_returns200WithEmptyList() throws Exception {
            // Given
            when(movimientoService.listarMovimientos()).thenReturn(Collections.emptyList());

            // When / Then
            mockMvc.perform(get("/movimiento"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isEmpty());

            verify(movimientoService, times(1)).listarMovimientos();
        }

        // ============================
        // GET /movimiento/stock/{stockId}
        // ============================

        @Test
        void listarPorStock_returns200WithList() throws Exception {
            // Given
            MovimientoStockResponseDTO dto = new MovimientoStockResponseDTO();
            dto.setId(2L);
            dto.setStockId(5L);
            dto.setTipoMovimiento(TipoMovimiento.SALIDA);
            dto.setCantidad(10);
            dto.setObservacion("Venta pedido #1");

            when(movimientoService.listarMovimientos()).thenReturn(List.of(dto));

            // When / Then
            mockMvc.perform(get("/movimiento/stock/5"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].tipoMovimiento").value("SALIDA"))
                    .andExpect(jsonPath("$[0].cantidad").value(10));

            verify(movimientoService, times(1)).listarMovimientos();
        }
    }
