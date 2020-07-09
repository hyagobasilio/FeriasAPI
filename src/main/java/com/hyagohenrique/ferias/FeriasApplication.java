package com.hyagohenrique.ferias;

import com.hyagohenrique.ferias.irepository.IUsuarioRepository;
import com.hyagohenrique.ferias.iservice.IUsuarioService;
import com.hyagohenrique.ferias.model.Usuario;
import com.hyagohenrique.ferias.service.UsuarioService;
import com.hyagohenrique.ferias.utils.Bcrypt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FeriasApplication {

	public static void main(final String[] args) {
		SpringApplication.run(FeriasApplication.class, args);
	}

}
