package cl.dsy1103.ms_stock.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import cl.dsy1103.ms_stock.model.Stock;


@Schema(description = "Respuesta con datos del stock de un producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockResponseDTO {

    @Schema(description = "ID único del stock", example = "1")
    private Long id;

    @Schema(description = "ID del producto en ms-Catalogo", example = "5")
    private Long productoId;

    @Schema(description = "Cantidad disponible en stock", example = "100")
    private Integer cantidadDisponible;

    @Schema(description = "Cantidad mínima recomendada antes de reponer", example = "10")
    private Integer stockMinimo;


    /**
     * Método HELPER para convertir una entidad Stock a DTO.
     *
     * Este patrón es común: transforma la entidad JPA normal en DTO seguro.
     *
     * Uso en el Service:
     *   Stock stock = stockRepository.findById(1L).orElseThrow(...);
     *   StockResponseDTO dto = StockResponseDTO.from(stock);
     *   return dto;
     */
    public static StockResponseDTO from(Stock stock) {
        return StockResponseDTO.builder()
                .id(stock.getId())
                .productoId(stock.getProductoId())
                .cantidadDisponible(stock.getCantidadDisponible())
                .stockMinimo(stock.getStockMinimo())
                .build();
    }
}