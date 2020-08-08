package ar.com.thinksoft.business;

import ar.com.thinksoft.business.entornos.implementations.ImpBusCliente;
import ar.com.thinksoft.business.entornos.implementations.ImpBusEntornoCliente;
import ar.com.thinksoft.business.entornos.interfaces.IntBusCliente;
import ar.com.thinksoft.business.entornos.interfaces.IntBusEntornoCliente;
import ar.com.thinksoft.business.seguridad.implementations.ImpBusUsuario;
import ar.com.thinksoft.business.seguridad.interfaces.IntBusUsuario;

public class BusinessFactory {

	private static IntBusUsuario intBusUsuario;
	public static IntBusUsuario getIntBusUsuario() {
		return (intBusUsuario == null) ? intBusUsuario = new ImpBusUsuario() : intBusUsuario;
	}

	private static IntBusCliente intBusCliente;
	public static IntBusCliente getIntBusCliente() {
		return (intBusCliente == null) ? intBusCliente = new ImpBusCliente() : intBusCliente;
	}

	private static IntBusEntornoCliente intBusEntornoCliente;
	public static IntBusEntornoCliente getIntBusEntornoCliente() {
		return (intBusEntornoCliente == null) ? intBusEntornoCliente = new ImpBusEntornoCliente() : intBusEntornoCliente;
	}

}
