package ar.com.thinksoft.persistance.generix.interfaces;

import java.io.Serializable;
import java.util.List;

import org.hibernate.StatelessSession;

import ar.com.thinksoft.exception.BusinessException;

public interface IntDomain {

	public List selectEqual(Serializable serializable,StatelessSession s, Class clase) throws BusinessException;

	public List selectAll(Serializable serializable,StatelessSession s, Class clase) throws BusinessException;

	public List selectLike(Serializable serializable,StatelessSession s, Class clase) throws BusinessException;

	public void insert(Serializable serializable,StatelessSession s, Class clase) throws BusinessException;

	public void update(Serializable serializable,StatelessSession s, Class clase) throws BusinessException;

	public void delete(Serializable serializable,StatelessSession s, Class clase) throws BusinessException;

}