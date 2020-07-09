package com.hyagohenrique.ferias.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "enderecos")
@Entity
public class Endereco implements Serializable {

    public Endereco(String rua) {
        this.rua = rua;
    }

    private static final long serialVersionUID = -2297222174336187922L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String rua;
    @Column
    private String numero;
    @Column
    private String complemento;
    @Column
    private String bairro;
    @Column
    private String cidade;
    @Column
    private String estado;
}