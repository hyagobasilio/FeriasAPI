package com.hyagohenrique.ferias.dto;


import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hyagohenrique.ferias.model.Endereco;
import com.hyagohenrique.ferias.model.Equipe;
import com.hyagohenrique.ferias.model.Funcionario;
import com.hyagohenrique.ferias.utils.QRCodeUtils;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FuncionarioDTO  {
    private MultipartFile arquivo;
    
    private Long id;
    @NotNull(message = "Informe o nome do funcionário")
    private String nome;
    private String matricula;
    @NotNull(message = "Informe uma data de nascimento")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
    private Date dataNascimento;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "Informe uma data de contratação")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
    private Date dataContratacao;

    private String avatar;
    private String avatarLocation;

    @NotNull(message = "Informe a equipe deste funcionário")
    private Long equipeId;

    private String qrcodeBase64;

    // Dados do endereço
    @NotNull(message = "Informe o nome da rua")
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    

    public void gerarQrCodeBase64() {
        this.qrcodeBase64 = QRCodeUtils.gerarQRCodeAPartirDeFuncionarioDTO(this, 300, 250);
    }


    public Funcionario convertParaEntidade() {
        final Funcionario f = new Funcionario();
        f.setNome(nome);
        f.setDataNascimento(dataNascimento);
        f.setDataContratacao(dataContratacao);
        f.setId(id);
        final Equipe eq = new Equipe();
        eq.setId(equipeId);
        f.setEquipe(eq);
        final Endereco endereco = new Endereco();
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