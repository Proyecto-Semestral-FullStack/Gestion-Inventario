package cl.dsy1103.ms_stock.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para AUMENTAR stock (ENTRADA).
 *
 * Se envía en PUT /stocks/aumentar
 *
 * Ejemplo:
 * {
 *   "stockId": 1,
 *   "cantidad": 50,
 *   "observacion": "Compra proveedor ABC"
 * }
 */
@Schema(description = "Datos para aumentar stock (ENTRADA)")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AumentarStockDTO {

    /**
     * ID del stock a aumentar.
     */
    @Schema(description = "ID del stock a aumentar", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del stock es obligatorio")
    @Positive(message = "El ID del stock debe ser mayor a 0")
    private Long stockId;

    /**
     * Cantidad a aumentar.
     */

    @Schema(description = "Cantidad a aumentar", example = "50", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    /**
     * Observación del movimiento (opcional).
     */
    @Schema(description = "Observación del movimiento", example = "Compra proveedor ABC")
    @Size(max = 500, message = "La observación no puede exceder 500 caracteres")
    private String observacion;
}