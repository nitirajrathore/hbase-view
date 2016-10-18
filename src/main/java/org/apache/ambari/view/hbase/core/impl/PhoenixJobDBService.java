package org.apache.ambari.view.hbase.core.impl;

import org.apache.ambari.view.hbase.core.persistence.FilteringStrategy;
import org.apache.ambari.view.hbase.core.persistence.IResourceManager;
import org.apache.ambari.view.hbase.core.persistence.ItemNotFoundException;
import org.apache.ambari.view.hbase.core.persistence.PhoenixJobDao;
import org.apache.ambari.view.hbase.jobs.PhoenixJob;

import java.util.List;

public class PhoenixJobDBService implements IResourceManager<PhoenixJob> {

	private PhoenixJobDao phoenixJobDao = new PhoenixJobDao();

	public List<PhoenixJob> readAll(FilteringStrategy filteringStrategy) {
		return phoenixJobDao.findAll();
	}

	@Override
	public PhoenixJob create(PhoenixJob object) {
		phoenixJobDao.persist(object);
		return object;
	}

	@Override
	public PhoenixJob read(Object id) throws ItemNotFoundException {
		return phoenixJobDao.find(id);
	}

	@Override
	public PhoenixJob update(PhoenixJob newObject, String id) throws ItemNotFoundException {
		return phoenixJobDao.update(newObject, id);
	}

	@Override
	public void delete(Object resourceId) throws ItemNotFoundException {

	}
}