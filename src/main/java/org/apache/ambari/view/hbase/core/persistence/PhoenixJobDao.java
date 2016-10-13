package org.apache.ambari.view.hbase.core.persistence;

import org.apache.ambari.view.hbase.jobs.PhoenixJob;
import org.apache.ambari.view.hbase.jobs.impl.PhoenixJobImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

public class PhoenixJobDao {

  private EntityManagerFactory emf;
  public PhoenixJobDao() {
    this.emf = DatabaseServiceFactory.getInstance().getEntityManagerFactory();
  }

  public void persist(PhoenixJob phoenixJob) {
    EntityManager entityManager = getEntityManager();
    entityManager.getTransaction().begin();
    entityManager.persist(phoenixJob);
    entityManager.getTransaction().commit();
  }

  public List<PhoenixJob> findAll() {
    return new ArrayList<>(getEntityManager().createQuery("SELECT p FROM PhoenixJobImpl p").getResultList()) ;
  }

  public PhoenixJob find(Object id) {
    return getEntityManager().find(PhoenixJobImpl.class, id);
  }

  public PhoenixJob update(PhoenixJob newObject, String id) {
    EntityManager entityManager = getEntityManager();
    PhoenixJobImpl object = entityManager.find(PhoenixJobImpl.class, id);
    object.override(newObject);
    entityManager.getTransaction().begin();
    entityManager.persist(object);
    entityManager.getTransaction().commit();
    return object;
  }

  public EntityManager getEntityManager() {
    return emf.createEntityManager();
  }
}