package ar.com.thinksoft.business.seguridad.interfaces;

import org.hibernate.StatelessSession;

import ar.com.thinksoft.dtos.seguridad.Usuario;
import ar.com.thinksoft.exception.BusinessException;

public interface IntBusUsuario {

	public Usuario loginUsuario(Usuario usuario, StatelessSession s) throws BusinessException;

}
