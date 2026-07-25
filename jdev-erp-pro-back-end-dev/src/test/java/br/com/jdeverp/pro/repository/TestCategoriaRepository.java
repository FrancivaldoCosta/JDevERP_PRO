package br.com.jdeverp.pro.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.jdeverp.pro.contexto.TestContextoSpring;
import br.com.jdeverp.pro.model.Categoria;
import br.com.jdeverp.pro.model.Empresa;

public class TestCategoriaRepository extends TestContextoSpring {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private EmpresaRepository empresaRepository;
	
//Correção1
	
	@Test
	public void testbuscaPorNome() {
		categoriaRepository.findAll(1L);
		
		Empresa empresa = empresaRepository.findById(1L).get();
		
		Categoria categoria = new Categoria();
		categoria.setNome("Eletrodoméstico");
		categoria.setEmpresa(empresa);
		
		/* Salva no banco e retorna os dados salvos */
		categoria = categoriaRepository.saveAndFlush(categoria);
		
		/* Verifica os dados salvos */
		assertTrue(categoria.getId() > 0);
		assertEquals("Eletrodoméstico", categoria.getNome());
		
		/* Busca e testa se o método buscaPorNome está trasendo a categoria */
		categoria = categoriaRepository.buscaPorNome("Eletrodoméstico", empresa.getId()).get(0);
		assertTrue(categoria.getId() > 0);
		assertEquals("Eletrodoméstico", categoria.getNome());
	}
	
	
	//Correção2
	@Test
	public void testfindAll() {
		categoriaRepository.findAll(1L);
		
		Empresa empresa = empresaRepository.findById(1L).get();
		
		Categoria categoria = new Categoria();
		categoria.setNome("Eletrodoméstico");
		categoria.setEmpresa(empresa);
		
		/* Salva no banco e retorna os dados salvos */
		categoria = categoriaRepository.saveAndFlush(categoria);
		
		/* Verifica os dados salvos */
		assertTrue(categoria.getId() > 0);
		assertEquals("Eletrodoméstico", categoria.getNome());
		
		/* Busca e testa se o método buscaPorNome está trasendo a categoria */
		categoria = categoriaRepository.findAll(empresa.getId()).get(0);
		assertTrue(categoria.getId() > 0);
		assertEquals("Eletrodoméstico", categoria.getNome());
	}
	
	
	
	//Correção3
	@Test
	public void testexistePorNome() {
		categoriaRepository.findAll(1L);
		
		Empresa empresa = empresaRepository.findById(1L).get();
		
		Categoria categoria = new Categoria();
		categoria.setNome("Eletrodoméstico");
		categoria.setEmpresa(empresa);
		
		/* Salva no banco e retorna os dados salvos */
		categoria = categoriaRepository.saveAndFlush(categoria);
		
		/* Verifica os dados salvos */
		assertTrue(categoria.getId() > 0);
		assertEquals("Eletrodoméstico", categoria.getNome());
		
		/* Busca e testa se o método buscaPorNome está trasendo a categoria */
		boolean existe = categoriaRepository.existePorNome("Eletrodoméstico", empresa.getId());
		assertTrue(existe);
	}
	
	//Correção4
	@Test
	public void testexistePorNomeDiferenteId() {
		categoriaRepository.findAll(1L);
		
		Empresa empresa = empresaRepository.findById(1L).get();
		
		Categoria categoria = new Categoria();
		categoria.setNome("Eletrodoméstico");
		categoria.setEmpresa(empresa);
		
		/* Salva no banco e retorna os dados salvos */
		categoria = categoriaRepository.saveAndFlush(categoria);
		
		/* Verifica os dados salvos */
		assertTrue(categoria.getId() > 0);
		assertEquals("Eletrodoméstico", categoria.getNome());
		
		/* Busca e testa se o método buscaPorNome está trasendo a categoria */
		boolean existe = categoriaRepository.existePorNomeDiferenteId(categoria.getId(), "Eletrodoméstico", empresa.getId());
		assertTrue(existe);
	}
	
	
	//Correção5
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
	

//TESTE1	
//	@Test
//	public void testbuscaPorNome() {
//
//		Empresa empresa = empresaRepository.findById(1L).get();
//
//		Categoria categoria = new Categoria();
//		categoria.setNome("Eletrodoméstico");
//		categoria.setEmpresa(empresa);
//
//		/* Salva no banco e retorna os dados salvos */
//		categoria = categoriaRepository.saveAndFlush(categoria);
//
//		/* Verifica os dados salvos */
//		assertTrue(categoria.getId() > 0);
//		assertEquals("Eletrodoméstico", categoria.getNome());
//
//		/* Busca e testa se o método buscaPorNome está trasendo a categoria */
//		categoria = categoriaRepository.buscaPorNome("Eletrodoméstico", empresa.getId()).get(0);
//		assertTrue(categoria.getId() > 0);
//		assertEquals("Eletrodoméstico", categoria.getNome());
//
//	}

//TESTE2	

//	@Test
//	public void testfindAll() {
//
//		Empresa empresa = empresaRepository.findById(1L).get();
//
//		Categoria categoria = new Categoria();
//		categoria.setNome("Eletrodoméstico");
//		categoria.setEmpresa(empresa);
//
//		/* Salva no banco e retorna os dados salvos */
//		categoria = categoriaRepository.saveAndFlush(categoria);
//
//		/* Verifica os dados salvos */
//		assertTrue(categoria.getId() > 0);
//		assertEquals("Eletrodoméstico", categoria.getNome());
//
//		/* Busca e testa se o método buscaPorNome está trasendo a categoria */
//		categoria = categoriaRepository.findAll(empresa.getId()).get(0);
//		assertTrue(categoria.getId() > 0);
//		assertEquals("Eletrodoméstico", categoria.getNome());
//
//	}

//TESTE3	

//	@Test
//	public void testexistePorNome() {
//
//		Empresa empresa = empresaRepository.findById(1L).get();
//
//		Categoria categoria = new Categoria();
//		categoria.setNome("Eletrodoméstico");
//		categoria.setEmpresa(empresa);
//
//		/* Salva no banco e retorna os dados salvos */
//		categoria = categoriaRepository.saveAndFlush(categoria);
//
//		/* Verifica os dados salvos */
//		assertTrue(categoria.getId() > 0);
//		assertEquals("Eletrodoméstico", categoria.getNome());
//
//		/* Busca e testa se o método buscaPorNome está trasendo a categoria */
//		boolean existe = categoriaRepository.existePorNome("Eletrodoméstico", empresa.getId());
//		assertTrue(existe);
//
//	}

//TESTE4	



//TESTE5	

//	@Test
//	public void testdeleteById() {
//
//		Empresa empresa = empresaRepository.findById(1L).get();
//
//		Categoria categoria = new Categoria();
//		categoria.setNome("Som Automotivo");
//		categoria.setEmpresa(empresa);
//
//		/* Salva no banco e retorna os dados salvos */
//		categoria = categoriaRepository.saveAndFlush(categoria);
//
//		/* Verifica os dados salvos */
//		assertTrue(categoria.getId() > 0);
//		assertEquals("Som Automotivo", categoria.getNome());
//
//		/* Busca e testa se o método buscaPorNome está trasendo a categoria */
//		categoriaRepository.deleteById(categoria.getId(), empresa.getId());
//
//		boolean existe = categoriaRepository.existePorNome("Som Automotivo", empresa.getId());
//		assertFalse(existe);
//
//	}
	



}
