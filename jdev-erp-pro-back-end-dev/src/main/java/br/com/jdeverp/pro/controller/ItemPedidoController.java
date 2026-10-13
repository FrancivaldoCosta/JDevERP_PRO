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

import br.com.jdeverp.pro.model.ItemPedido;
import br.com.jdeverp.pro.service.ItemPedidoService;
import br.com.jdeverp.pro.service.UsuarioLogadoService;

@RestController
@RequestMapping("/api/itemPedido")
public class ItemPedidoController {

	@Autowired
	private ItemPedidoService itemPedidoService;

	@Autowired
	private UsuarioLogadoService usuarioLogadoService;

	@GetMapping("/listar")
	public ResponseEntity<List<ItemPedido>> listar() {
		return ResponseEntity.ok(itemPedidoService.listar(usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/findAll/{idPedido}")
	public ResponseEntity<List<ItemPedido>> findAll(@PathVariable Long idPedido) {
		return ResponseEntity.ok(itemPedidoService.findAll(idPedido, usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/buscarPorId/{id}")
	public ResponseEntity<ItemPedido> buscarPorId(@PathVariable("id") Long id) {
		return itemPedidoService.buscarPorId(id, usuarioLogadoService.getEmpresaIdLogada())
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/buscarPorIds")
	public ResponseEntity<List<ItemPedido>> buscarPorIds(@RequestBody List<Long> ids) {
		return ResponseEntity.ok(itemPedidoService.buscarPorIds(ids, usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/buscaPorNome/{idPedido}/{nome}")
	public ResponseEntity<List<ItemPedido>> buscaPorNome(@PathVariable Long idPedido, @PathVariable String nome) {
		return ResponseEntity.ok(itemPedidoService.buscaPorNome(nome, idPedido, usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/existePorNome/{idPedido}/{nome}")
	public ResponseEntity<Boolean> existePorNome(@PathVariable Long idPedido, @PathVariable String nome) {
		return ResponseEntity.ok(itemPedidoService.existePorNome(nome, idPedido, usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/existePorNomeDiferenteId/{id}/{idPedido}/{nome}")
	public ResponseEntity<Boolean> existePorNomeDiferenteId(@PathVariable("id") Long id,
			@PathVariable Long idPedido, @PathVariable String nome) {
		return ResponseEntity.ok(itemPedidoService.existePorNomeDiferenteId(id, nome, idPedido,
				usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/existsById/{id}")
	public ResponseEntity<Boolean> existsById(@PathVariable("id") Long id) {
		return ResponseEntity.ok(itemPedidoService.existsById(id, usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/total")
	public ResponseEntity<Long> total() {
		return ResponseEntity.ok(itemPedidoService.total(usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/paginado")
	public ResponseEntity<Page<ItemPedido>> listarPaginado(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String sort,
			@RequestParam(defaultValue = "asc") String direction) {

		Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

		return ResponseEntity.ok(itemPedidoService.listarPaginado(usuarioLogadoService.getEmpresaIdLogada(), pageable));
	}

	@DeleteMapping("/deletar/{idPedido}/{id}")
	public ResponseEntity<String> deletePorId(@PathVariable Long idPedido, @PathVariable("id") Long id) {
		itemPedidoService.deleteById(id, idPedido, usuarioLogadoService.getEmpresaIdLogada());
		return ResponseEntity.ok("Item do pedido deletado com sucesso.");
	}

	@PostMapping("/deletarPorIds")
	public ResponseEntity<String> deletarAllById(@RequestBody List<Long> ids) {
		itemPedidoService.deletarAllById(ids, usuarioLogadoService.getEmpresaIdLogada());
		return ResponseEntity.ok("Itens deletados com sucesso.");
	}

	@DeleteMapping("/deletarTodos")
	public ResponseEntity<Long> deleteAll() {
		return ResponseEntity.ok(itemPedidoService.deleteAll(usuarioLogadoService.getEmpresaIdLogada()));
	}

	// ====================Métodos específicos para Pedido====================

	@GetMapping("/pedido/{idPedido}")
	public ResponseEntity<List<ItemPedido>> findAllByPedido(@PathVariable Long idPedido) {
		return ResponseEntity.ok(itemPedidoService.findAllByPedido(idPedido, usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/pedido/{idPedido}/buscaPorNome/{nome}")
	public ResponseEntity<List<ItemPedido>> buscaPorNomePorPedido(@PathVariable Long idPedido,
			@PathVariable String nome) {
		return ResponseEntity.ok(itemPedidoService.buscaPorNomePorPedido(nome, idPedido,
				usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/pedido/{idPedido}/existePorNome/{nome}")
	public ResponseEntity<Boolean> existePorNomePorPedido(@PathVariable Long idPedido, @PathVariable String nome) {
		return ResponseEntity.ok(itemPedidoService.existePorNomePorPedido(nome, idPedido,
				usuarioLogadoService.getEmpresaIdLogada()));
	}

	@GetMapping("/pedido/{idPedido}/existePorNomeDiferenteId/{id}/{nome}")
	public ResponseEntity<Boolean> existePorNomeDiferenteIdPorPedido(@PathVariable Long idPedido,
			@PathVariable("id") Long id, @PathVariable String nome) {
		return ResponseEntity.ok(itemPedidoService.existePorNomeDiferenteIdPorPedido(id, nome, idPedido,
				usuarioLogadoService.getEmpresaIdLogada()));
	}

	@DeleteMapping("/pedido/{idPedido}/deletar/{id}")
	public ResponseEntity<String> deleteByIdAndPedido(@PathVariable Long idPedido, @PathVariable("id") Long id) {
		itemPedidoService.deleteByIdAndPedido(id, idPedido, usuarioLogadoService.getEmpresaIdLogada());
		return ResponseEntity.ok("Item do pedido deletado com sucesso.");
	}

}
