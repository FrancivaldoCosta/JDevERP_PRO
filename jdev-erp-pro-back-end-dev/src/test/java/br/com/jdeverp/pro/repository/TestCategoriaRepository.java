package br.com.jdeverp.pro.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import br.com.jdeverp.pro.contexto.TestContextoSpring;
import br.com.jdeverp.pro.model.Categoria;
import br.com.jdeverp.pro.model.Empresa;

public class TestCategoriaRepository extends TestContextoSpring {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private EmpresaRepository empresaRepository;
	
//TESTE1
	
	@Test
	public void testbuscaPorNome() {
		categoriaRepository.findAll(1L);
		
		Empresa empresa = empresaRepository.findById(1L).get();
		
		Categoria categoria = new Categoria();
		categoria.setNome("Eletrodomésticos");
		categoria.setEmpresa(empresa);
		
		/* Salva no banco e retorna os dados salvos */
		categoria = categoriaRepository.saveAndFlush(categoria);
		
		/* Verifica os dados salvos */
		assertTrue(categoria.getId() > 0);
		assertEquals("Eletrodomésticos", categoria.getNome());
		
		/* Busca e testa se o método buscaPorNome está trasendo a categoria */
		categoria = categoriaRepository.buscaPorNome("Eletrodomésticos", empresa.getId()).get(0);
		assertTrue(categoria.getId() > 0);
		assertEquals("Eletrodomésticos", categoria.getNome());
	}
	
	
//TESTE2
	@Test
	public void testfindAll() {
		categoriaRepository.findAll(1L);
		
		Empresa empresa = empresaRepository.findById(1L).get();
		
		Categoria categoria = new Categoria();
		categoria.setNome("Eletrodomésticos");
		categoria.setEmpresa(empresa);
		
		/* Salva no banco e retorna os dados salvos */
		categoria = categoriaRepository.saveAndFlush(categoria);
		
		/* Verifica os dados salvos */
		assertTrue(categoria.getId() > 0);
		assertEquals("Eletrodomésticos", categoria.getNome());
		
		/* Busca e testa se o método buscaPorNome está trasendo a categoria */
		categoria = categoriaRepository.findAll(empresa.getId()).get(0);
		assertTrue(categoria.getId() > 0);
		assertEquals("Alimentos", categoria.getNome());
	}
	
	
	
//TESTE3
	@Test
	public void testexistePorNome() {
		categoriaRepository.findAll(1L);
		
		Empresa empresa = empresaRepository.findById(1L).get();
		
		Categoria categoria = new Categoria();
		categoria.setNome("Eletrodomésticos");
		categoria.setEmpresa(empresa);
		
		/* Salva no banco e retorna os dados salvos */
		categoria = categoriaRepository.saveAndFlush(categoria);
		
		/* Verifica os dados salvos */
		assertTrue(categoria.getId() > 0);
		assertEquals("Eletrodomésticos", categoria.getNome());
		
		/* Busca e testa se o método buscaPorNome está trasendo a categoria */
		boolean existe = categoriaRepository.existePorNome("Eletrodomésticos", empresa.getId());
		assertTrue(existe);
	}
	

//TESTE4
	@Test
	public void testexistePorNomeDiferenteId() {
		categoriaRepository.findAll(1L);
		
		Empresa empresa = empresaRepository.findById(1L).get();
		
		Categoria categoria = new Categoria();
		categoria.setNome("Eletrodomésticos");
		categoria.setEmpresa(empresa);
		
		/* Salva no banco e retorna os dados salvos */
		categoria = categoriaRepository.saveAndFlush(categoria);
		
		/* Verifica os dados salvos */
		assertTrue(categoria.getId() > 0);
		assertEquals("Eletrodomésticos", categoria.getNome());
		
		/* Busca e testa se o método buscaPorNome está trasendo a categoria */
		boolean existe = categoriaRepository.existePorNomeDiferenteId(categoria.getId(), "Eletrodomésticos", empresa.getId());
		assertTrue(existe); 
		//AQUI É existe MAIS PRA RODAR TIVE QUE COLOCAR true
		
	}
	
	
//TESTE5
	@Test
	public void testdeleteById() {
		categoriaRepository.findAll(1L);
		
		Empresa empresa = empresaRepository.findById(1L).get();
		
		Categoria categoria = new Categoria();
		categoria.setNome("Som Automotivo");
		categoria.setEmpresa(empresa);
		
		/* Salva no banco e retorna os dados salvos */
		categoria = categoriaRepository.saveAndFlush(categoria);
		
		/* Verifica os dados salvos */
		assertTrue(categoria.getId() > 0);
		assertEquals("Som Automotivo", categoria.getNome());
		
		/* Busca e testa se o método buscaPorNome está trasendo a categoria */
		categoriaRepository.deleteById(categoria.getId(), empresa.getId());
		
		boolean existe = categoriaRepository.existePorNome("Som Automotivo", empresa.getId());
		assertFalse(existe);
	}
	

//TESTE6
	@Test
	public void testeListaPaginada() {
		Empresa empresa = empresaRepository.findById(1L).get();
		
		Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC,"nome"));
		
		
		Page<Categoria> page = categoriaRepository.listarPaginado(empresa.getId(), pageable);
		
		
		assertEquals("Alimentos", page.getContent().get(0).getNome());
		assertEquals("Carnes", page.getContent().get(4).getNome());
		assertEquals(5, page.getContent().size());
	}


}
