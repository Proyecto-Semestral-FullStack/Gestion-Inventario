package cl.dsy1103.ms_stock.service;


import cl.dsy1103.ms_stock.dto.MovimientoStockResponseDTO;
import cl.dsy1103.ms_stock.model.MovimientoStock;
import cl.dsy1103.ms_stock.model.Stock;
import cl.dsy1103.ms_stock.model.TipoMovimiento;
import cl.dsy1103.ms_stock.repository.MovimientoStockRepository;
import cl.dsy1103.ms_stock.repository.StockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class MovimientoStockServiceTest {

    @Mock
    private MovimientoStockRepository movimientoRepository;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private MovimientoStockService movimientoStockService;

    // ============================
    // listarMovimientos
    // ============================

    @Test
    void listarMovimientos_givenRepositoryHasMovimientos_returnsListOfDTOs() {
        // Given
        Stock stock = Stock.builder()
                .id(1L).productoId(5L)
                .cantidadDisponible(50).stockMinimo(5)
                .build();

        MovimientoStock m1 = MovimientoStock.builder()
                .id(1L)
                .stock(stock)
                .tipoMovimiento(TipoMovimiento.ENTRADA)
                .cantidad(20)
                .observacion("Compra proveedor")
                .build();

        MovimientoStock m2 = MovimientoStock.builder()
                .id(2L)
                .stock(stock)
                .tipoMovimiento(TipoMovimiento.SALIDA)
                .cantidad(10)
                .observacion("Venta pedido #1")
                .build();

        when(movimientoRepository.findAll()).thenReturn(List.of(m1, m2));

        // When
        List<MovimientoStockResponseDTO> resultado = movimientoStockService.listarMovimientos();

        // Then
        assertEquals(2, resultado.size());
        verify(movimientoRepository, times(1)).findAll();
    }

    @Test
    void listarMovimientos_givenEmptyRepository_returnsEmptyList() {
        // Given
        when(movimientoRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<MovimientoStockResponseDTO> resultado = movimientoStockService.listarMovimientos();

        // Then
        assertTrue(resultado.isEmpty());
        verify(movimientoRepository, times(1)).findAll();
    }

    @Test
    void listarMovimientos_verifyNeverInteractsWithStockRepository() {
        // Given
        when(movimientoRepository.findAll()).thenReturn(Collections.emptyList());

        // When


        movimientoStockService.listarMovimientos();

        // Then
        // listarMovimientos no debería tocar el stockRepository en ningún momento
        verify(stockRepository, never()).findAll();
        verify(stockRepository, never()).findById(any());
    }
}
