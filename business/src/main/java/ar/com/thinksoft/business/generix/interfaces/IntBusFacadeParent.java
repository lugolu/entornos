package ar.com.thinksoft.business.generix.interfaces;

import java.io.Serializable;
import java.util.List;

import org.hibernate.StatelessSession;

import ar.com.thinksoft.exception.BusinessException;

public interface IntBusFacadeParent<T extends Serializable> {

	public List<T> selectEqual(T serializable,StatelessSession s) throws BusinessException;

	public List<T> selectAll(StatelessSession s) throws BusinessException;

	public List<T> selectLike(T serializable,StatelessSession s) throws BusinessException;

	public void insert(T serializable,StatelessSession s) throws BusinessException;

	public void update(T serializable,StatelessSession s) throws BusinessException;

	public void delete(T serializable,StatelessSession s) throws BusinessException;

	public T selectEqualOne(T serializable,StatelessSession s) throws BusinessException;

	public T selectLikeOne(T serializable,StatelessSession s) throws BusinessException;

	public T selectEqualOneIgnoreCase(T serializable,StatelessSession s) throws BusinessException;
}

