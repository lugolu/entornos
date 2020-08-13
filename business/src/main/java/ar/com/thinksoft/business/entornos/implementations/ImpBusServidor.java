package ar.com.thinksoft.business.entornos.implementations;

import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

import ar.com.thinksoft.business.entornos.interfaces.IntBusServidor;
import ar.com.thinksoft.business.generix.implementations.ImpBusFacadeParent;
import ar.com.thinksoft.dtos.entornos.Servidor;
import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.jdbc.HibernateSessionFactory;
import ar.com.thinksoft.persistance.PersistanceFactory;
import ar.com.thinksoft.utils.HandlerException;

public class ImpBusServidor extends ImpBusFacadeParent<Servidor> implements IntBusServidor {

	@Override
	public void insert(Servidor serializable, StatelessSession s) throws BusinessException {
		Transaction t = null;
		try {
			t = s.beginTransaction();
			serializable.setIdServidor(HibernateSessionFactory.getInstance().selectNextId(serializable, "idServidor", s));
			PersistanceFactory.getIntDomain().insert(serializable,s,getClass());
			t.commit();
		} catch (Exception e) {
			if (t != null && t.isActive()) {
				t.rollback();
			}
			HandlerException.getInstancia().treateException(e,getClass());
		} finally {
			try {s.close();} catch (Exception e) {}
		}
	}

}
