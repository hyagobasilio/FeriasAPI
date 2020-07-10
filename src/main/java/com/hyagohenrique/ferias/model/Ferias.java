package com.hyagohenrique.ferias.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hyagohenrique.ferias.dto.FeriasDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ferias")
public class Ferias implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_id", referencedColumnName = "id")
    private Funcionario funcionario;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date inicio;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fim;


    public FeriasDTO converteParaDTO() {
        FeriasDTO dto = new FeriasDTO();
        dto.setId(id);
        dto.setInicio(inicio);
        dto.setFim(fim);
        dto.setFuncionario(funcionario.getId());
        return dto;
    }
}