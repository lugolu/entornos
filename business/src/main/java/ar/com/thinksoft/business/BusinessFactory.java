package ar.com.thinksoft.business;

import ar.com.thinksoft.business.entornos.implementations.ImpBusAplicacion;
import ar.com.thinksoft.business.entornos.implementations.ImpBusAplicacionCliente;
import ar.com.thinksoft.business.entornos.implementations.ImpBusCliente;
import ar.com.thinksoft.business.entornos.implementations.ImpBusContenedor;
import ar.com.thinksoft.business.entornos.implementations.ImpBusDependenciaAplicacion;
import ar.com.thinksoft.business.entornos.implementations.ImpBusEntornoCliente;
import ar.com.thinksoft.business.entornos.implementations.ImpBusServidor;
import ar.com.thinksoft.business.entornos.interfaces.IntBusAplicacion;
import ar.com.thinksoft.business.entornos.interfaces.IntBusAplicacionCliente;
import ar.com.thinksoft.business.entornos.interfaces.IntBusCliente;
import ar.com.thinksoft.business.entornos.interfaces.IntBusContenedor;
import ar.com.thinksoft.business.entornos.interfaces.IntBusDependenciaAplicacion;
import ar.com.thinksoft.business.entornos.interfaces.IntBusEntornoCliente;
import ar.com.thinksoft.business.entornos.interfaces.IntBusServidor;
import ar.com.thinksoft.business.seguridad.implementations.ImpBusUsuario;
import ar.com.thinksoft.business.seguridad.interfaces.IntBusUsuario;

public class BusinessFactory {

	private static IntBusAplicacion intBusAplicacion;
	public static IntBusAplicacion getIntBusAplicacion() {
		return (intBusAplicacion == null) ? intBusAplicacion = new ImpBusAplicacion() : intBusAplicacion;
	}

	private static IntBusAplicacionCliente intBusAplicacionCliente;
	public static IntBusAplicacionCliente getIntBusAplicacionCliente() {
		return (intBusAplicacionCliente == null) ? intBusAplicacionCliente = new ImpBusAplicacionCliente() : intBusAplicacionCliente;
	}

	private static IntBusCliente intBusCliente;
	public static IntBusCliente getIntBusCliente() {
		return (intBusCliente == null) ? intBusCliente = new ImpBusCliente() : intBusCliente;
	}

	private static IntBusContenedor intBusContenedor;
	public static IntBusContenedor getIntBusContenedor() {
		return (intBusContenedor == null) ? intBusContenedor = new ImpBusContenedor() : intBusContenedor;
	}

	private static IntBusDependenciaAplicacion intBusDependenciaAplicacion;
	public static IntBusDependenciaAplicacion getIntBusDependenciaAplicacion() {
		return (intBusDependenciaAplicacion == null) ? intBusDependenciaAplicacion = new ImpBusDependenciaAplicacion() : intBusDependenciaAplicacion;
	}

	private static IntBusEntornoCliente intBusEntornoCliente;
	public static IntBusEntornoCliente getIntBusEntornoCliente() {
		return (intBusEntornoCliente == null) ? intBusEntornoCliente = new ImpBusEntornoCliente() : intBusEntornoCliente;
	}

	private static IntBusServidor intBusServidor;
	public static IntBusServidor getIntBusServidor() {
		return (intBusServidor == null) ? intBusServidor = new ImpBusServidor() : intBusServidor;
	}

	private static IntBusUsuario intBusUsuario;
	public static IntBusUsuario getIntBusUsuario() {
		return (intBusUsuario == null) ? intBusUsuario = new ImpBusUsuario() : intBusUsuario;
	}

}
