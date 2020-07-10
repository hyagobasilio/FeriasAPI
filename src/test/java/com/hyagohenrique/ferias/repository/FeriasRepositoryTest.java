package com.hyagohenrique.ferias.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.hyagohenrique.ferias.irepository.IEquipeRepository;
import com.hyagohenrique.ferias.irepository.IFeriasRepository;
import com.hyagohenrique.ferias.irepository.IFuncionarioRepository;
import com.hyagohenrique.ferias.model.Endereco;
import com.hyagohenrique.ferias.model.Equipe;
import com.hyagohenrique.ferias.model.Ferias;
import com.hyagohenrique.ferias.model.Funcionario;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class FeriasRepositoryTest {
    
    @Autowired
    private IFeriasRepository feriasRepository;
    @Autowired
    private IFuncionarioRepository funcionarioRepository;
    @Autowired
    private IEquipeRepository equipeRepository;

    private static final String INICIO = "01-06-2020";
    private static final String FIM = "30-06-2020";

    @After
    public void afterEach() {
        feriasRepository.deleteAll();
    }

    @Test
    public void testCadastrarFerias() throws ParseException {

        Funcionario funcionario = getFuncionario();
        this.funcionarioRepository.save(funcionario);

        Ferias ferias = new Ferias();
        Date dataInicio = new SimpleDateFormat("dd-MM-yyyy").parse(INICIO);
        Date dataFim = new SimpleDateFormat("dd-MM-yyyy").parse(FIM);
        ferias.setInicio(dataInicio);
        ferias.setFim(dataFim);
        ferias.setFuncionario(funcionario);

        feriasRepository.save(ferias);

        assertNotNull(ferias.getId());
        assertEquals(dataInicio, ferias.getInicio());
        assertEquals(dataFim, ferias.getFim());
        assertEquals(funcionario.getId(), ferias.getFuncionario().getId());
    }

    @Test
    public void testBuscaDeFeriasPorMatriculaDoFuncionario() throws ParseException {
        String matricula = "000001";
        Funcionario funcionario = getFuncionario();
        funcionario.setMatricula(matricula);
        this.funcionarioRepository.save(funcionario);

        Ferias ferias = new Ferias();
        Date dataInicio = new SimpleDateFormat("dd-MM-yyyy").parse(INICIO);
        Date dataFim = new SimpleDateFormat("dd-MM-yyyy").parse(FIM);
        ferias.setInicio(dataInicio);
        ferias.setFim(dataFim);
        ferias.setFuncionario(funcionario);

        feriasRepository.save(ferias);

        List<Ferias> feriasList = this.feriasRepository.findAllByFuncionarioMatricula(matricula);

        assertNotNull(feriasList);
        assertEquals(1, feriasList.size());
        assertEquals(funcionario.getId(), feriasList.get(0).getFuncionario().getId());

    }
    @Test
    public void testBuscaDeFerias() throws ParseException {
        String matricula = "000001";
        Funcionario funcionario = getFuncionario();
        funcionario.setMatricula(matricula);
        this.funcionarioRepository.save(funcionario);

        Ferias ferias = new Ferias();
        Date dataInicio = new SimpleDateFormat("dd-MM-yyyy").parse(INICIO);
        Date dataFim = new SimpleDateFormat("dd-MM-yyyy").parse(FIM);
        ferias.setInicio(dataInicio);
        ferias.setFim(dataFim);
        ferias.setFuncionario(funcionario);

        feriasRepository.save(ferias);

        List<Ferias> feriasList = this.feriasRepository.findAll();

        assertNotNull(feriasList);
        assertEquals(1, feriasList.size());

    }


    private Funcionario getFuncionario() throws ParseException {
        Equipe equipe = new Equipe();
        equipe.setNome("Equipe 1");
        this.equipeRepository.save(equipe);

        Funcionario funcionario = new Funcionario();
        funcionario.setNome("NOME");
        funcionario.setDataNascimento(new SimpleDateFormat("dd-MM-yyyy").parse("07-07-1992"));
        funcionario.setDataContratacao(new SimpleDateFormat("dd-MM-yyyy").parse("07-07-2020"));
        funcionario.setEndereco(new Endereco("Av Brasil"));
        funcionario.setEquipe(equipe);
        return funcionario;
    }


}