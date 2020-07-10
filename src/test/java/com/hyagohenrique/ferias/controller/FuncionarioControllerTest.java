package com.hyagohenrique.ferias.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyagohenrique.ferias.dto.FuncionarioDTO;
import com.hyagohenrique.ferias.iservice.IFuncionarioService;
import com.hyagohenrique.ferias.model.Endereco;
import com.hyagohenrique.ferias.model.Equipe;
import com.hyagohenrique.ferias.model.Funcionario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FuncionarioControllerTest {

  private static final Long ID = 1L;
  private static final String NOME = "Fulano da Silva";
  private static final String DATA_NASCIMENTO = "07-07-1992";
  private static final String ENDERECO = "RUA NOVA";
  private static final String DATA_CONTRATACAO = "07-07-2020";
  private static final String URL = "/funcionario";

  @MockBean
  private IFuncionarioService funcionarioService;

  @Autowired
  MockMvc mvc;

  @WithMockUser
  @Test
  public void testCadastroFuncionario() throws JsonProcessingException, Exception {

    BDDMockito.given(this.funcionarioService.salvar(Mockito.any(Funcionario.class))).willReturn(getMockFuncionario());

    mvc.perform(MockMvcRequestBuilders.post(URL).content(getJsonPayload()).contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andExpect(jsonPath("$.data.id").value(ID))
        .andExpect(jsonPath("$.data.nome").value(NOME))
        .andExpect(jsonPath("$.data.dataNascimento").value(DATA_NASCIMENTO))
        .andExpect(jsonPath("$.data.dataContratacao").value(DATA_CONTRATACAO));
  }

  @WithMockUser
  @Test
  public void testListarFuncionarios() throws JsonProcessingException, Exception {

    List<Funcionario> lista = new ArrayList<>();
    lista.add(getMockFuncionario());

    BDDMockito.given(this.funcionarioService.listarFuncionarios()).willReturn(lista);

    mvc.perform(MockMvcRequestBuilders.get(URL).content(getJsonPayload())
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(jsonPath("$.data.[0].id").value(ID))
        .andExpect(jsonPath("$.data.[0].nome").value(NOME))
        .andExpect(jsonPath("$.data.[0].dataNascimento").value(DATA_NASCIMENTO))
        .andExpect(jsonPath("$.data.[0].dataContratacao").value(DATA_CONTRATACAO));
  }

  @WithMockUser
  @Test
  public void testListarFuncionariosDisponiveisParaFerias() throws JsonProcessingException, Exception {

    List<Funcionario> lista = new ArrayList<>();
    lista.add(getMockFuncionario());

    BDDMockito.given(this.funcionarioService.listarFuncionarioQueDevemTirarFerias(Mockito.anyInt())).willReturn(lista);

    mvc.perform(MockMvcRequestBuilders.get(URL+"/ferias/2").content(getJsonPayload())
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(jsonPath("$.data.[0].id").value(ID))
        .andExpect(jsonPath("$.data.[0].nome").value(NOME))
        .andExpect(jsonPath("$.data.[0].dataNascimento").value(DATA_NASCIMENTO))
        .andExpect(jsonPath("$.data.[0].dataContratacao").value(DATA_CONTRATACAO));
  }

  private String getJsonPayload() throws ParseException, JsonProcessingException {
    FuncionarioDTO dto = new FuncionarioDTO();
    dto.setId(ID);
    dto.setNome(NOME);
    dto.setDataNascimento(new SimpleDateFormat("dd-MM-yyyy").parse(DATA_NASCIMENTO));
    dto.setDataContratacao(new SimpleDateFormat("dd-MM-yyyy").parse(DATA_CONTRATACAO));
    dto.setRua(ENDERECO);
    dto.setEquipeId(1L);

    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(dto);
  }

  private Funcionario getMockFuncionario() throws ParseException {
    Equipe equipe = new Equipe();
    equipe.setNome("Equipe 1");

    Funcionario funcionario = new Funcionario();
    funcionario.setId(ID);
    funcionario.setNome(NOME);
    funcionario.setDataNascimento(new SimpleDateFormat("dd-MM-yyyy").parse(DATA_NASCIMENTO));
    funcionario.setDataContratacao(new SimpleDateFormat("dd-MM-yyyy").parse(DATA_CONTRATACAO));
    funcionario.setEndereco(new Endereco(ENDERECO));
    funcionario.setEquipe(equipe);

    return funcionario;
  }
}