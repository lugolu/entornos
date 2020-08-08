package ar.com.thinksoft.business.seguridad.implementations;

import org.hibernate.StatelessSession;

import ar.com.thinksoft.business.seguridad.interfaces.IntBusUsuario;
import ar.com.thinksoft.dtos.seguridad.Usuario;
import ar.com.thinksoft.exception.BusinessException;

public class ImpBusUsuario implements IntBusUsuario {

	@Override
	public Usuario loginUsuario(Usuario usuario, StatelessSession s) throws BusinessException {
		//TODO: loginUsuario
		Usuario ret = new Usuario();
		ret.setFaullName("usuario");

		try {
			s.close();
		} catch (Exception e) {
		}

		return ret;
	}

}
