package cl.dsy1103.ms_stock.service;

import cl.dsy1103.ms_stock.dto.StockCreateDTO;
import cl.dsy1103.ms_stock.exception.StockInsuficienteException;
import static org.mockito.Mockito.doNothing;

import cl.dsy1103.ms_stock.config.CatalogoClient;
import cl.dsy1103.ms_stock.dto.StockResponseDTO;
import cl.dsy1103.ms_stock.exception.StockNoEncontradoException;
import cl.dsy1103.ms_stock.model.Stock;
import cl.dsy1103.ms_stock.repository.MovimientoStockRepository;
import cl.dsy1103.ms_stock.repository.StockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class StockServiceTest {
    @Mock
    private StockRepository stockRepository;

    @Mock
    private MovimientoStockRepository movimientoRepository;

    @Mock
    private CatalogoClient catalogoClient;

    @InjectMocks
    private StockService stockService;

    // ============================
    // listarStocks
    // ============================

    @Test
    void listarStocks_givenRepositoryHasStocks_returnsListOfDTOs() {
        // Given
        Stock s1 = Stock.builder()
                .id(1L).productoId(10L)
                .cantidadDisponible(50).stockMinimo(5)
                .build();

        Stock s2 = Stock.builder()
                .id(2L).productoId(20L)
                .cantidadDisponible(100).stockMinimo(10)
                .build();

        when(stockRepository.findAll()).thenReturn(List.of(s1, s2));

        // When
        List<StockResponseDTO> resultado = stockService.listarStocks();

        // Then
        assertEquals(2, resultado.size());
        verify(stockRepository, times(1)).findAll();
    }

    // ============================
    // obtenerPorId
    // ============================

    @Test
    void obtenerPorId_whenExists_returnsDTO() {
        // Given
        Stock stock = Stock.builder()
                .id(1L).productoId(5L)
                .cantidadDisponible(50).stockMinimo(5)
                .build();

        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));

        // When
        StockResponseDTO dto = stockService.obtenerPorId(1L);

        // Then
        assertEquals(1L, dto.getId());
        assertEquals(5L, dto.getProductoId());
        verify(stockRepository, times(1)).findById(1L);
    }

    //Test lanzarExcepcion
    @Test
    void obtenerPorId_whenNotFound_throwsStockNoEncontradoException() {
        // Given
        when(stockRepository.findById(99L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(StockNoEncontradoException.class,
                () -> stockService.obtenerPorId(99L));
        verify(stockRepository, times(1)).findById(99L);
    }

    // ============================
    // crearStock
    // ============================

    @Test
    void crearStock_givenValidRequest_savesAndReturnsDTO() {
        // Given
        StockCreateDTO dto = new StockCreateDTO();
        dto.setProductoId(10L);
        dto.setCantidadDisponible(50);
        dto.setStockMinimo(5);
        //
        when(catalogoClient.obtenerProducto(10L))
                .thenReturn(new CatalogoClient.ProductoInfo());

        // Simulamos que no hay stock duplicado
        when(stockRepository.existsByProductoId(10L)).thenReturn(false);

        Stock guardado = Stock.builder()
                .id(1L).productoId(10L)
                .cantidadDisponible(50).stockMinimo(5)
                .build();

        when(stockRepository.save(any(Stock.class))).thenReturn(guardado);

        // When
        StockResponseDTO resultado = stockService.crearStock(dto);

        // Then
        assertEquals(1L, resultado.getId());
        assertEquals(10L, resultado.getProductoId());
        assertEquals(50, resultado.getCantidadDisponible());
        verify(catalogoClient, times(1)).obtenerProducto(10L);
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    // ============================
    // disminuirStock
    // ============================

    @Test
    void disminuirStock_whenInsuficiente_throwsStockInsuficienteException() {
        // Given
        Stock stock = Stock.builder()
                .id(1L).productoId(5L)
                .cantidadDisponible(10) // solo hay 10
                .stockMinimo(2)
                .build();

        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));

        // When / Then
        // Intentamos disminuir 50 pero solo hay 10 → debe lanzar excepción
        assertThrows(StockInsuficienteException.class,
                () -> stockService.disminuirStock(1L, 50, "Test"));

        // Verificamos que nunca se guardó nada
        verify(stockRepository, never()).save(any());
        verify(movimientoRepository, never()).save(any());
    }

    // ============================
    // ajustarStock
    // ============================

    @Test
    void ajustarStock_whenMismoValor_noRegistraMovimiento() {
        // Given
        Stock stock = Stock.builder()
                .id(1L).productoId(5L)
                .cantidadDisponible(75) // ya tiene 75
                .stockMinimo(5)
                .build();

        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));

        // When
        // Ajustamos al mismo valor → no debería guardar nada
        stockService.ajustarStock(1L, 75, "Sin cambio");

        // Then
        verify(stockRepository, never()).save(any());
        verify(movimientoRepository, never()).save(any());
    }




}
