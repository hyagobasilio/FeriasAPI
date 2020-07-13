package com.hyagohenrique.ferias.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {
    
    private String username;

    @JsonIgnore
    private String password;

    private String firstname;

    private String lastname;
}