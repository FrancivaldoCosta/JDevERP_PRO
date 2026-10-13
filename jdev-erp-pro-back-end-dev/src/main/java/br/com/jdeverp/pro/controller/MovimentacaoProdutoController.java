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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.jdeverp.pro.model.MovimentacaoProduto;
import br.com.jdeverp.pro.service.MovimentacaoProdutoService;
import br.com.jdeverp.pro.service.UsuarioLogadoService;

@RestController
@RequestMapping("/api/movimentacaoProduto")
public class MovimentacaoProdutoController {

	@Autowired
	private MovimentacaoProdutoService movimentacaoProdutoService;

	@Autowired
	private UsuarioLogadoService usuarioLogadoService;

	@GetMapping("/listar")
	public ResponseEntity<List<MovimentacaoProduto>> listar() {
		return ResponseEntity.ok(movimentacaoProdutoService.listar(usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/findAll")
	public ResponseEntity<List<MovimentacaoProduto>> findAll() {
		return ResponseEntity.ok(movimentacaoProdutoService.findAll(usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/buscarPorId/{id}")
	public ResponseEntity<MovimentacaoProduto> buscarPorId(@PathVariable("id") Long id) {
		return movimentacaoProdutoService.buscarPorId(id, usuarioLogadoService.getEmpresaIdLogada())
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/buscarPorIds")
	public ResponseEntity<List<MovimentacaoProduto>> buscarPorIds(@RequestBody List<Long> ids) {
		return ResponseEntity.ok(movimentacaoProdutoService.buscarPorIds(ids, usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/buscaPorNome/{nome}")
	public ResponseEntity<List<MovimentacaoProduto>> buscaPorNome(@PathVariable String nome) {
		return ResponseEntity.ok(movimentacaoProdutoService.buscaPorNome(nome, usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/existePorNome/{nome}")
	public ResponseEntity<Boolean> existePorNome(@PathVariable String nome) {
		return ResponseEntity.ok(movimentacaoProdutoService.existePorNome(nome, usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/existePorNomeDiferenteId/{id}/{nome}")
	public ResponseEntity<Boolean> existePorNomeDiferenteId(@PathVariable("id") Long id,
			@PathVariable String nome) {
		return ResponseEntity.ok(movimentacaoProdutoService.existePorNomeDiferenteId(id, nome,
				usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/existsById/{id}")
	public ResponseEntity<Boolean> existsById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(movimentacaoProdutoService.existsById(id, usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/total")
	public ResponseEntity<Long> total() {
		return ResponseEntity.ok(movimentacaoProdutoService.total(usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/paginado")
	public ResponseEntity<Page<MovimentacaoProduto>> listarPaginado(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sort,
			@RequestParam(defaultValue = "asc") String direction) {

		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

		return ResponseEntity.ok(movimentacaoProdutoService.listarPaginado(usuarioLogadoService.getEmpresaIdLogada(), pageable));
	}

	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<String> deletePorId(@PathVariable("id") Long id) {
		movimentacaoProdutoService.deleteById(id, usuarioLogadoService.getEmpresaIdLogada());
		return ResponseEntity.ok("Movimentação de produto deletada com sucesso.");
	}

	@PostMapping("/deletarPorIds")
	public ResponseEntity<String> deletarAllById(@RequestBody List<Long> ids) {
		movimentacaoProdutoService.deletarAllById(ids, usuarioLogadoService.getEmpresaIdLogada());
		return ResponseEntity.ok("Movimentações deletadas com sucesso.");
	}

	@DeleteMapping("/deletarTodos")
	public ResponseEntity<Long> deleteAll() {
		return ResponseEntity.ok(movimentacaoProdutoService.deleteAll(usuarioLogadoService.getEmpresaIdLogada()));
	}

	// ====================Métodos específicos para Pedido====================

	@GetMapping("/pedido/{idPedido}")
	public ResponseEntity<List<MovimentacaoProduto>> findAllByPedido(@PathVariable Long idPedido) {
		return ResponseEntity.ok(movimentacaoProdutoService.findAllByPedido(idPedido,
				usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/pedido/{idPedido}/buscaPorNome/{nome}")
	public ResponseEntity<List<MovimentacaoProduto>> buscaPorNomeByPedido(@PathVariable Long idPedido,
			@PathVariable String nome) {
		return ResponseEntity.ok(movimentacaoProdutoService.buscaPorNomeByPedido(nome, idPedido,
				usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/pedido/{idPedido}/existePorNome/{nome}")
	public ResponseEntity<Boolean> existePorNomeByPedido(@PathVariable Long idPedido, @PathVariable String nome) {
		return ResponseEntity.ok(movimentacaoProdutoService.existePorNomeByPedido(nome, idPedido,
				usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/pedido/{idPedido}/existePorNomeDiferenteId/{id}/{nome}")
	public ResponseEntity<Boolean> existePorNomeDiferenteIdByPedido(@PathVariable Long idPedido,
			@PathVariable("id") Long id, @PathVariable String nome) {
		return ResponseEntity.ok(movimentacaoProdutoService.existePorNomeDiferenteIdByPedido(id, nome, idPedido,
				usuarioLogadoService.getEmpresaIdLogada()));
	}

	@DeleteMapping("/pedido/{idPedido}/deletar/{id}")
	public ResponseEntity<String> deleteByIdAndPedido(@PathVariable Long idPedido, @PathVariable("id") Long id) {
		movimentacaoProdutoService.deleteByIdAndPedido(id, idPedido, usuarioLogadoService.getEmpresaIdLogada());
		return ResponseEntity.ok("Movimentação de produto do pedido deletada com sucesso.");
	}

}
