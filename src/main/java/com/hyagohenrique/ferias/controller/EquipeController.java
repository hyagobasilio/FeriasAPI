package com.hyagohenrique.ferias.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.hyagohenrique.ferias.dto.EquipeDTO;
import com.hyagohenrique.ferias.iservice.IEquipeService;
import com.hyagohenrique.ferias.model.Equipe;
import com.hyagohenrique.ferias.response.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("equipe")
public class EquipeController {
    
    @Autowired
    private IEquipeService equipeService;

    @GetMapping
    public ResponseEntity<Response<List<EquipeDTO>>> index() {
        List<EquipeDTO> lista = equipeService.listarEquipes().stream().map(i -> i.converteParaDTO()).collect(Collectors.toList());

        Response<List<EquipeDTO>> response = new Response<>();
        response.setData(lista);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Response<Equipe>> salvar(@Valid @RequestBody EquipeDTO equipe, BindingResult result) {
        Response<Equipe> response = new Response<>();
        if (result.hasErrors()) {
			result.getAllErrors().forEach(e -> response.getErrors().add(e.getDefaultMessage()));
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
        Equipe equipeSalva = this.equipeService.salvar(equipe.converteParaEntidade());
        response.setData(equipeSalva);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}