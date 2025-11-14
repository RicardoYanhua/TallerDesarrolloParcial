package com.yanhua.ms.operacion.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "operacion")
public class OperacionModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_operacion")
    private Integer idOperacion;

    @Column(name = "id_cliente")
    private Integer idCliente;

    @Column(name = "tipo_operacion")
    private String tipoOperacion;

    @Column(name = "total")
    private double total;

    public OperacionModel(Integer idCliente, String tipoOperacion, double total) {
        this.idCliente = idCliente;
        this.tipoOperacion = tipoOperacion;
        this.total = total;
    }

}
