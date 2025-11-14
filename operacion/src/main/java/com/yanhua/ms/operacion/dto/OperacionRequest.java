package com.yanhua.ms.operacion.dto;
 
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperacionRequest implements Serializable {
        private Integer idCliente;
        private String tipoOperacion;
        private double total;
 
    
}





