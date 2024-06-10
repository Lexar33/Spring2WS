package spring2ws.dao;

import java.util.List;
import java.util.Map;

public interface BaseDao<K, T>  {

    List<T> findByNamedQuery(String namedQueryName, Map<String, Object> parameters);




}
