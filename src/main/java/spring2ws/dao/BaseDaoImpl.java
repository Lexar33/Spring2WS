package spring2ws.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class BaseDaoImpl<K, T> implements BaseDao<K, T> {

    final Logger logger = LoggerFactory.getLogger(BaseDaoImpl.class);

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findByNamedQuery(String namedQueryName, Map<String, Object> parameters) throws IllegalStateException {
        Query q = getSession().createNamedQuery(namedQueryName);
        for (Map.Entry<String, Object> paramName : parameters.entrySet()) {
            q.setParameter(paramName.getKey(), paramName.getValue());
        }
        return q.getResultList();
    }

    private Session getSession() { return sessionFactory.getCurrentSession(); }


}
