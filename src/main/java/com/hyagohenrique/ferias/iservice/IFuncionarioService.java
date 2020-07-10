package com.hyagohenrique.ferias.iservice;

import java.util.Date;
import java.util.List;

import com.hyagohenrique.ferias.model.Funcionario;

public interface IFuncionarioService {
    
    Funcionario salvar(Funcionario funcionario);

    List<Funcionario> listarFuncionarios();

    Funcionario buscarPorId(Long id);

	List<Funcionario> listarFuncionarioQueDevemTirarFerias(int quantidadeDeMeses);

    void validaSeFuncionarioTemUmAnoDeEmpresa(Funcionario funcionario);
    
    void validaSeFuncionarioEstaImpedidoDeTirarFeriasPorContaDeOutraPessoaDaMesmaEquipe(Long idEquipe, Date inicio);
}