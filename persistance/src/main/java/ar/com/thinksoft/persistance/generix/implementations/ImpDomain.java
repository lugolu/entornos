package ar.com.thinksoft.persistance.generix.implementations;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Restrictions;

import ar.com.thinksoft.exception.BusinessException;
import ar.com.thinksoft.persistance.generix.interfaces.IntDomain;
import ar.com.thinksoft.utils.HandlerException;
import ar.com.thinksoft.utils.SeverityBundle;

public class ImpDomain implements IntDomain {

	/**
	 * Genera una sentencia select que devuelve los resultados de la tabla que
	 * concuerdan con la clase del objeto Serializable que recibe por parametro.
	 *
	 * @param serializable
	 *            Objeto a buscar en la base de datos.
	 * @param s
	 *            Objeto de session de StandardBasicTypes.
	 * @return List con los resultados de la tabla que se consulta.
	 */
	@Override
	public List selectAll(Serializable serializable, StatelessSession s, Class clase) throws BusinessException {
		try {
			return (serializable != null) ? s.createCriteria(serializable.getClass()).list() : null;
		} catch (HibernateException e) {
			HandlerException.getInstancia().treateException(e,clase);
			return null;
		}
	}

	/**
	 * Genera una sentencia select que devuelve los resultados de la tabla que
	 * concuerdan con los campos seteados en el objeto Serializable que recibe
	 * por parametro. Utiliza = como comparador.
	 *
	 * @param serializable
	 *            Objeto a buscar en la base de datos.
	 * @param s
	 *            Objeto de session de StandardBasicTypes.
	 * @return List con los resultados de la tabla que se consulta.
	 */
	@Override
	public List selectEqual(Serializable serializable, StatelessSession s, Class clase) throws BusinessException {
		try {
			return (serializable != null) ? s.createCriteria(serializable.getClass()).add(
					Example.create(serializable)).list()
					:null;
		} catch (HibernateException e) {
			HandlerException.getInstancia().treateException(e,clase);
			return null;
		}
	}

	/**
	 * Genera una sentencia select que devuelve los resultados de la tabla que
	 * concuerdan con los campos seteados en el objeto Serializable que recibe
	 * por parametro. Utiliza like como comparador.
	 *
	 * @param serializable
	 *            Objeto a buscar en la base de datos.
	 * @param s
	 *            Objeto de session de StandardBasicTypes.
	 * @return List con los resultados de la tabla que se consulta.
	 */
	@Override
	public List selectLike(Serializable serializable, StatelessSession s, Class clase) throws BusinessException {
		try {
			Criteria c = s.createCriteria(serializable.getClass());
			c.add(Example.create(serializable).enableLike());
			return c.list();
		} catch (HibernateException e) {
			HandlerException.getInstancia().treateException(e,clase);
			return null;
		}
	}

	/**
	 * Inserta un objeto en la base de datos.
	 *
	 *
	 * @param serializable
	 *            Objeto a insertar en la base de datos.
	 * @param s
	 *            Objeto de session de StandardBasicTypes.
	 */
	@Override
	public void insert(Serializable serializable, StatelessSession s, Class clase) throws BusinessException {
		try {
			//-- Comentado porque se utiliza un secuenciador y no una tabla para secuenciar --//
			//Class clase = Class.forName(serializable.getClass().getName());
			//Method metodo = clase.getMethod("setIdObjeto",Long.class);
			//metodo.invoke(serializable,new Long(-1));
			s.insert(serializable);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e,clase);
		}
	}

	/**
	 * Actualiza un objeto en la base de datos.
	 *
	 * @param serializable
	 *            Objeto a actualizar en la base de datos.
	 * @param s
	 *            Objeto de session de StandardBasicTypes.
	 */
	@Override
	public void update(Serializable serializable, StatelessSession s, Class clase) throws BusinessException {
		try {
			s.update(serializable);
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e,clase);
		}
	}

	/**
	 * Elimina un objeto en la base de datos.
	 *
	 * @param serializable
	 *            Objeto a eliminar en la base de datos.
	 * @param s
	 *            Objeto de session de StandardBasicTypes.
	 */
	@Override
	public void delete(Serializable serializable, StatelessSession s, Class clase) throws BusinessException {
		try {
			s.delete(serializable);
			serializable = null;
		} catch (Exception e) {
			HandlerException.getInstancia().treateException(e,clase);
		}
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