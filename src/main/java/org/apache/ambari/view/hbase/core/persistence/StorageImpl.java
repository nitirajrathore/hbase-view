package org.apache.ambari.view.hbase.core.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class StorageImpl implements Storage {

  private final EntityManagerFactory entityManagerFactory;

  public StorageImpl(EntityManagerFactory entityManagerFactory) {
    this.entityManagerFactory = entityManagerFactory;
  }

  @Override
  public <T extends PersistentResource> T create(Class<? extends T> klass, T obj) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(obj);
    entityManager.getTransaction().commit();
    return obj;
  }

  @Override
  public <T extends PersistentResource> T update(Class<? extends T> klass, T obj) {

    EntityManager entityManager = getEntityManager();
    T object = entityManager.find(klass, obj.getId());
    object.override(obj);
    entityManager.getTransaction().begin();
    entityManager.persist(object);
    entityManager.getTransaction().commit();
    return object;
  }

  @Override
  public <T extends PersistentResource> T read(Class<? extends T> klass, Object id) throws ItemNotFoundException {
    return null;
  }

  @Override
  public <T extends PersistentResource> List<T> readAll(Class<? extends T> klass, FilteringStrategy filter) throws PersistenceException {
    String query = getSelectStatement(klass, (null == filter) ? null : filter.whereStatement());
    EntityManager entityManager = getEntityManager();
    Query q = entityManager.createQuery(query);
    List resultSet = q.getResultList();
    return new ArrayList<>(resultSet);
  }

  @Override
  public <T extends PersistentResource> List<T> readAll(Class<? extends T> klass) throws PersistenceException {
    return this.readAll(klass, null);
  }

  @Override
  public <T extends PersistentResource> void delete(Class<? extends T> klass, Object id) throws ItemNotFoundException, PersistenceException {
    Object obj = read(klass, id);
    getEntityManager().remove(obj);
  }

  @Override
  public <T extends PersistentResource> boolean exists(Class<? extends T> klass, Object id) throws PersistenceException {
    return this.read(klass, id) != null;
  }

  <T> String getSelectStatement(Class<? extends T> clazz, String whereClause) throws PersistenceException {
    StringBuilder stringBuilder = new StringBuilder();
    String entityName = clazz.getName();
    try {

      stringBuilder.append("SELECT e FROM ").append(entityName).append(" e");
      if (whereClause != null) {
        stringBuilder.append(" WHERE ");

        Set<String> propertyNames = getPropertyNames(clazz);
        StringTokenizer tokenizer = new StringTokenizer(whereClause, " \t\n\r\f+-*/=><()\"", true);
        boolean quoted = false;

        while (tokenizer.hasMoreElements()) {
          String token = tokenizer.nextToken();

          quoted = quoted ^ token.equals("\"");

          if (propertyNames.contains(token) && !quoted) {
            stringBuilder.append(" e.").append(token);
          } else {
            stringBuilder.append(" e.").append(token);
          }
        }
      }
    } catch (IntrospectionException e) {
      throw new PersistenceException("Exception occurred while creating select statement.", e);
    }

    return stringBuilder.toString();
  }

  // get the property names for the given view entity class
  Set<String> getPropertyNames(Class clazz) throws IntrospectionException {
    Set<String> propertyNames = new HashSet<String>();
    for (PropertyDescriptor pd : Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {
      propertyNames.add(pd.getName());
    }
    return propertyNames;
  }


  private EntityManager getEntityManager() {
    return entityManagerFactory.createEntityManager();
  }
}