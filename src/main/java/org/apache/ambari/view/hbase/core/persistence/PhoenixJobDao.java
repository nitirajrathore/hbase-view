package org.apache.ambari.view.hbase.core.persistence;

import org.apache.ambari.view.hbase.jobs.PhoenixJob;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@Component
public class PhoenixJobDao {

	@PersistenceContext
	private EntityManager em;

	public void persist(PhoenixJob product) {
		em.persist(product);
	}

	public List<PhoenixJob> findAll() {
		return em.createQuery("SELECT p FROM PhoenixJob p").getResultList();
	}

}