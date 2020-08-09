package ar.com.thinksoft.delegators;

import java.util.List;

import org.hibernate.StatelessSession;

import ar.com.thinksoft.business.BusinessFactory;
import ar.com.thinksoft.dtos.entornos.Aplicacion;
import ar.com.thinksoft.dtos.entornos.AplicacionCliente;
import ar.com.thinksoft.dtos.entornos.Cliente;
import ar.com.thinksoft.dtos.entornos.Contenedor;
import ar.com.thinksoft.dtos.entornos.DependenciaAplicacion;
import ar.com.thinksoft.dtos.entornos.EntornoCliente;
import ar.com.thinksoft.dtos.entornos.Servidor;
import ar.com.thinksoft.exception.BusinessException;

public class Entornos {

	// *************************************************************************************** //
	// Aplicacion
	// *************************************************************************************** //
	public static List<Aplicacion> selectAplicacion(StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusAplicacion().selectAll(session);
	}

	public static List<Aplicacion> selectAplicacionEqual(Aplicacion dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusAplicacion().selectEqual(dto, session);
	}

	public static Aplicacion selectAplicacionEqualOne(Aplicacion dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusAplicacion().selectEqualOne(dto, session);
	}

	public static List<Aplicacion> selectAplicacionLike(Aplicacion dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusAplicacion().selectLike(dto, session);
	}

	public static Aplicacion selectAplicacionLikeOne(Aplicacion dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusAplicacion().selectLikeOne(dto, session);
	}

	public static void insertAplicacion(Aplicacion dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusAplicacion().insert(dto, session);
	}

	public static void updateAplicacion(Aplicacion dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusAplicacion().update(dto, session);
	}

	public static void deleteAplicacion(Aplicacion dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusAplicacion().delete(dto, session);
	}

	// *************************************************************************************** //
	// AplicacionCliente
	// *************************************************************************************** //
	public static List<AplicacionCliente> selectAplicacionCliente(StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusAplicacionCliente().selectAll(session);
	}

	public static List<AplicacionCliente> selectAplicacionClienteEqual(AplicacionCliente dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusAplicacionCliente().selectEqual(dto, session);
	}

	public static AplicacionCliente selectAplicacionClienteEqualOne(AplicacionCliente dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusAplicacionCliente().selectEqualOne(dto, session);
	}

	public static List<AplicacionCliente> selectAplicacionClienteLike(AplicacionCliente dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusAplicacionCliente().selectLike(dto, session);
	}

	public static AplicacionCliente selectAplicacionClienteLikeOne(AplicacionCliente dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusAplicacionCliente().selectLikeOne(dto, session);
	}

	public static void insertAplicacionCliente(AplicacionCliente dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusAplicacionCliente().insert(dto, session);
	}

	public static void updateAplicacionCliente(AplicacionCliente dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusAplicacionCliente().update(dto, session);
	}

	public static void deleteAplicacionCliente(AplicacionCliente dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusAplicacionCliente().delete(dto, session);
	}

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
	// Contenedor
	// *************************************************************************************** //
	public static List<Contenedor> selectContenedor(StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusContenedor().selectAll(session);
	}

	public static List<Contenedor> selectContenedorEqual(Contenedor dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusContenedor().selectEqual(dto, session);
	}

	public static Contenedor selectContenedorEqualOne(Contenedor dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusContenedor().selectEqualOne(dto, session);
	}

	public static List<Contenedor> selectContenedorLike(Contenedor dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusContenedor().selectLike(dto, session);
	}

	public static Contenedor selectContenedorLikeOne(Contenedor dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusContenedor().selectLikeOne(dto, session);
	}

	public static void insertContenedor(Contenedor dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusContenedor().insert(dto, session);
	}

	public static void updateContenedor(Contenedor dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusContenedor().update(dto, session);
	}

	public static void deleteContenedor(Contenedor dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusContenedor().delete(dto, session);
	}

	// *************************************************************************************** //
	// DependenciaAplicacion
	// *************************************************************************************** //
	public static List<DependenciaAplicacion> selectDependenciaAplicacion(StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusDependenciaAplicacion().selectAll(session);
	}

	public static List<DependenciaAplicacion> selectDependenciaAplicacionEqual(DependenciaAplicacion dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusDependenciaAplicacion().selectEqual(dto, session);
	}

	public static DependenciaAplicacion selectDependenciaAplicacionEqualOne(DependenciaAplicacion dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusDependenciaAplicacion().selectEqualOne(dto, session);
	}

	public static List<DependenciaAplicacion> selectDependenciaAplicacionLike(DependenciaAplicacion dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusDependenciaAplicacion().selectLike(dto, session);
	}

	public static DependenciaAplicacion selectDependenciaAplicacionLikeOne(DependenciaAplicacion dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusDependenciaAplicacion().selectLikeOne(dto, session);
	}

	public static void insertDependenciaAplicacion(DependenciaAplicacion dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusDependenciaAplicacion().insert(dto, session);
	}

	public static void updateDependenciaAplicacion(DependenciaAplicacion dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusDependenciaAplicacion().update(dto, session);
	}

	public static void deleteDependenciaAplicacion(DependenciaAplicacion dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusDependenciaAplicacion().delete(dto, session);
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

	// *************************************************************************************** //
	// Servidor
	// *************************************************************************************** //
	public static List<Servidor> selectServidor(StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusServidor().selectAll(session);
	}

	public static List<Servidor> selectServidorEqual(Servidor dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusServidor().selectEqual(dto, session);
	}

	public static Servidor selectServidorEqualOne(Servidor dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusServidor().selectEqualOne(dto, session);
	}

	public static List<Servidor> selectServidorLike(Servidor dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusServidor().selectLike(dto, session);
	}

	public static Servidor selectServidorLikeOne(Servidor dto, StatelessSession session) throws BusinessException {
		return BusinessFactory.getIntBusServidor().selectLikeOne(dto, session);
	}

	public static void insertServidor(Servidor dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusServidor().insert(dto, session);
	}

	public static void updateServidor(Servidor dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusServidor().update(dto, session);
	}

	public static void deleteServidor(Servidor dto, StatelessSession session) throws BusinessException {
		BusinessFactory.getIntBusServidor().delete(dto, session);
	}

}
