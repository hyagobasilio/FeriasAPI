package com.hyagohenrique.ferias.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.hyagohenrique.ferias.irepository.IEquipeRepository;
import com.hyagohenrique.ferias.irepository.IFuncionarioRepository;
import com.hyagohenrique.ferias.iservice.IFuncionarioService;
import com.hyagohenrique.ferias.model.Endereco;
import com.hyagohenrique.ferias.model.Equipe;
import com.hyagohenrique.ferias.model.Funcionario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class FuncionarioServiceTest {
    
    @MockBean
    private IFuncionarioRepository funcionarioRepository;
    @Autowired
    private IFuncionarioService funcionarioService;

    @Autowired
    private IEquipeRepository equipeRepository;

    private static final String NOME = "Fulano da Silva";
    private static final String DATA_NASCIMENTO = "07-07-1992";
    private static final String ENDERECO = "RUA NOVA";
    private static final String DATA_CONTRATACAO = "07-07-2020";


    @Test
    public void testSalvarFuncionario() throws ParseException {
        BDDMockito.given(funcionarioRepository.save(Mockito.any(Funcionario.class))).willReturn(getMockFuncionario());
        
        Funcionario funcionario = this.funcionarioService.salvar(getMockFuncionario());

        assertNotNull(funcionario);
        assertEquals(NOME, funcionario.getNome());
        assertEquals(6, funcionario.getMatricula().length());
    }
    
    @Test
    public void testListarFuncionariosPrestesATirarFerias() throws ParseException {
        List<Funcionario> lista = new ArrayList<>();
        lista.add(getMockFuncionario());

        BDDMockito.given(this.funcionarioService.listarFuncionarios()).willReturn(lista);

        List<Funcionario> resposta = this.funcionarioService.listarFuncionarios();

        assertNotNull(resposta);
        assertEquals(1, resposta.size());
        assertEquals(NOME, resposta.get(0).getNome());
    }

    @Test
    public void testListarFuncionarios() throws ParseException {
        List<Funcionario> lista = new ArrayList<>();
        lista.add(getMockFuncionario());

        BDDMockito.given(this.funcionarioService.listarFuncionarios()).willReturn(lista);

        List<Funcionario> resposta = this.funcionarioService.listarFuncionarios();

        assertNotNull(resposta);
        assertEquals(1, resposta.size());
        assertEquals(NOME, resposta.get(0).getNome());
    }

    private Funcionario getMockFuncionario() throws ParseException {
        Equipe equipe = new Equipe();
        equipe.setNome("Equipe 1");
        this.equipeRepository.save(equipe);
        
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1L);
        funcionario.setNome(NOME);
        funcionario.setDataNascimento(new SimpleDateFormat("dd-MM-yyyy").parse(DATA_NASCIMENTO));
        funcionario.setDataContratacao(new SimpleDateFormat("dd-MM-yyyy").parse(DATA_CONTRATACAO));
        funcionario.setEndereco(new Endereco(ENDERECO));
        funcionario.setEquipe(equipe);

        return funcionario;
    }
}