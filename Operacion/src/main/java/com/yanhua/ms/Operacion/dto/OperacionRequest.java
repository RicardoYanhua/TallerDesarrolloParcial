package com.yanhua.ms.Operacion.dto;
 
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperacionRequest implements Serializable {

    
        private Integer idCliente;
        private String tipoOperacion;
        private double total;
 
    
}





