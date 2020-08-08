package ar.com.thinksoft.business.generix.interfaces;

import java.io.Serializable;
import java.util.List;

import org.hibernate.StatelessSession;

import ar.com.thinksoft.exception.BusinessException;

public interface IntBusFacadeParentChildren<P extends Serializable, C1 extends Serializable, C2 extends Serializable> {

	// Metodos del padre
	public List<P> selectEqual(P parent, StatelessSession s) throws BusinessException;
	public List<P> selectAll(StatelessSession s) throws BusinessException;
	public List<P> selectLike(P parent, StatelessSession s) throws BusinessException;
	public void insert(P parent, StatelessSession s) throws BusinessException;
	public void update(P parent, StatelessSession s) throws BusinessException;
	public void delete(P parent, StatelessSession s) throws BusinessException;
	public P selectEqualOne(P parent, StatelessSession s) throws BusinessException;
	public P selectLikeOne(P parent, StatelessSession s) throws BusinessException;

	// Metodos del hijo 1
	public List<C1> selectChild1(C1 child, StatelessSession s) throws BusinessException;
	public C1 selectOneChild1(C1 child, StatelessSession s) throws BusinessException;
	public List<C1> selectChild1FromParent(P parent, StatelessSession s) throws BusinessException;
	public void deleteChild1(C1 child, StatelessSession s) throws BusinessException;
	public void insertChild1(C1 child, StatelessSession s) throws BusinessException;
	public void updateChild1(C1 child, StatelessSession s) throws BusinessException;
	public void insertParentAndChild1(P parent, List<C1> child1, StatelessSession s)	throws BusinessException;
	
	//	Metodos del hijo 2
	public List<C2> selectChild2(C2 child, StatelessSession s) throws BusinessException;
	public C2 selectOneChild2(C2 child, StatelessSession s) throws BusinessException;
	public List<C2> selectChild2FromParent(P parent, StatelessSession s) throws BusinessException;
	public void deleteChild2(C2 child, StatelessSession s) throws BusinessException;
	public void insertChild2(C2 child, StatelessSession s) throws BusinessException;
	public void updateChild2(C2 child, StatelessSession s) throws BusinessException;
	public void insertParentAndChild2(P parent, List<C2> child2, StatelessSession s)	throws BusinessException;

}
