package br.com.jdeverp.pro.validator;

import java.lang.ProcessHandle.Info;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.core.support.RepositoryFactoryInformation;
import org.springframework.stereotype.Component;

@Component
public class RepositoryEmpresaValidator implements SmartInitializingSingleton{
	
	
	private final ApplicationContext applicationContext;
	
	public RepositoryEmpresaValidator(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	
	
	private List<Class<?>> getRepositories() {
		List<Class<?>> repositores = new ArrayList<Class<?>>();
		applicationContext.getBeansOfType(RepositoryFactoryInformation.class)
						.values().forEach(Info -> repositores.add(Info.getRepositoryInformation().getRepositoryInterface()));
		
		return repositores;
		
//		PAREI_AQUI
		
	}
	
	

	@Override
	public void afterSingletonsInstantiated() {

	
		
	}
	
	

}
