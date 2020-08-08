package ar.com.thinksoft.persistance.generix.interfaces;

import java.io.Serializable;
import java.util.List;

import org.hibernate.StatelessSession;

import ar.com.thinksoft.exception.BusinessException;

public interface IntDomainExtended {

	public List selectLikeIgnoreCase (Serializable dto, StatelessSession session, Class clase) throws BusinessException;
	public List selectLikeOrderedBy (Serializable dto, String columna, boolean ascendente,  StatelessSession session, Class clase) throws BusinessException;
	public List selectLikeOrderedBy (Serializable dto, String[] columnasAsc, String[] columnasDesc,  StatelessSession session, Class clase) throws BusinessException;
	public List selectEqualOrderedBy (Serializable dto, String columna, boolean ascendente,  StatelessSession session, Class clase) throws BusinessException;
	public Serializable selectEqualOne (Serializable dto, StatelessSession session, Class clase) throws BusinessException;
	public List selectEqualIgnoreCase(Serializable serializable, StatelessSession s, Class clase) throws BusinessException;
	public List selectLike (Serializable dto, String[] columnasOr, String[] columnasAnd, StatelessSession session, Class clase) throws BusinessException;
	public void deleteAll(Serializable serializable, StatelessSession s, Class clase) throws BusinessException;

}
