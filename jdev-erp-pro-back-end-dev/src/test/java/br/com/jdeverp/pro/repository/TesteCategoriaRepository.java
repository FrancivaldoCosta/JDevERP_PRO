package br.com.jdeverp.pro.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.jdeverp.pro.contexto.TestContextoSpring;
import br.com.jdeverp.pro.model.Categoria;
import br.com.jdeverp.pro.model.Empresa;

public class TesteCategoriaRepository extends TestContextoSpring {
	

	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	@Test
	public void testCadastro() {
		
		Empresa empresa = empresaRepository.findById(1L).get();
		
		Categoria categoria = new Categoria();
		categoria.setNome("Eletrodoméstico");
		categoria.setEmpresa(empresaRepository.findById(1L).get());
		
		
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

}
