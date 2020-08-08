package ar.com.thinksoft.persistance.generix.implementations;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.persistance.generix.interfaces.IntDomainExtended;
import ar.com.thinksoft.utils.HandlerException;
import ar.com.thinksoft.utils.Reflection;
import ar.com.thinksoft.utils.SeverityBundle;

@SuppressWarnings("unchecked")
public class ImpDomainExtended implements IntDomainExtended {

	@Override
	public List selectLikeIgnoreCase (Serializable dto, StatelessSession session, Class clase) throws BusinessException {
		List<Serializable> ret = null;
		try {
			ret = session.createCriteria(dto.getClass())
					.add(Example.create(dto).enableLike().ignoreCase()).list();
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, clase);
		}
		return ret;
	}

	@Override
	public List selectLikeOrderedBy (Serializable dto, String columna, boolean ascendente,  StatelessSession session, Class clase) throws BusinessException {
		List<Serializable> ret = null;
		try {
			Criteria c = session.createCriteria(dto.getClass())
					.add(Example.create(dto).enableLike());
			if (ascendente) {
				c.addOrder(Order.asc(columna));
			} else {
				c.addOrder(Order.desc(columna));
			}
			ret = c.list();
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, clase);
		}
		return ret;
	}

	@Override
	public List selectLikeOrderedBy (Serializable dto, String[] columnasAsc, String[] columnasDesc,  StatelessSession session, Class clase) throws BusinessException {
		List<Serializable> ret = null;
		try {
			Criteria c = session.createCriteria(dto.getClass())
					.add(Example.create(dto).enableLike());
			for (String columna : columnasAsc) {
				c.addOrder(Order.asc(columna));
			}
			for (String columna : columnasDesc) {
				c.addOrder(Order.desc(columna));
			}
			ret = c.list();
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, clase);
		}
		return ret;
	}

	@Override
	public List selectEqualOrderedBy (Serializable dto, String columna, boolean ascendente,  StatelessSession session, Class clase) throws BusinessException {
		List<Serializable> ret = null;
		try {
			Criteria c = session.createCriteria(dto.getClass())
					.add(Example.create(dto));
			if (ascendente) {
				c.addOrder(Order.asc(columna));
			} else {
				c.addOrder(Order.desc(columna));
			}
			ret = c.list();
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, clase);
		}
		return ret;
	}

	@Override
	public Serializable selectEqualOne (Serializable dto, StatelessSession session, Class clase) throws BusinessException {
		Serializable retorno = null;
		try {
			retorno = (Serializable) session.createCriteria(dto.getClass())
					.add(Example.create(dto))
					.uniqueResult();
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, clase);
		}
		return retorno;
	}

	@Override
	public List selectEqualIgnoreCase(Serializable serializable, StatelessSession s, Class clase) throws BusinessException {
		try {
			return (serializable != null) ? s.createCriteria(serializable.getClass()).add(
					Example.create(serializable).ignoreCase()).list() : null;
		} catch (HibernateException e) {
			HandlerException.getInstancia().treateException(e,clase);
			return null;
		}
	}

	@Override
	public List selectLike (Serializable dto, String[] columnasOr, String[] columnasAnd, StatelessSession session, Class clase) throws BusinessException {
		List<Serializable> ret = null;
		try {
			Criteria c = session.createCriteria(dto.getClass());
			if (columnasAnd != null && columnasAnd.length > 0) {
				for (String columna : columnasAnd) {
					c.add(Restrictions.like(columna, Reflection.getInstancia().invokeMethod(dto, columna)));
				}
			}
			if (columnasOr != null && columnasOr.length > 0) {
				Criterion criterionsOr = null;
				for (String columna : columnasOr) {
					if (criterionsOr == null) {
						criterionsOr = Restrictions.like(columna, Reflection.getInstancia().invokeMethod(dto, columna));
					} else {
						criterionsOr = Restrictions.or(criterionsOr, Restrictions.like(columna, Reflection.getInstancia().invokeMethod(dto, columna)));
					}
				}
				c.add(criterionsOr);
			}
			ret = c.list();
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e, clase);
		}
		return ret;
	}

	@Override
	public void deleteAll(Serializable serializable, StatelessSession s, Class clase) throws BusinessException {
		try {
			List<Serializable> list = (serializable != null) ? s.createCriteria(serializable.getClass()).list() : new ArrayList<>(0);
			for (Serializable dto : list) {
				s.delete(dto);
				dto = null;
			}
		} catch (HibernateException e) {
			HandlerException.getInstancia().treateException(e,clase);
		}
	}

	private String getTableNameForClass (String clase) {
		StringBuffer table = null;

		char[] chars = clase.toCharArray();

		for (int i=0; i<chars.length; i++) {
			if (table == null) {
				table = new StringBuffer("");
			}
			else {
				if (chars[i] >= 65 && chars[i] <= 90) {
					table.append("_");
				}
			}
			table.append(chars[i]);
		}

		return table.toString().toUpperCase();
	}

	private void checkIds(Criteria c, Serializable serializable, String method) throws Exception {
		for(Field f : serializable.getClass().getDeclaredFields()) {
			Transient transientAnnotation = f.getAnnotation(Transient.class);
			if(transientAnnotation != null) {
				continue;
			}
			Id idAnnotation = f.getAnnotation(Id.class);
			if(idAnnotation != null) {
				f.setAccessible(true);
				Object value = f.get(serializable);
				if(value != null) {
					if("eq".equals(method)) {
						c.add(Restrictions.eq(f.getName(), value));
					} else if ("like".equals(method)) {
						c.add(Restrictions.like(f.getName(), value));
					} else {
						throw new BusinessException("No mwthod", SeverityBundle.FATAL);
					}
				}
			}
			EmbeddedId embeddedIdAnnotation = f.getAnnotation(EmbeddedId.class);
			if(embeddedIdAnnotation != null) {
				f.setAccessible(true);
				Object idClass = f.get(serializable);
				if(idClass != null) {
					for(Field id : idClass.getClass().getDeclaredFields()) {
						Transient transientAnnotationAux = id.getAnnotation(Transient.class);
						if(transientAnnotationAux != null) {
							continue;
						}
						id.setAccessible(true);
						Object value = id.get(idClass);
						if(value != null) {
							if("eq".equals(method)) {
								c.add(Restrictions.eq(f.getName() + "." + id.getName(), value));
							} else if ("like".equals(method)) {
								c.add(Restrictions.like(f.getName() + "." + id.getName(), value));
							} else {
								throw new BusinessException("No mwthod", SeverityBundle.FATAL);
							}
						}
					}
				}
			}
		}
	}
}