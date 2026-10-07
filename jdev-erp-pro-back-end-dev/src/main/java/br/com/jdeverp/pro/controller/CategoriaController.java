package br.com.jdeverp.pro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jdeverp.pro.dto.UsuarioDto;
import br.com.jdeverp.pro.model.Categoria;
import br.com.jdeverp.pro.service.CategoriaService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categoria")
public class CategoriaController {
	
	@Autowired
	private CategoriaService categoriaService;
	
	@PostMapping("/salvar")
	public ResponseEntity<Categoria> salvar(@RequestBody @Valid Categoria categoria) {
		Categoria usuarioSalvo = categoriaService.salvar(categoria);
		return ResponseEntity.ok(usuarioSalvo);
	}
	
	
	
	@PostMapping("/atualizar")
	public ResponseEntity<Categoria> atualizar(@RequestBody @Valid Categoria categoria) {
		Categoria usuarioSalvo = categoriaService.atualizar(categoria);
		return ResponseEntity.ok(usuarioSalvo);
	}
	
	
	

}
