package com.hyagohenrique.ferias.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.hyagohenrique.ferias.irepository.IEquipeRepository;
import com.hyagohenrique.ferias.irepository.IFeriasRepository;
import com.hyagohenrique.ferias.irepository.IFuncionarioRepository;
import com.hyagohenrique.ferias.model.Endereco;
import com.hyagohenrique.ferias.model.Equipe;
import com.hyagohenrique.ferias.model.Ferias;
import com.hyagohenrique.ferias.model.Funcionario;
import com.hyagohenrique.ferias.utils.DateUtils;

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
public class FuncionarioRespositoryTest {

    @Autowired
    private IFuncionarioRepository funcionarioRepository;
    @Autowired
    private IEquipeRepository equipeRepository;
    @Autowired
    private IFeriasRepository feriasRepository;

    private static final String NOME = "Fulano da Silva";
    private static final String DATA_NASCIMENTO = "07-07-1992";
    private static final String ENDERECO = "RUA NOVA";
    private static final String DATA_CONTRATACAO = "07-07-2020";
    private static final String NOME_EQUIPE = "EQUIPE1";

    @After
    public void afterEach() {
        feriasRepository.deleteAll();
        funcionarioRepository.deleteAll();
        equipeRepository.deleteAll();

    }

    @Test
    public void testFuncionarioComFeriasNaMesmaEquipeENoMesmoPeriodo() throws ParseException {

        Funcionario funcionario= getFuncionario();
        this.funcionarioRepository.save(funcionario);

        LocalDate inicio = LocalDate.now().minusMonths(2);
        LocalDate fim  = LocalDate.now().minusMonths(1);
        
        Date inicioDate = DateUtils.convertLocalDateToDate(inicio);
        Date fimDate = DateUtils.convertLocalDateToDate(fim);

        Ferias ferias = new Ferias();
        ferias.setFuncionario(funcionario);
        ferias.setInicio(inicioDate);
        ferias.setFim(fimDate);
        this.feriasRepository.save(ferias);

        List<Funcionario> lista = this.funcionarioRepository.funcionarioQueEstaoDeFeriasNoMesmoTempoDesejado(funcionario.getEquipe().getId(), inicioDate);

        assertFalse(lista.isEmpty());
        assertEquals(1, lista.size());
        
    }


    @Test
    public void testQuantidadeDeFuncionariosPorEquipe() throws ParseException {
        Funcionario funcionario = getFuncionario();
        this.funcionarioRepository.save(funcionario);

        long quantidadePorEquipe = this.funcionarioRepository.countByEquipeId(funcionario.getEquipe().getId());
        long esperado = Long.parseLong("1");

        assertTrue(esperado == quantidadePorEquipe);
    }
    
    /**
     * Consultar funcionarios que irao completar 2 anos sem solicitar ferias
     * em no maximo x meses
     * 
     * consultar funcionarios que nao tem registro de ferias e que foram contratados entre (hoje - 2 anos) e (hoje - 2 anos e x meses)
     * ou que tenham registro de ferias porém a data final está entre (hoje - 2 anos) e (hoje - 2 anos e x meses)
     */
    @Test
    public void testConsultarFuncionariosQueIraoCompletar2AnosSemSolicitarFeriasEmNoMaximoXMeses() {
        int numeroDeMeses = 2;

        LocalDate hoje = LocalDate.now();
        LocalDate inicio = hoje.minusYears(2);
        LocalDate fim  = inicio.minusMonths(numeroDeMeses);
        
        Date inicioDate = DateUtils.convertLocalDateToDate(inicio);
        Date fimDate = DateUtils.convertLocalDateToDate(fim);
        List<Funcionario> funcionarios = this.funcionarioRepository.consultarFuncionariosQueIraoCompletar2AnosSemSolicitarFeriasEmNoMaximoXMeses(inicioDate, fimDate);

        assertTrue(funcionarios.isEmpty());
    }

    /**
     * Consultar funcionarios que irao completar 2 anos sem solicitar ferias em no
     * maximo x meses e ainda nao tirou ferias
     * 
     * @throws ParseException
     * 
     */
    @Test
    public void testRetornoFuncionarioQueAindaNaoTirouFerias() throws ParseException {
        int numeroDeMeses = 2;
        
        LocalDate hoje = LocalDate.now();
        LocalDate fim = hoje.minusYears(2);
        LocalDate inicio  = fim.minusMonths(numeroDeMeses);

        Date inicioDate = DateUtils.convertLocalDateToDate(inicio);
        Date fimDate = DateUtils.convertLocalDateToDate(fim);

        System.out.println(inicio);
        System.out.println(fim);

        Funcionario funcionario = getFuncionario();
        funcionario.setDataContratacao(inicioDate);
        funcionario.setEquipe(getEquipe());
        this.funcionarioRepository.save(funcionario);
        

        List<Funcionario> funcionarios = this.funcionarioRepository.consultarFuncionariosQueIraoCompletar2AnosSemSolicitarFeriasEmNoMaximoXMeses(inicioDate, fimDate);

        assertFalse(funcionarios.isEmpty());
        assertEquals(1, funcionarios.size());
    }

    /**
     * Consultar funcionarios que irao completar 2 anos sem solicitar ferias em no
     * maximo x meses e ja tirou ferias porem terá que tirar novamente
     * 
     * @throws ParseException
     * 
     */
    @Test
    public void testRetornoFuncionarioQueTeraQueTirarFeriasNovamente() throws ParseException {
        int numeroDeMeses = 2;
        
        LocalDate hoje = LocalDate.now();
        LocalDate fim = hoje.minusYears(2);
        LocalDate inicio  = fim.minusMonths(numeroDeMeses);

        Date inicioDate = DateUtils.convertLocalDateToDate(inicio);
        Date fimDate = DateUtils.convertLocalDateToDate(fim);

        System.out.println(inicio);
        System.out.println(fim);

        Funcionario funcionario = getFuncionario();
        funcionario.setDataContratacao(DateUtils.convertLocalDateToDate(LocalDate.of(2014,05,01)));
        funcionario.setEquipe(getEquipe());
        this.funcionarioRepository.save(funcionario);


        Ferias ferias = new Ferias();
        ferias.setInicio(inicioDate);
        ferias.setFim(fimDate);
        ferias.setFuncionario(funcionario);
        feriasRepository.save(ferias);

        List<Funcionario> funcionarios = this.funcionarioRepository.consultarFuncionariosQueIraoCompletar2AnosSemSolicitarFeriasEmNoMaximoXMeses(inicioDate, fimDate);

        assertFalse(funcionarios.isEmpty());
        assertEquals(1, funcionarios.size());
    }

    @Test
    public void testSalvarFuncionario() throws ParseException {
        
        Funcionario funcionario = getFuncionario();
        funcionario.setEquipe(getEquipe());
        Funcionario saved = this.funcionarioRepository.save(funcionario);

        assertNotNull(saved);
        assertEquals(NOME, saved.getNome());
        assertEquals(DATA_NASCIMENTO, new SimpleDateFormat("dd-MM-yyyy").format(saved.getDataNascimento()));
        assertEquals(DATA_CONTRATACAO, new SimpleDateFormat("dd-MM-yyyy").format(saved.getDataContratacao()));
        assertEquals(ENDERECO, saved.getEndereco().getRua());
        assertNotNull(funcionario.getEquipe().getId());
        assertEquals(funcionario.getEquipe().getNome(), saved.getEquipe().getNome());
    }

    @Test
    public void testConsultarFuncionario() throws ParseException {
        Funcionario funcionario = getFuncionario();
        funcionario.setEquipe(getEquipe());
        this.funcionarioRepository.save(funcionario);

        Optional<Funcionario> retorno = this.funcionarioRepository.findById(funcionario.getId());

        assertTrue(retorno.isPresent());
        assertEquals(NOME, retorno.get().getNome());
        assertEquals(DATA_NASCIMENTO, new SimpleDateFormat("dd-MM-yyyy").format(retorno.get().getDataNascimento()));
        assertEquals(DATA_CONTRATACAO, new SimpleDateFormat("dd-MM-yyyy").format(retorno.get().getDataContratacao()));
        assertEquals(ENDERECO, retorno.get().getEndereco().getRua());
    }
    
    @Test
    public void testListarFuncionario() throws ParseException {
        Equipe equipe = getEquipe();

        this.funcionarioRepository.save(setEquipe(getFuncionario(), equipe));
        this.funcionarioRepository.save(setEquipe(getFuncionario(), equipe));

        List<Funcionario> funcionarios = this.funcionarioRepository.findAll();
        assertEquals(2, funcionarios.size());
    }

    private Funcionario setEquipe(Funcionario f, Equipe e ) {
        f.setEquipe(e);
        return f;
    }

    private Equipe getEquipe() {
        Equipe equipe = new Equipe();
        equipe.setNome(NOME_EQUIPE);
        return this.equipeRepository.save(equipe);

    }

    private Funcionario getFuncionario() throws ParseException {
        Equipe equipe = new Equipe();
        equipe.setNome("Equipe 1");
        this.equipeRepository.save(equipe);

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(NOME);
        funcionario.setDataNascimento(new SimpleDateFormat("dd-MM-yyyy").parse(DATA_NASCIMENTO));
        funcionario.setDataContratacao(new SimpleDateFormat("dd-MM-yyyy").parse(DATA_CONTRATACAO));
        funcionario.setEndereco(new Endereco(ENDERECO));
        funcionario.setEquipe(equipe);

        return funcionario;
    }
}