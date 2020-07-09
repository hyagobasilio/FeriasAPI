package com.hyagohenrique.ferias.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hyagohenrique.ferias.dto.FuncionarioDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "funcionarios")
public class Funcionario implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Temporal(TemporalType.DATE)
    @Column(name = "data_nascimento")
    private Date dataNascimento;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco endereco;
    @Temporal(TemporalType.DATE)
    @Column(name = "data_contratacao")
    private Date dataContratacao;
    @Column
    private String avatar;
    
    
    @JoinColumn(name = "equipe_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Equipe equipe;
    @Column
    private String matricula;

    public FuncionarioDTO converteParaDTO() {
        FuncionarioDTO dto = new FuncionarioDTO();
        dto.setNome(nome);
        dto.setDataNascimento(dataNascimento);
        dto.setDataContratacao(dataContratacao);
        dto.setEquipeId(equipe.getId());
        
        if (endereco != null) {
            dto.setRua(endereco.getRua());
            dto.setNumero(endereco.getNumero());
            dto.setBairro(endereco.getBairro());
            dto.setComplemento(endereco.getComplemento());
            dto.setCidade(endereco.getCidade());
            dto.setEstado(endereco.getEstado());
        }
        return dto;
    }
}