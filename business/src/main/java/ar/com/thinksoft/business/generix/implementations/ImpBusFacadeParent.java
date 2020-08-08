package ar.com.thinksoft.business.generix.implementations;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

import ar.com.thinksoft.business.generix.interfaces.IntBusFacadeParent;
import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.persistance.PersistanceFactory;
import ar.com.thinksoft.utils.HandlerException;

public abstract class ImpBusFacadeParent<S extends Serializable> implements IntBusFacadeParent<S> {

	protected Class<S> clase;

	@SuppressWarnings("unchecked")
	public ImpBusFacadeParent() {
		clase = (Class<S>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<S> selectEqual(S serializable, StatelessSession s) throws BusinessException {
		List l = null;
		try {
			l = PersistanceFactory.getIntDomain().selectEqual(serializable,s,getClass());
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e,getClass());
		} finally {
			try {s.close();} catch (Exception e) {}
		}
		return l;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<S> selectAll(StatelessSession s) throws BusinessException {
		List l = null;
		try {
			l = PersistanceFactory.getIntDomain().selectAll(clase.newInstance(),s,getClass());
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e,getClass());
		} finally {
			try {s.close();} catch (Exception e) {}
		}
		return l;
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public List<S> selectLike(S serializable, StatelessSession s) throws BusinessException {
		List l = null;
		try {
			l = PersistanceFactory.getIntDomain().selectLike(serializable,s,getClass());
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e,getClass());
		} finally {
			try {s.close();} catch (Exception e) {}
		}
		return l;
	}

	@Override
	public void insert(S serializable, StatelessSession s) throws BusinessException {
		Transaction t = null;
		try {
			t = s.beginTransaction();
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

	@Override
	public void update(S serializable, StatelessSession s) throws BusinessException {
		Transaction t = null;
		try {
			t = s.beginTransaction();
			PersistanceFactory.getIntDomain().update(serializable,s,getClass());
			t.commit();
		} catch (Exception e) {
			if (t != null && t.isActive()) {
				t.rollback();
			}
			HandlerException.getInstancia().treateException(e,getClass());
		} finally {
			try {
				s.close();
			} catch (Exception e) {}
		}
	}

	@Override
	public void delete(S serializable, StatelessSession s) throws BusinessException {
		Transaction t = null;
		try {
			t = s.beginTransaction();
			PersistanceFactory.getIntDomain().delete(serializable,s,getClass());
			serializable = null;
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

	@Override
	@SuppressWarnings("unchecked")
	public S selectEqualOne(S serializable,StatelessSession s) throws BusinessException{
		Object o = null;
		try {
			List l = PersistanceFactory.getIntDomain().selectEqual(serializable,s,getClass());
			if (l != null && l.size() > 0) {
				o = l.get(0);
			}
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e,getClass());
		} finally {
			try {s.close();} catch (Exception e) {}
		}
		return (S)o;
	}

	@Override
	@SuppressWarnings("unchecked")
	public S selectLikeOne(S serializable,StatelessSession s) throws BusinessException{
		Object o = null;
		try {
			List l = PersistanceFactory.getIntDomain().selectLike(serializable,s,getClass());
			if (l != null && l.size() > 0) {
				o = l.get(0);
			}
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e,getClass());
		} finally {
			try {s.close();} catch (Exception e) {}
		}
		return (S)o;
	}

	@Override
	@SuppressWarnings("unchecked")
	public S selectEqualOneIgnoreCase(S serializable,StatelessSession s) throws BusinessException{
		Object o = null;
		try {
			List l = PersistanceFactory.getIntDomainExtendeed().selectEqualIgnoreCase(serializable,s,getClass());
			if (l != null && l.size() > 0) {
				o = l.get(0);
			}
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e,getClass());
		} finally {
			try {s.close();} catch (Exception e) {}
		}
		return (S)o;
	}
}