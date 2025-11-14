package com.yanhua.ms.Operacion.model;
 
import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "operacion")
public class OperacionModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_operacion", updatable = false, nullable = false)
    private String idOperacion;

    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "tipo_operacion")
    private String tipoOperacion;

    @Column(name = "total")
    private double total;

    public OperacionModel(String tipoOperacion, Integer idCliente, double total) {
        this.idOperacion = UUID.randomUUID().toString();
        this.tipoOperacion = tipoOperacion;
        this.idCliente = idCliente;
        this.total = total;
    }

    @PrePersist
    public void prePersist() {
        if (this.idOperacion == null) {
            this.idOperacion = UUID.randomUUID().toString();
        }
    }
}

