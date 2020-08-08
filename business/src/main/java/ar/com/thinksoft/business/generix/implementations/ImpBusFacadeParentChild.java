package ar.com.thinksoft.business.generix.implementations;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

import ar.com.thinksoft.business.generix.interfaces.IntBusFacadeParentChild;
import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.persistance.PersistanceFactory;
import ar.com.thinksoft.persistance.generix.interfaces.IntDomain;
import ar.com.thinksoft.utils.HandlerException;
import ar.com.thinksoft.utils.Setter;

public abstract class ImpBusFacadeParentChild<P extends Serializable, C extends Serializable>
		implements IntBusFacadeParentChild<P, C> {

	protected Class<P> claseP;

	protected Class<C> claseC;

	@SuppressWarnings("unchecked")
	public ImpBusFacadeParentChild() {
		this.claseP = (Class<P>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		this.claseC = (Class<C>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1];
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<P> selectEqual(P parent, StatelessSession s) throws BusinessException {
		List l = null;
		try {
			l = PersistanceFactory.getIntDomain().selectEqual(parent,s,getClass());
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e,getClass());
		} finally {
			try {s.close();} catch (Exception e) {}
		}
		return l;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<P> selectAll(StatelessSession s) throws BusinessException {
		List l = null;
		try {
			l = PersistanceFactory.getIntDomain().selectAll(claseP.newInstance(),s,getClass());
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e,getClass());
		} finally {
			try {s.close();} catch (Exception e) {}
		}
		return l;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<P> selectLike(P parent, StatelessSession s) throws BusinessException {
		List l = null;
		try {
			l = PersistanceFactory.getIntDomain().selectLike(parent,s,getClass());
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e,getClass());
		} finally {
			try {s.close();} catch (Exception e) {}
		}
		return l;
	}

	@Override
	public void insert(P parent, StatelessSession s) throws BusinessException {
		Transaction t = null;
		try {
			t = s.beginTransaction();
			PersistanceFactory.getIntDomain().insert(parent, s, getClass());
			t.commit();
		} catch (Exception e) {
			if (t != null && t.isActive())
				t.rollback();
			HandlerException.getInstancia().treateException(e, getClass());
		} finally {
			try {s.close();} catch (Exception e) {
			}
		}
	}

	@Override
	public void update(P parent, StatelessSession s) throws BusinessException {
		Transaction t = null;
		try {
			t = s.beginTransaction();
			PersistanceFactory.getIntDomain().update(parent, s, getClass());
			t.commit();
		} catch (Exception e) {
			if (t != null && t.isActive())
				t.rollback();
			HandlerException.getInstancia().treateException(e, getClass());
		} finally {
			try {s.close();} catch (Exception e) {
			}
		}
	}

	@Override
	public void delete(P parent, StatelessSession s) throws BusinessException {
		Transaction t = null;
		try {
			t = s.beginTransaction();
			PersistanceFactory.getIntDomain().delete(parent, s, getClass());
			t.commit();
		} catch (Exception e) {
			if (t != null && t.isActive())
				t.rollback();
			HandlerException.getInstancia().treateException(e, getClass());
		} finally {
			try {s.close();} catch (Exception e) {
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public P selectEqualOne(P parent, StatelessSession s) throws BusinessException {
		Object o = null;
		try {
			List l = PersistanceFactory.getIntDomain().selectEqual(parent,s,getClass());
			if (l != null && l.size() > 0)
				o = l.get(0);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e,getClass());
		} finally {
			try {s.close();} catch (Exception e) {}
		}
		return (P)o;
	}

	@Override
	@SuppressWarnings("unchecked")
	public P selectLikeOne(P serializable, StatelessSession s) throws BusinessException {
		Object o = null;
		try {
			List l = PersistanceFactory.getIntDomain().selectLike(serializable,s,getClass()); 
			if (l != null && l.size() > 0)
				o = l.get(0);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e,getClass());
		} finally {
			try {s.close();} catch (Exception e) {}
		}
		return (P)o;
	}

	// ************************************************************************//
	// ********************************HIJO************************************//
	// ************************************************************************//
	@Override
	@SuppressWarnings("unchecked")
	public List<C> selectChild(C child, StatelessSession s) throws BusinessException {
		List l = null;
		try {
			l = PersistanceFactory.getIntDomain().selectEqual(child,s,getClass());
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e,getClass());
		} finally {
			try {s.close();} catch (Exception e) {}
		}
		return l;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public C selectOneChild(C child, StatelessSession s) throws BusinessException {
		Object o = null;
		try {
			List l = PersistanceFactory.getIntDomain().selectEqual(child,s,getClass());
			if (l != null && l.size() > 0)
				o = l.get(0);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e,getClass());
		} finally {
			try {s.close();} catch (Exception e) {}
		}
		return (C)o;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<C> selectChildFromParent(P parent, StatelessSession s)
			throws BusinessException {
		List l = null;
		if (parent != null) {
			try {
				C child = claseC.newInstance();
				l = (Setter.setChild(parent, child)) ? PersistanceFactory
						.getIntDomain().selectEqual(child, s, getClass()) : null;
			} catch (Exception e) {
				HandlerException.getInstancia().treateException(e, getClass());
			} finally {
				try {s.close();} catch (Exception e) {}
			}
		}
		return l;
	}

	@Override
	public void deleteChild(C child, StatelessSession s) throws BusinessException {
		Transaction t = null;
		try {
			t = s.beginTransaction();
			PersistanceFactory.getIntDomain().delete(child, s, getClass());
			t.commit();
		} catch (Exception e) {
			if (t != null && t.isActive())
				t.rollback();
			HandlerException.getInstancia().treateException(e, getClass());
		} finally {
			try {s.close();} catch (Exception e) {
			}
		}
	}

	@Override
	public void insertChild(C child, StatelessSession s) throws BusinessException {
		Transaction t = null;
		try {
			t = s.beginTransaction();
			PersistanceFactory.getIntDomain().insert(child, s, getClass());
			t.commit();
		} catch (Exception e) {
			if (t != null && t.isActive())
				t.rollback();
			HandlerException.getInstancia().treateException(e, getClass());
		} finally {
			try {s.close();} catch (Exception e) {
			}
		}
	}

	@Override
	public void updateChild(C child, StatelessSession s) throws BusinessException {
		Transaction t = null;
		try {
			t = s.beginTransaction();
			PersistanceFactory.getIntDomain().update(child, s, getClass());
			t.commit();
		} catch (Exception e) {
			if (t != null && t.isActive())
				t.rollback();
			HandlerException.getInstancia().treateException(e, getClass());
		} finally {
			try {s.close();} catch (Exception e) {}
		}
	}

	@Override
	public void insertParentAndChildren(P parent, List<C> children, StatelessSession s)
			throws BusinessException {
		Transaction t = null;
		try {
			t = s.beginTransaction();
			IntDomain domain = PersistanceFactory.getIntDomain();
			domain.insert(parent, s, getClass());
			for (C child : children)
				domain.insert(child, s, getClass());
			t.commit();
		} catch (Exception e) {
			if (t != null && t.isActive())
				t.rollback();
			HandlerException.getInstancia().treateException(e, getClass());
		} finally {
			try {s.close();} catch (Exception e) {}
		}
	}

}