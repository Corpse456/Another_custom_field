package com.generation_p.hotel_demo.services.memory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.generation_p.hotel_demo.entity.Hotel;
import com.generation_p.hotel_demo.entity.HotelCategory;

/**
 * 
 * @author Oleg Romanovski
 *
 */
public class HotelServiceMemory {

	private static HotelServiceMemory instance;

	private static final Logger LOGGER = Logger.getLogger(HotelServiceMemory.class.getName());

	private static Comparator<Hotel> hotelComparator = new Comparator<Hotel>() {
		@Override
		public int compare(Hotel o1, Hotel o2) {
			if (o1.getId() < o2.getId())
				return 1;
			if (o1.getId() > o2.getId())
				return -1;
			return 0;
		}
	};

	// concurrentHashMap better than HashMap + synchronized methods
	private final Map<Long, Hotel> hotels = new ConcurrentHashMap<>();

	private long nextId = 0;

	private HotelServiceMemory() {}

	public static synchronized HotelServiceMemory getInstance() {
		if (instance == null) {
			instance = new HotelServiceMemory();
			instance.fillTestData();
		}
		return instance;
	}

	public List<Hotel> findAll() {
		List<Hotel> result = new ArrayList<>(hotels.size());
		try {
			for (Hotel h : hotels.values()) {
				result.add(h.clone());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		result.sort(hotelComparator);
		return result;
	}

	public List<Hotel> findAll(HotelServiceMemory.Filter filter) {
		List<Hotel> result = new ArrayList<>();
		try {
			for (Hotel h : hotels.values()) {
				if (filter.matches(h))
					result.add(h.clone());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		result.sort(hotelComparator);
		return result;
	}

	public List<Hotel> findAll(HotelServiceMemory.Filter filter, int start, int maxResults) {
		// list already sorted and its values are cloned
		List<Hotel> list = this.findAll(filter);

		int end = start + maxResults > list.size() ? list.size() : start + maxResults;
		return list.subList(start, end);
	}

	public long count() {
		return hotels.size();
	}

	public void delete(Hotel value) {
		hotels.remove(value.getId());
	}

	// create and update in one
	public void save(Hotel entry) {
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
			entry = (Hotel) entry.clone();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		hotels.put(entry.getId(), entry);
	}

	public void fillTestData() {
		final String[] hotelData = new String[] {
				"3 Nagas Luang Prabang - MGallery by Sofitel;4;https://www.booking.com/hotel/la/3-nagas-luang-prabang-by-accor.en-gb.html;Vat Nong Village, Sakkaline Road, Democratic Republic Lao, 06000 Luang Prabang, Laos;",
				"Abby Boutique Guesthouse;1;https://www.booking.com/hotel/la/abby-boutique-guesthouse.en-gb.html;Ban Sawang , 01000 Vang Vieng, Laos",
				"Bountheung Guesthouse;1;https://www.booking.com/hotel/la/bountheung-guesthouse.en-gb.html;Ban Tha Heua, 01000 Vang Vieng, Laos",
				"Chalouvanh Hotel;2;https://www.booking.com/hotel/la/chalouvanh.en-gb.html;13 road, Ban Phonesavanh, Pakse District, 01000 Pakse, Laos",
				"Chaluenxay Villa;3;https://www.booking.com/hotel/la/chaluenxay-villa.en-gb.html;Sakkarin Road Ban Xienthong Luang Prabang Laos, 06000 Luang Prabang, Laos",
				"Dream Home Hostel 1;1;https://www.booking.com/hotel/la/getaway-backpackers-hostel.en-gb.html;049 Sihome Road, Ban Sihome, 01000 Vientiane, Laos",
				"Inpeng Hotel and Resort;2;https://www.booking.com/hotel/la/inpeng-and-resort.en-gb.html;406 T4 Road, Donekoy Village, Sisattanak District, 01000 Vientiane, Laos",
				"Jammee Guesthouse II;2;https://www.booking.com/hotel/la/jammee-guesthouse-vang-vieng1.en-gb.html;Vang Vieng, 01000 Vang Vieng, Laos",
				"Khemngum Guesthouse 3;2;https://www.booking.com/hotel/la/khemngum-guesthouse-3.en-gb.html;Ban Thalat No.10 Road Namngum Laos, 01000 Thalat, Laos",
				"Khongview Guesthouse;1;https://www.booking.com/hotel/la/khongview-guesthouse.en-gb.html;Ban Klang Khong, Khong District, 01000 Muang Không, Laos",
				"Kong Kham Pheng Guesthouse;1;https://www.booking.com/hotel/la/kong-kham-pheng-guesthouse.en-gb.html;Mixay Village, Paksan district, Bolikhamxay province, 01000 Muang Pakxan, Laos",
				"Laos Haven Hotel & Spa;3;https://www.booking.com/hotel/la/laos-haven.en-gb.html;047 Ban Viengkeo, Vang Vieng , 01000 Vang Vieng, Laos",
				"Lerdkeo Sunset Guesthouse;1;https://www.booking.com/hotel/la/lerdkeo-sunset-guesthouse.en-gb.html;Muang Ngoi Neua,Ban Ngoy-Nua, 01000 Muang Ngoy, Laos",
				"Luangprabang River Lodge Boutique 1;3;https://www.booking.com/hotel/la/luangprabang-river-lodge.en-gb.html;Mekong River Road, 06000 Luang Prabang, Laos",
				"Manichan Guesthouse;2;https://www.booking.com/hotel/la/manichan-guesthouse.en-gb.html;Ban Pakham Unit 4/143, 60000 Luang Prabang, Laos",
				"Mixok Inn;2;https://www.booking.com/hotel/la/mixok-inn.en-gb.html;188 Sethathirate Road , Mixay Village , Chanthabuly District, 01000 Vientiane, Laos",
				"Ssen Mekong;2;https://www.booking.com/hotel/la/muang-lao-mekong-river-side-villa.en-gb.html;Riverfront, Mekong River Road, 06000 Luang Prabang, Laos",
				"Nammavong Guesthouse;2;https://www.booking.com/hotel/la/nammavong-guesthouse.en-gb.html;Ban phone houang Sisalearmsak Road , 06000 Luang Prabang, Laos",
				"Niny Backpacker hotel;1;https://www.booking.com/hotel/la/niny-backpacker.en-gb.html;Next to Wat Mixay, Norkeokhunmane Road., 01000 Vientiane, Laos",
				"Niraxay Apartment;2;https://www.booking.com/hotel/la/niraxay-apartment.en-gb.html;Samsenthai Road Ban Sihom , 01000 Vientiane, Laos",
				"Pakse Mekong Hotel;2;https://www.booking.com/hotel/la/pakse-mekong.en-gb.html;No 062 Khemkong Road, Pakse District, Champasak, Laos, 01000 Pakse, Laos",
				"Phakchai Hotel;2;https://www.booking.com/hotel/la/phakchai.en-gb.html;137 Ban Wattay Mueng Sikothabong Vientiane Laos, 01000 Vientiane, Laos",
				"Phetmeuangsam Hotel;2;https://www.booking.com/hotel/la/phetmisay.en-gb.html;Ban Phanhxai, Xumnuea, Xam Nua, 01000 Xam Nua, Laos" };

		Random r = new Random(0);
		for (String hotel : hotelData) {
			String[] split = hotel.split(";");
			Hotel h = new Hotel();
			h.setName(split[0]);
			h.setRating(Integer.parseInt(split[1]));
			h.setUrl(split[2]);
			h.setAddress(split[3]);
			h.setCategory(new HotelCategory());
			long daysOld = r.nextInt(365 * 30);
			h.setOperatesFrom(daysOld);
			h.setDescription("no description");
			save(h);
		}
	}

	public interface Filter {
		boolean matches(Hotel hotel);
	}

}