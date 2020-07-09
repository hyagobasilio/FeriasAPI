package com.hyagohenrique.ferias.iservice;

import java.util.List;
import java.util.Optional;

import com.hyagohenrique.ferias.model.Funcionario;

public interface IFuncionarioService {
    
    Funcionario salvar(Funcionario funcionario);

    List<Funcionario> listarFuncionarios();

    Optional<Funcionario> buscarPorId(Long id);
}