package com.hyagohenrique.ferias.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.hyagohenrique.ferias.model.Equipe;

import lombok.Data;

@Data
public class EquipeDTO {
    private Long id;
    @NotNull(message = "Preencha o nome da equipe")
    @Size(min = 3, message = "O nome da equipe deve conter pelo menos 3 caracteres")
    private String nome;

    public Equipe converteParaEntidade() {
        Equipe e = new Equipe();
        e.setId(id);
        e.setNome(nome);
        return e;
    }
}