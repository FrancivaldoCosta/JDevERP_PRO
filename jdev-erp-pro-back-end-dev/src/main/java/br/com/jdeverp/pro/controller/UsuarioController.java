package br.com.jdeverp.pro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.jdeverp.pro.dto.AlterarSenhaDTO;
import br.com.jdeverp.pro.dto.LoginDTO;
import br.com.jdeverp.pro.dto.TokenDTO;
import br.com.jdeverp.pro.dto.UsuarioDto;
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
	
	
	@PostMapping("/salvar")
	public ResponseEntity<UsuarioDto> salvar(@RequestBody @Valid UsuarioDto usuarioDto) {
		UsuarioDto usuarioSalvo = usuarioService.salvar(usuarioDto);
		return ResponseEntity.ok(usuarioSalvo);
	}
	
	
	
	@PostMapping("/atualizar")
	public ResponseEntity<UsuarioDto> atualizar(@RequestBody @Valid UsuarioDto usuarioDto) {
		UsuarioDto usuarioSalvo = usuarioService.atualizar(usuarioDto);
		return ResponseEntity.ok(usuarioSalvo);
	}
	

	/* Ponto de acesso (end-point): /api/usuario/login */
	@PostMapping("/login")
	public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO login) {

		TokenDTO TokenDTO = usuarioService.login(login);

		return ResponseEntity.ok(TokenDTO);

	}
	
	@PostMapping("/alterarSenha")
	public ResponseEntity<String> alterarSenha(@RequestBody @Valid AlterarSenhaDTO dto) {
		
		usuarioService.alterarSenha(dto);
		
		return ResponseEntity.ok("Senha alterada com sucesso!");
		
	}
	
	@GetMapping("/listar")
	public ResponseEntity<List<UsuarioDto>> listarUsuarios() {
		return ResponseEntity.ok(usuarioService.listar(usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/buscaPorNome/{nome}")
	public ResponseEntity<List<UsuarioDto>> buscaPorNome(@PathVariable String nome) {
		return ResponseEntity.ok(usuarioService.buscaPorNomeDto(nome, usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/buscaPorLogin/{login}")
	public ResponseEntity<UsuarioDto> buscaPorLogin(@PathVariable String login) {
		return ResponseEntity.ok(usuarioService.buscaPorLoginDto(login, usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/existePorNome/{nome}")
	public ResponseEntity<Boolean> existePorNome(@PathVariable String nome) {
		return ResponseEntity.ok(usuarioService.existePorNome(nome, usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/existePorNomeDiferenteId/{id}/{nome}")
	public ResponseEntity<Boolean> existePorNomeDiferenteId(@PathVariable("id") Long idUser, @PathVariable String nome) {
		return ResponseEntity.ok(usuarioService.existePorNomeDiferenteId(idUser, nome, usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/total")
	public ResponseEntity<Long> totalUsuarios() {
		return ResponseEntity.ok(usuarioService.total(usuarioLogadoService.getEmpresaIdLogada()));
	}
	
	@GetMapping("/buscarPorId/{id}")
	public ResponseEntity<UsuarioDto> buscarPorId(@PathVariable(required = true, value = "id") Long idUser) {
		return ResponseEntity.ok(usuarioService.buscarPorIdDto(idUser, usuarioLogadoService.getEmpresaIdLogada()));
		
		
	}
	
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<String> deletePorId(@PathVariable(required = true, value = "id") Long idUser) {
		
		usuarioService.deleteById(idUser, usuarioLogadoService.getEmpresaIdLogada());
		
		
		return ResponseEntity.ok("Usuario deletado com sucesso!");
		
				
	}

	@DeleteMapping("/deletarTodos")
	public ResponseEntity<Long> deletarTodos() {
		return ResponseEntity.ok(usuarioService.deleteAll(usuarioLogadoService.getEmpresaIdLogada()));
	}

	@PostMapping("/buscarPorIds")
	public ResponseEntity<List<UsuarioDto>> buscarPorIds(@RequestBody List<Long> ids) {
		return ResponseEntity.ok(usuarioService.buscarPorIdsDto(ids, usuarioLogadoService.getEmpresaIdLogada()));
	}
	
	
	@GetMapping("/paginado")
	public ResponseEntity<List<UsuarioDto>> listarPaginado(@RequestParam(defaultValue = "0") int page,
			                                               @RequestParam(defaultValue = "10") int size,
			                                               @RequestParam(defaultValue = "id") String sort,
			                                               @RequestParam(defaultValue = "asc") String direction) { 
		
		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;	
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

		List<UsuarioDto> retorno = usuarioService.listarPaginadoDto(usuarioLogadoService.getEmpresaIdLogada(), pageable);
		
		return ResponseEntity.ok(retorno);
		
	
		
	}  
	
}
