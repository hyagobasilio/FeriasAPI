package com.hyagohenrique.ferias.security.controller;

import java.util.Optional;

import javax.validation.Valid;

import com.hyagohenrique.ferias.enums.Role;
import com.hyagohenrique.ferias.irepository.IUsuarioRepository;
import com.hyagohenrique.ferias.model.Usuario;
import com.hyagohenrique.ferias.response.Response;
import com.hyagohenrique.ferias.security.dto.JwtAuthenticationDTO;
import com.hyagohenrique.ferias.security.dto.TokenDTO;
import com.hyagohenrique.ferias.security.utils.JwtTokenUtil;
import com.hyagohenrique.ferias.utils.Bcrypt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	IUsuarioRepository usuarioRepository;

	@PostMapping
	public ResponseEntity<Response<TokenDTO>> gerarTokenJwt(
			@Valid @RequestBody JwtAuthenticationDTO authenticationDto, BindingResult result)
			throws AuthenticationException {
		Response<TokenDTO> response = new Response<TokenDTO>();

		if (result.hasErrors()) {
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}

		Optional<Usuario> usuario = usuarioRepository.findByEmail(authenticationDto.getEmail());
		
		if(!usuario.isPresent()) {
			Usuario u = new Usuario();
			u.setNome("Administrador");	
			u.setRole(Role.ROLE_ADMIN);
			u.setEmail("administrador.ferias@castgroup.com.br");
			u.setPassword(Bcrypt.getHash("123a"));
			usuarioRepository.save(u);
		}
		

		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				authenticationDto.getEmail(), authenticationDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationDto.getEmail());
		String token = jwtTokenUtil.getToken(userDetails);
		response.setData(new TokenDTO(token));

		return ResponseEntity.ok(response);
	}


}