package cl.drcde.cqrs.presentation.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ApiResponse: Respuesta genérica para todos los endpoints
 *
 * VENTAJA: Formato consistente en toda la API
 * VENTAJA: Genérico para cualquier tipo de dato
 * VENTAJA: Fácil de serializar/deserializar
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiResponse<T> {
    private int status;
    private T data;
    private String message;
}
