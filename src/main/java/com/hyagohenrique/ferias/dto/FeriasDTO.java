package com.hyagohenrique.ferias.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hyagohenrique.ferias.model.Ferias;
import com.hyagohenrique.ferias.model.Funcionario;

import lombok.Data;

@Data
public class FeriasDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -64050818373433847L;
    private Long id;
    @NotNull(message = "Informe o id do funcionário")
    private Long funcionario;
    @NotNull(message = "Informe a data de início")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
    private Date inicio;
    @NotNull(message = "Informe uma data fim")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
    private Date fim;


    public Ferias converterParaEntidade() {
        Funcionario fun = new Funcionario();
        fun.setId(funcionario);
        
        Ferias f = new Ferias();
        f.setId(id);
        f.setInicio(inicio);
        f.setFim(fim);
        f.setFuncionario(fun);
        return f;
    }
}