package ar.com.thinksoft.business.generix.implementations;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

import ar.com.thinksoft.business.generix.interfaces.IntBusFacadeParentChildren;
import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.persistance.PersistanceFactory;
import ar.com.thinksoft.persistance.generix.interfaces.IntDomain;
import ar.com.thinksoft.utils.HandlerException;
import ar.com.thinksoft.utils.Setter;

public abstract class ImpBusFacadeParentChildren<P extends Serializable, C1 extends Serializable, C2 extends Serializable>
		implements IntBusFacadeParentChildren<P, C1, C2> {

	protected Class<P> claseP;

	protected Class<C1> claseC1;

	protected Class<C2> claseC2;

	@SuppressWarnings("unchecked")
	public ImpBusFacadeParentChildren() {
		this.claseP = (Class<P>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		this.claseC1 = (Class<C1>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1];
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<P> selectEqual(P parent, StatelessSession s) throws BusinessException {
		List l = null;
		try {
			l = PersistanceFactory.getIntDomain()
					.selectEqual(parent, s, getClass());
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		} finally {
			try {
				s.close();
			} catch (Exception e) {
			}
		}
		return l;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<P> selectAll(StatelessSession s) throws BusinessException {
		List l = null;
		try {
			l = PersistanceFactory.getIntDomain().selectAll(
					claseP.newInstance(), s, getClass());
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		} finally {
			try {
				s.close();
			} catch (Exception e) {
			}
		}
		return l;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<P> selectLike(P parent, StatelessSession s) throws BusinessException {
		List l = null;
		try {
			l = PersistanceFactory.getIntDomain().selectLike(parent, s, getClass());
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		} finally {
			try {
				s.close();
			} catch (Exception e) {
			}
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
			try {
				s.close();
			} catch (Exception e) {
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
			try {
				s.close();
			} catch (Exception e) {
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
			try {
				s.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public P selectEqualOne(P parent, StatelessSession s) throws BusinessException {
		Object o = null;
		try {
			List l = PersistanceFactory.getIntDomain().selectEqual(parent, s,
					getClass());
			if (l != null && l.size() > 0)
				o = l.get(0);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		} finally {
			try {
				s.close();
			} catch (Exception e) {
			}
		}
		return (P) o;
	}

	@Override
	@SuppressWarnings("unchecked")
	public P selectLikeOne(P serializable, StatelessSession s) throws BusinessException {
		Object o = null;
		try {
			List l = PersistanceFactory.getIntDomain().selectLike(serializable,
					s, getClass());
			if (l != null && l.size() > 0)
				o = l.get(0);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		} finally {
			try {
				s.close();
			} catch (Exception e) {
			}
		}
		return (P) o;
	}

	// ************************************************************************//
	// *******************************HIJO_1***********************************//
	// ************************************************************************//
	@Override
	@SuppressWarnings("unchecked")
	public List<C1> selectChild1(C1 child, StatelessSession s) throws BusinessException {
		List l = null;
		try {
			l = PersistanceFactory.getIntDomain().selectEqual(child, s, getClass());
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		} finally {
			try {
				s.close();
			} catch (Exception e) {
			}
		}
		return l;
	}

	@Override
	@SuppressWarnings("unchecked")
	public C1 selectOneChild1(C1 child, StatelessSession s) throws BusinessException {
		Object o = null;
		try {
			List l = PersistanceFactory.getIntDomain().selectEqual(child, s,
					getClass());
			if (l != null && l.size() > 0)
				o = l.get(0);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		} finally {
			try {
				s.close();
			} catch (Exception e) {
			}
		}
		return (C1) o;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<C1> selectChild1FromParent(P parent, StatelessSession s)
			throws BusinessException {
		List l = null;
		if (parent != null) {
			try {
				C1 child = claseC1.newInstance();
				l = (Setter.setChild(parent, child)) ? PersistanceFactory
						.getIntDomain().selectEqual(child, s, getClass()) : null;
			} catch (Exception e) {
				HandlerException.getInstancia().treateException(e, getClass());
			} finally {
				try {
					s.close();
				} catch (Exception e) {
				}
			}
		}
		return l;
	}

	@Override
	public void deleteChild1(C1 child, StatelessSession s) throws BusinessException {
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
			try {
				s.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void insertChild1(C1 child, StatelessSession s) throws BusinessException {
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
			try {
				s.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void updateChild1(C1 child, StatelessSession s) throws BusinessException {
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
			try {
				s.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void insertParentAndChild1(P parent, List<C1> children, StatelessSession s)
			throws BusinessException {
		Transaction t = null;
		try {
			t = s.beginTransaction();
			IntDomain domain = PersistanceFactory.getIntDomain();
			domain.insert(parent, s, getClass());
			for (C1 child : children)
				domain.insert(child, s, getClass());
			t.commit();
		} catch (Exception e) {
			if (t != null && t.isActive())
				t.rollback();
			HandlerException.getInstancia().treateException(e, getClass());
		} finally {
			try {
				s.close();
			} catch (Exception e) {
			}
		}
	}

	// ************************************************************************//
	// *******************************HIJO_2***********************************//
	// ************************************************************************//
	@Override
	@SuppressWarnings("unchecked")
	public List<C2> selectChild2(C2 child, StatelessSession s) throws BusinessException {
		List l = null;
		try {
			l = PersistanceFactory.getIntDomain().selectEqual(child, s, getClass());
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		} finally {
			try {
				s.close();
			} catch (Exception e) {
			}
		}
		return l;
	}

	@Override
	@SuppressWarnings("unchecked")
	public C2 selectOneChild2(C2 child, StatelessSession s) throws BusinessException {
		Object o = null;
		try {
			List l = PersistanceFactory.getIntDomain().selectEqual(child, s,
					getClass());
			if (l != null && l.size() > 0)
				o = l.get(0);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, getClass());
		} finally {
			try {
				s.close();
			} catch (Exception e) {
			}
		}
		return (C2) o;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<C2> selectChild2FromParent(P parent, StatelessSession s)
			throws BusinessException {
		List l = null;
		if (parent != null) {
			try {
				C2 child = claseC2.newInstance();
				l = (Setter.setChild(parent, child)) ? PersistanceFactory
						.getIntDomain().selectEqual(child, s, getClass()) : null;
			} catch (Exception e) {
				HandlerException.getInstancia().treateException(e, getClass());
			} finally {
				try {
					s.close();
				} catch (Exception e) {
				}
			}
		}
		return l;
	}

	@Override
	public void deleteChild2(C2 child, StatelessSession s) throws BusinessException {
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
			try {
				s.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void insertChild2(C2 child, StatelessSession s) throws BusinessException {
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
			try {
				s.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void updateChild2(C2 child, StatelessSession s) throws BusinessException {
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
			try {
				s.close();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void insertParentAndChild2(P parent, List<C2> children, StatelessSession s)
			throws BusinessException {
		Transaction t = null;
		try {
			t = s.beginTransaction();
			IntDomain domain = PersistanceFactory.getIntDomain();
			domain.insert(parent, s, getClass());
			for (C2 child : children)
				domain.insert(child, s, getClass());
			t.commit();
		} catch (Exception e) {
			if (t != null && t.isActive())
				t.rollback();
			HandlerException.getInstancia().treateException(e, getClass());
		} finally {
			try {
				s.close();
			} catch (Exception e) {
			}
		}
	}
}