package ar.com.thinksoft.business.generix.interfaces;

import java.io.Serializable;
import java.util.List;

import org.hibernate.StatelessSession;

import ar.com.thinksoft.exception.BusinessException;

public interface IntBusFacadeParentChild<P extends Serializable, C extends Serializable> {
	// Metodos del padre
	public List<P> selectEqual(P parent, StatelessSession s) throws BusinessException;

	public List<P> selectAll(StatelessSession s) throws BusinessException;

	public List<P> selectLike(P parent, StatelessSession s) throws BusinessException;

	public void insert(P parent, StatelessSession s) throws BusinessException;

	public void update(P parent, StatelessSession s) throws BusinessException;

	public void delete(P parent, StatelessSession s) throws BusinessException;

	public P selectEqualOne(P parent, StatelessSession s) throws BusinessException;

	public P selectLikeOne(P parent, StatelessSession s) throws BusinessException;

	// Metodos del hijo
	public List<C> selectChild(C child, StatelessSession s) throws BusinessException;
	
	//	Metodos del hijo
	public C selectOneChild(C child, StatelessSession s) throws BusinessException;

	public List<C> selectChildFromParent(P parent, StatelessSession s) throws BusinessException;

	public void deleteChild(C child, StatelessSession s) throws BusinessException;

	public void insertChild(C child, StatelessSession s) throws BusinessException;

	public void updateChild(C child, StatelessSession s) throws BusinessException;

	// Metodos de insercion conjunta
	public void insertParentAndChildren(P parent, List<C> children, StatelessSession s) throws BusinessException;

}
