package ar.com.thinksoft.business.entornos.interfaces;

import org.hibernate.StatelessSession;

import ar.com.thinksoft.business.generix.interfaces.IntBusFacadeParent;
import ar.com.thinksoft.dtos.entornos.EntornoCliente;
import ar.com.thinksoft.exception.BusinessException;

public interface IntBusEntornoCliente extends IntBusFacadeParent<EntornoCliente> {

	@Override
	public void insert(EntornoCliente serializable, StatelessSession s) throws BusinessException;

}
