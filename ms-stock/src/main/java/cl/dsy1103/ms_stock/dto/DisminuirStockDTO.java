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
 * DTO para DISMINUIR stock (SALIDA).
 *
 * Se envía en PUT /stocks/disminuir
 *
 * Ejemplo:
 * {
 *   "stockId": 1,
 *   "cantidad": 10,
 *   "observacion": "Venta pedido #456"
 * }
 */
@Schema(description = "Datos para disminuir stock (SALIDA)")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisminuirStockDTO {
    @Schema(description = "ID del stock a disminuir", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El ID del stock es obligatorio")
    @Positive(message = "El ID del stock debe ser mayor a 0")
    private Long stockId;

    @Schema(description = "Cantidad a disminuir", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @Schema(description = "Observación del movimiento", example = "Venta pedido #456")
    @Size(max = 500, message = "La observación no puede exceder 500 caracteres")
    private String observacion;
}