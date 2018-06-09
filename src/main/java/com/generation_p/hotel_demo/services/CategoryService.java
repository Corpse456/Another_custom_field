package com.generation_p.hotel_demo.services;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.generation_p.hotel_demo.entity.HotelCategory;

@Repository
public class CategoryService {

	@PersistenceContext(unitName = "hotel_demo", type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;

	private static final Logger LOGGER = Logger.getLogger(CategoryService.class.getName());

	public CategoryService() {}

	public List<HotelCategory> findAll() {
		return entityManager.createQuery("select c from HotelCategory c", HotelCategory.class).getResultList();
	}

	public List<HotelCategory> findAll(String filter, int start, int maxResults) {
		return entityManager.createQuery("select c from HotelCategory c", HotelCategory.class).setFirstResult(start)
				.setMaxResults(maxResults).getResultList();
	}

	@Transactional
	public HotelCategory findById(Long id) {
		return entityManager.find(HotelCategory.class, id);
	}

	public long count() {
		return findAll().size();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(HotelCategory category) {
		HotelCategory rem = entityManager.find(HotelCategory.class, category.getId());
		entityManager.remove(rem);
		entityManager.flush();
	}

	@Transactional
	public void save(HotelCategory entry) {
		if (entry == null) {
			LOGGER.log(Level.SEVERE, "Category is null.");
			return;
		}
		HotelCategory category = entityManager.merge(entry);
		entityManager.persist(category);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void fillTestData() {
		if (findAll().isEmpty()) {
			final String[] data = new String[] { "Hotel", "Hostel", "Villa", "Appartments" };

			for (String category : data) {
				HotelCategory cat = new HotelCategory();
				cat.setCategoryName(category);
				save(cat);
			}
			entityManager.flush();
		}
	}

}