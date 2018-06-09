package com.generation_p.hotel_demo.services.memory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.generation_p.hotel_demo.entity.HotelCategory;

public class CategoryServiceMemory {

	private static CategoryServiceMemory instance;

	private static final Logger LOGGER = Logger.getLogger(CategoryServiceMemory.class.getName());

	private static Comparator<HotelCategory> comparator = new Comparator<HotelCategory>() {
		@Override
		public int compare(HotelCategory o1, HotelCategory o2) {
			if (o1.getId() < o2.getId())
				return 1;
			if (o1.getId() > o2.getId())
				return -1;
			return 0;
		}
	};

	private final Map<Long, HotelCategory> categories = new ConcurrentHashMap<>();

	private long nextId = 0;

	private CategoryServiceMemory() {}

	public static synchronized CategoryServiceMemory getInstance() {
		if (instance == null) {
			instance = new CategoryServiceMemory();
			instance.fillTestData();
		}
		return instance;
	}

	public List<HotelCategory> findAll() {
		List<HotelCategory> result = new ArrayList<>(categories.size());
		try {
			for (HotelCategory c : categories.values()) {
				result.add(c.clone());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		result.sort(comparator);
		return result;
	}

	public List<HotelCategory> findAll(CategoryServiceMemory.Filter filter) {
		List<HotelCategory> result = new ArrayList<>();
		try {
			for (HotelCategory c : categories.values()) {
				if (filter.matches(c))
					result.add(c.clone());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		result.sort(comparator);
		return result;
	}

	public List<HotelCategory> findAll(CategoryServiceMemory.Filter filter, int start, int maxResults) {
		List<HotelCategory> list = this.findAll(filter);

		int end = start + maxResults > list.size() ? list.size() : start + maxResults;
		return list.subList(start, end);
	}
	
	public HotelCategory findById(Long id) {
		return categories.get(id);
	}

	public long count() {
		return categories.size();
	}

	public void delete(HotelCategory category) {
		categories.remove(category.getId());
	}

	// create and update in one
	public void save(HotelCategory entry) {
		if (entry == null) {
			LOGGER.log(Level.SEVERE, "Hotel is null.");
			return;
		}
		// synchronization on instance required here(or make save() method
		// synchronized again)
		if (entry.getId() == null) {
			synchronized (this) {
				entry.setId(nextId++);
			}
		}
		try {
			entry = (HotelCategory) entry.clone();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		categories.put(entry.getId(), entry);
	}

	public void fillTestData() {
		final String[] data = new String[] { "Hotel", "Hostel", "Villa", "Appartments" };

		Long id = 0L;
		for (String category : data) {
			HotelCategory cat = new HotelCategory();
			cat.setId(++id);
			cat.setCategoryName(category);
			save(cat);
		}
	}

	public interface Filter {
		boolean matches(HotelCategory category);
	}

}