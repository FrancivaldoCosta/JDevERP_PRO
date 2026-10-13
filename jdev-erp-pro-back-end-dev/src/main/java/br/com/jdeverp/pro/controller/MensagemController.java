package br.com.jdeverp.pro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.jdeverp.pro.model.Mensagem;
import br.com.jdeverp.pro.service.MensagemService;
import br.com.jdeverp.pro.service.UsuarioLogadoService;

@RestController
@RequestMapping("/api/mensagem")
public class MensagemController {

	@Autowired
	private MensagemService mensagemService;

	@Autowired
	private UsuarioLogadoService usuarioLogadoService;

	private Long empresa() {
		return usuarioLogadoService.getEmpresaIdLogada();
	}

	// O conteúdo vai como query param por poder conter "/" e textos longos.

	@GetMapping("/listar")
	public ResponseEntity<List<Mensagem>> listar() {
		return ResponseEntity.ok(mensagemService.listar(empresa()));
	}

	@GetMapping("/findAll")
	public ResponseEntity<List<Mensagem>> findAll() {
		return ResponseEntity.ok(mensagemService.findAll(empresa()));
	}

	@GetMapping("/buscarPorId/{id}")
	public ResponseEntity<Mensagem> buscarPorId(@PathVariable("id") Long id) {
		return mensagemService.buscarPorId(id, empresa())
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/buscarPorIds")
	public ResponseEntity<List<Mensagem>> buscarPorIds(@RequestBody List<Long> ids) {
		return ResponseEntity.ok(mensagemService.buscarPorIds(ids, empresa()));
	}

	@GetMapping("/buscaPorConteudo")
	public ResponseEntity<List<Mensagem>> buscaPorConteudo(@RequestParam String conteudo) {
		return ResponseEntity.ok(mensagemService.buscaPorConteudo(conteudo, empresa()));
	}

	@GetMapping("/existePorConteudo")
	public ResponseEntity<Boolean> existePorConteudo(@RequestParam String conteudo) {
		return ResponseEntity.ok(mensagemService.existePorConteudo(conteudo, empresa()));
	}

	@GetMapping("/existePorConteudoDiferenteId/{id}")
	public ResponseEntity<Boolean> existePorConteudoDiferenteId(@PathVariable("id") Long id,
			@RequestParam String conteudo) {
		return ResponseEntity.ok(mensagemService.existePorConteudoDiferenteId(id, conteudo, empresa()));
	}

	@GetMapping("/existsById/{id}")
	public ResponseEntity<Boolean> existsById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(mensagemService.existsById(id, empresa()));
	}

	@GetMapping("/total")
	public ResponseEntity<Long> total() {
		return ResponseEntity.ok(mensagemService.total(empresa()));
	}

	@GetMapping("/paginado")
	public ResponseEntity<Page<Mensagem>> listarPaginado(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sort,
			@RequestParam(defaultValue = "asc") String direction) {

		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

		return ResponseEntity.ok(mensagemService.listarPaginado(empresa(), pageable));
	}

	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<String> deletePorId(@PathVariable("id") Long id) {
		mensagemService.deleteById(id, empresa());
		return ResponseEntity.ok("Mensagem deletada com sucesso.");
	}

	@PostMapping("/deletarPorIds")
	public ResponseEntity<String> deletarAllById(@RequestBody List<Long> ids) {
		mensagemService.deletarAllById(ids, empresa());
		return ResponseEntity.ok("Mensagens deletadas com sucesso.");
	}

	@DeleteMapping("/deletarTodos")
	public ResponseEntity<Long> deleteAll() {
		return ResponseEntity.ok(mensagemService.deleteAll(empresa()));
	}

	// ====================Métodos específicos para Chamado====================

	@GetMapping("/chamado/{idChamado}")
	public ResponseEntity<List<Mensagem>> findAllByChamado(@PathVariable Long idChamado) {
		return ResponseEntity.ok(mensagemService.findAllByChamado(idChamado, empresa()));
	}

	@GetMapping("/chamado/{idChamado}/buscaPorConteudo")
	public ResponseEntity<List<Mensagem>> buscaPorConteudoByChamado(@PathVariable Long idChamado,
			@RequestParam String conteudo) {
		return ResponseEntity.ok(mensagemService.buscaPorConteudoByChamado(conteudo, idChamado, empresa()));
	}

	@GetMapping("/chamado/{idChamado}/existePorConteudo")
	public ResponseEntity<Boolean> existePorConteudoByChamado(@PathVariable Long idChamado,
			@RequestParam String conteudo) {
		return ResponseEntity.ok(mensagemService.existePorConteudoByChamado(conteudo, idChamado, empresa()));
	}

	@GetMapping("/chamado/{idChamado}/existePorConteudoDiferenteId/{id}")
	public ResponseEntity<Boolean> existePorConteudoDiferenteIdByChamado(@PathVariable Long idChamado,
			@PathVariable("id") Long id, @RequestParam String conteudo) {
		return ResponseEntity.ok(
				mensagemService.existePorConteudoDiferenteIdByChamado(id, conteudo, idChamado, empresa()));
	}

	@GetMapping("/chamado/{idChamado}/total")
	public ResponseEntity<Long> countByChamado(@PathVariable Long idChamado) {
		return ResponseEntity.ok(mensagemService.countByChamado(idChamado, empresa()));
	}

	@DeleteMapping("/chamado/{idChamado}/deletarTodas")
	public ResponseEntity<String> deleteAllByChamado(@PathVariable Long idChamado) {
		mensagemService.deleteAllByChamado(idChamado, empresa());
		return ResponseEntity.ok("Mensagens do chamado deletadas com sucesso.");
	}

	@DeleteMapping("/chamado/{idChamado}/deletar/{id}")
	public ResponseEntity<String> deleteByIdAndChamado(@PathVariable Long idChamado, @PathVariable("id") Long id) {
		mensagemService.deleteByIdAndChamado(id, idChamado, empresa());
		return ResponseEntity.ok("Mensagem deletada com sucesso.");
	}

	// ====================Status de Leitura====================

	@GetMapping("/naoLidas")
	public ResponseEntity<List<Mensagem>> findAllNaoLidas() {
		return ResponseEntity.ok(mensagemService.findAllNaoLidas(empresa()));
	}

	@GetMapping("/chamado/{idChamado}/naoLidas")
	public ResponseEntity<List<Mensagem>> findAllNaoLidasByChamado(@PathVariable Long idChamado) {
		return ResponseEntity.ok(mensagemService.findAllNaoLidasByChamado(idChamado, empresa()));
	}

	@GetMapping("/chamado/{idChamado}/totalNaoLidas")
	public ResponseEntity<Long> countNaoLidasByChamado(@PathVariable Long idChamado) {
		return ResponseEntity.ok(mensagemService.countNaoLidasByChamado(idChamado, empresa()));
	}

	@PutMapping("/lida/{id}")
	public ResponseEntity<String> updateLida(@PathVariable("id") Long id,
			@RequestParam(defaultValue = "true") Boolean lida) {
		mensagemService.updateLida(id, lida, empresa());
		return ResponseEntity.ok("Status de leitura atualizado com sucesso.");
	}

	// ====================Atendente====================

	@GetMapping("/atendente/{idAtendente}")
	public ResponseEntity<List<Mensagem>> findAllByAtendente(@PathVariable Long idAtendente) {
		return ResponseEntity.ok(mensagemService.findAllByAtendente(idAtendente, empresa()));
	}

	@GetMapping("/atendente/{idAtendente}/total")
	public ResponseEntity<Long> countByAtendente(@PathVariable Long idAtendente) {
		return ResponseEntity.ok(mensagemService.countByAtendente(idAtendente, empresa()));
	}

	@GetMapping("/atendente/{idAtendente}/naoLidas")
	public ResponseEntity<List<Mensagem>> findAllNaoLidasByAtendente(@PathVariable Long idAtendente) {
		return ResponseEntity.ok(mensagemService.findAllNaoLidasByAtendente(idAtendente, empresa()));
	}

	// ====================Cliente====================

	@GetMapping("/cliente/{idCliente}")
	public ResponseEntity<List<Mensagem>> findAllByCliente(@PathVariable Long idCliente) {
		return ResponseEntity.ok(mensagemService.findAllByCliente(idCliente, empresa()));
	}

	@GetMapping("/cliente/{idCliente}/total")
	public ResponseEntity<Long> countByCliente(@PathVariable Long idCliente) {
		return ResponseEntity.ok(mensagemService.countByCliente(idCliente, empresa()));
	}

	@GetMapping("/cliente/{idCliente}/naoLidas")
	public ResponseEntity<List<Mensagem>> findAllNaoLidasByCliente(@PathVariable Long idCliente) {
		return ResponseEntity.ok(mensagemService.findAllNaoLidasByCliente(idCliente, empresa()));
	}

	// ====================Combinados====================

	@GetMapping("/chamado/{idChamado}/atendente/{idAtendente}")
	public ResponseEntity<List<Mensagem>> findAllByChamadoAndAtendente(@PathVariable Long idChamado,
			@PathVariable Long idAtendente) {
		return ResponseEntity.ok(mensagemService.findAllByChamadoAndAtendente(idChamado, idAtendente, empresa()));
	}

}
