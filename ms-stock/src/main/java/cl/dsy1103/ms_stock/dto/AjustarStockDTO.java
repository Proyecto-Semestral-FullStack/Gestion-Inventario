package cl.dsy1103.ms_stock.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para AJUSTAR stock (corrección manual).
 *
 * Se envía en PUT /stocks/ajustar
 *
 * Ejemplo:
 * {
 *   "stockId": 1,
 *   "nuevoValor": 75,
 *   "observacion": "Corrección por inventario físico"
 * }
 */
@Schema(description = "Datos para ajuste manual de stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AjustarStockDTO {

    @Schema(description = "ID del stock a ajustar", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del stock es obligatorio")
    @Positive(message = "El ID del stock debe ser mayor a 0")
    private Long stockId;

    /**
     * Nuevo valor de cantidad disponible.
     * @PositiveOrZero permite 0 (puede haber stock vacío).
     */

    @Schema(description = "ID del stock a ajustar", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El nuevo valor es obligatorio")
    @PositiveOrZero(message = "El nuevo valor no puede ser negativo")
    private Integer nuevoValor;

    @Schema(description = "Motivo del ajuste", example = "Corrección por inventario físico")
    @Size(max = 500, message = "La observación no puede exceder 500 caracteres")
    private String observacion;
}