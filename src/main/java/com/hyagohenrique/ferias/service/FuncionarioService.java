package com.hyagohenrique.ferias.service;

import java.util.List;
import java.util.Optional;

import com.hyagohenrique.ferias.irepository.IFuncionarioRepository;
import com.hyagohenrique.ferias.iservice.IFuncionarioService;
import com.hyagohenrique.ferias.model.Funcionario;

import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FuncionarioService implements IFuncionarioService {
    @Autowired
    private IFuncionarioRepository funcionarioRepository;

    @Override
    public Funcionario salvar(Funcionario funcionario) {
        funcionario = this.funcionarioRepository.save(funcionario);
        String matricula = StringUtils.leftPad(funcionario.getId().toString(), 6, '0');
        funcionario.setMatricula(matricula);
        funcionario = this.funcionarioRepository.save(funcionario);

        return funcionario;
    }

    @Override
    public List<Funcionario> listarFuncionarios() {
        return this.funcionarioRepository.findAll();
    }

    @Override
    public Optional<Funcionario> buscarPorId(Long id) {
        return this.funcionarioRepository.findById(id);
    }
    
}