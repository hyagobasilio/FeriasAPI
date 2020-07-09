package com.hyagohenrique.ferias.dto;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hyagohenrique.ferias.model.Endereco;
import com.hyagohenrique.ferias.model.Funcionario;

import lombok.Data;

@Data
public class FuncionarioDTO  implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    private Long id;
    @NotNull(message = "Informe o nome do funcionário")
    private String nome;
    @NotNull(message = "Informe uma data de nascimento")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
    private Date dataNascimento;
    @NotNull(message = "Informe uma data de contratação")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
    private Date dataContratacao;

    @NotNull(message = "Informe a equipe deste funcionário")
    private Long equipeId;

    // Dados do endereço
    @NotNull(message = "Informe o nome da rua")
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;


    public Funcionario convertParaEntidade() {
        Funcionario f = new Funcionario();
        f.setNome(nome);
        f.setDataNascimento(dataNascimento);
        f.setDataContratacao(dataContratacao);
        f.setId(id);
        Endereco endereco = new Endereco();
        endereco.setRua(rua);
        endereco.setNumero(numero);
        endereco.setBairro(bairro);
        endereco.setComplemento(complemento);
        endereco.setCidade(cidade);
        endereco.setEstado(estado);
        f.setEndereco(endereco);

        return f;
    }
    
}