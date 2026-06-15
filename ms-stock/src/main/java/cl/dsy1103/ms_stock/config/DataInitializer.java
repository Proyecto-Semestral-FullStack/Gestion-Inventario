package cl.dsy1103.ms_stock.config;

import cl.dsy1103.ms_stock.model.Stock;
import cl.dsy1103.ms_stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    private final StockRepository stockRepository;


    @Override
    public void run(String... args) throws Exception {
        // Evitar inserción duplicada si ya existen datos
        if (stockRepository.count() > 0) {
            log.info("El inventario ya tiene datos. Se omite la inicialización.");
            return;
        }

        log.info(">>> Inicializando stock para los 7 productos del catálogo...");

        // Stock para cada producto del catálogo (IDs del 1 al 7) para probar el flujo de compra
        // new Stock(id, productoId, cantidadDisponible, stockMinimo)
        // id = null (se genera automáticamente), productoId = ID del producto en catálogo
        // cantidadDisponible = unidades disponibles, stockMinimo = umbral de alerta
                stockRepository.save(new Stock(null, 1L, 50, 5));   // Zelda
        stockRepository.save(new Stock(null, 2L, 30, 3));   // God of War
        stockRepository.save(new Stock(null, 3L, 15, 2));   // PS5
        stockRepository.save(new Stock(null, 4L, 10, 2));   // Xbox
        stockRepository.save(new Stock(null, 5L, 100, 10)); // Goku
        stockRepository.save(new Stock(null, 6L, 75, 5));   // Batman
        stockRepository.save(new Stock(null, 7L, 200, 20)); // Polera

        log.info(">>> Stock inicial creado correctamente para 7 productos.");
    }
}
