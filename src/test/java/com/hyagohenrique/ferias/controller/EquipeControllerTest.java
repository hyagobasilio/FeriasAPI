package com.hyagohenrique.ferias.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyagohenrique.ferias.dto.EquipeDTO;
import com.hyagohenrique.ferias.iservice.IEquipeService;
import com.hyagohenrique.ferias.model.Equipe;

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
public class EquipeControllerTest {

    @MockBean
    private IEquipeService equipeService;
    
    private static final String NOME = "EQUIPE 1";
    private static final String URL = "/equipe";
    private static final Long ID = 1L;

    @Autowired
    MockMvc mvc;
    
    @WithMockUser
    @Test
    public void testCadastroEquipe() throws JsonProcessingException, Exception {

        BDDMockito.given(this.equipeService.salvar(Mockito.any(Equipe.class))).willReturn(getMockEquipe());

        mvc.perform(MockMvcRequestBuilders.post(URL).content(getJsonPayload())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$.data.id").value(ID))
		.andExpect(jsonPath("$.data.nome").value(NOME));
    }

    
    @WithMockUser
    @Test
    public void testListarEquipes() throws JsonProcessingException, Exception {

        List<Equipe> lista = new ArrayList<>();
        lista.add(getMockEquipe());

        BDDMockito.given(this.equipeService.listarEquipes()).willReturn(lista);

        mvc.perform(MockMvcRequestBuilders.get(URL)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data.[0].id").value(ID))
		.andExpect(jsonPath("$.data.[0].nome").value(NOME));
    }

    public String getJsonPayload() throws JsonProcessingException {
		EquipeDTO dto = new EquipeDTO();
		dto.setId(1L);
		dto.setNome(NOME);

		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(dto);
	}

    public Equipe getMockEquipe() {
        Equipe equipe = new Equipe();
        equipe.setId(ID);
        equipe.setNome(NOME);
        return equipe;
    }
}