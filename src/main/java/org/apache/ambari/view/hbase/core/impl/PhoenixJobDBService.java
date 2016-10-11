package org.apache.ambari.view.hbase.core.impl;

import org.apache.ambari.view.hbase.core.persistence.FilteringStrategy;
import org.apache.ambari.view.hbase.core.persistence.IResourceManager;
import org.apache.ambari.view.hbase.core.persistence.ItemNotFound;
import org.apache.ambari.view.hbase.core.persistence.PhoenixJobDao;
import org.apache.ambari.view.hbase.jobs.PhoenixJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class PhoenixJobDBService implements IResourceManager<PhoenixJob> {

	@Autowired
	private PhoenixJobDao productDao;

//	@Transactional
//	public void add(PhoenixJob product) {
//
//	}
//
//	public void addAll(Collection<PhoenixJob> products) {
//		for (PhoenixJob product : products) {
//			productDao.persist(product);
//		}
//	}

	@Transactional(readOnly = true)
	public List<PhoenixJob> readAll(FilteringStrategy filteringStrategy) {
		return productDao.findAll();
	}

	@Override
	@Transactional
	public PhoenixJob create(PhoenixJob object) {
		productDao.persist(object);
		return object;
	}

	@Override
	public PhoenixJob read(Object id) throws ItemNotFound {
		return null;
	}

	@Override
	public PhoenixJob update(PhoenixJob newObject, String id) throws ItemNotFound {
		return null;
	}

	@Override
	public void delete(Object resourceId) throws ItemNotFound {

	}
}