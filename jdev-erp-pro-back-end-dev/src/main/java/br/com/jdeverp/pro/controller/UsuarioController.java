package br.com.jdeverp.pro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jdeverp.pro.dto.LoginDTO;
import br.com.jdeverp.pro.dto.TokenDTO;
import br.com.jdeverp.pro.model.Usuario;
import br.com.jdeverp.pro.service.UsuarioLogadoService;
import br.com.jdeverp.pro.service.UsuarioService;
import jakarta.validation.Valid;

/*Cada controller tem um contexto, recebe dados do front-end, ape, integração e etc*/
@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

	 
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private UsuarioLogadoService usuarioLogadoService;

	/* Ponto de acesso (end-point): /api/usuario/login */
	@PostMapping("/login")
	public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO login) {

		TokenDTO TokenDTO = usuarioService.login(login);

		return ResponseEntity.ok(TokenDTO);

	}
	
	@GetMapping("/listar")
	public ResponseEntity<List<Usuario>> listarUsuarios() {
		return ResponseEntity.ok(usuarioService.listar(usuarioLogadoService.getEmpresaIdLogada()));
	}
	
	

}
