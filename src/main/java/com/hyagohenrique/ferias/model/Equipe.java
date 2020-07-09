package com.hyagohenrique.ferias.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.hyagohenrique.ferias.dto.EquipeDTO;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Table(name = "equipes")
@Entity
public class Equipe implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Size(min = 3)
    @Column
    private String nome;
    
    public EquipeDTO converteParaDTO() {
        EquipeDTO dto = new EquipeDTO();
        dto.setId(id);
        dto.setNome(nome);
        return dto;
    }
}