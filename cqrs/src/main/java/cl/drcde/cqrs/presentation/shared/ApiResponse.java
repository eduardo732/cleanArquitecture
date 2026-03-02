package cl.drcde.cqrs.presentation.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiResponse<T> {
  private int status;
  private String message;
  private T data;
}
