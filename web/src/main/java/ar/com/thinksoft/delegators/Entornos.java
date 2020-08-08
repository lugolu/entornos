package ar.com.thinksoft.delegators;

import java.util.List;

import org.hibernate.StatelessSession;

import ar.com.thinksoft.business.BusinessFactory;
import ar.com.thinksoft.dtos.entornos.Cliente;
import ar.com.thinksoft.dtos.entornos.EntornoCliente;
import ar.com.thinksoft.exception.BusinessException;

public class Entornos {

	// *************************************************************************************** //
	// Cliente
	// *************************************************************************************** //
	public static List<Cliente> selectCliente(StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusCliente().selectAll(session);
	}

	public static List<Cliente> selectClienteEqual(Cliente dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusCliente().selectEqual(dto, session);
	}

	public static Cliente selectClienteEqualOne(Cliente dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusCliente().selectEqualOne(dto, session);
	}

	public static List<Cliente> selectClienteLike(Cliente dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusCliente().selectLike(dto, session);
	}

	public static Cliente selectClienteLikeOne(Cliente dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusCliente().selectLikeOne(dto, session);
	}

	public static void insertCliente(Cliente dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusCliente().insert(dto, session);
	}

	public static void updateCliente(Cliente dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusCliente().update(dto, session);
	}

	public static void deleteCliente(Cliente dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusCliente().delete(dto, session);
	}

	// *************************************************************************************** //
	// EntornoCliente
	// *************************************************************************************** //
	public static List<EntornoCliente> selectEntornoCliente(StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusEntornoCliente().selectAll(session);
	}

	public static List<EntornoCliente> selectEntornoClienteEqual(EntornoCliente dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusEntornoCliente().selectEqual(dto, session);
	}

	public static EntornoCliente selectEntornoClienteEqualOne(EntornoCliente dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusEntornoCliente().selectEqualOne(dto, session);
	}

	public static List<EntornoCliente> selectEntornoClienteLike(EntornoCliente dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusEntornoCliente().selectLike(dto, session);
	}

	public static EntornoCliente selectEntornoClienteLikeOne(EntornoCliente dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusEntornoCliente().selectLikeOne(dto, session);
	}

	public static void insertEntornoCliente(EntornoCliente dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusEntornoCliente().insert(dto, session);
	}

	public static void updateEntornoCliente(EntornoCliente dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusEntornoCliente().update(dto, session);
	}

	public static void deleteEntornoCliente(EntornoCliente dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusEntornoCliente().delete(dto, session);
	}

}
