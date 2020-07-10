package com.hyagohenrique.ferias.iservice;

import java.util.Date;
import java.util.List;

import com.hyagohenrique.ferias.model.Funcionario;

import org.springframework.web.multipart.MultipartFile;

public interface IFuncionarioService {
    
    Funcionario salvar(Funcionario funcionario);
    Funcionario salvar(Funcionario funcionario, MultipartFile file);

    List<Funcionario> listarFuncionarios();

    Funcionario buscarPorId(Long id);

	List<Funcionario> listarFuncionarioQueDevemTirarFerias(int quantidadeDeMeses);

    void validaSeFuncionarioTemUmAnoDeEmpresa(Funcionario funcionario);
    
    void validaSeFuncionarioEstaImpedidoDeTirarFeriasPorContaDeOutraPessoaDaMesmaEquipe(Long idEquipe, Date inicio);
}