package com.generation_p.hotel_demo.services;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.generation_p.hotel_demo.entity.FreeServices;
import com.generation_p.hotel_demo.entity.Hotel;
import com.generation_p.hotel_demo.entity.HotelCategory;

@Repository
public class HotelService {

	private static final String SELECT_WITH_FILTER = "select h from Hotel h where lower(h.name) like :filter or lower(h.address) like :filter";

	@PersistenceContext(unitName = "hotel_demo", type = PersistenceContextType.TRANSACTION)
	private EntityManager entityManager;

	@SuppressWarnings ("unused")
    private static final Logger LOGGER = Logger.getLogger(HotelService.class.getName());

	public HotelService() {}

	public List<Hotel> findAll() {
		return entityManager.createQuery("SELECT h from Hotel h", Hotel.class).getResultList();
	}

	@Transactional
	public List<Hotel> findAll(String filter) {
		if (filter == null) {
			return findAll();
		}
		return entityManager.createQuery(SELECT_WITH_FILTER, Hotel.class)
				.setParameter("filter", "%" + filter.toLowerCase() + "%").getResultList();
	}

	@Transactional
	public List<Hotel> findAll(String filter, int start, int maxResults) {
		if (filter == null) {
			return findAll();
		}
		// "select h from Hotel h where lower(h.name) like :filter or
		// lower(h.address) like :filter";
		return entityManager.createQuery(SELECT_WITH_FILTER, Hotel.class)
				.setParameter("filter", "%" + filter.toLowerCase() + "%").setFirstResult(start)
				.setMaxResults(maxResults).getResultList();
	}

	@Transactional
	public long count(String filter) {
		return entityManager.createQuery(SELECT_WITH_FILTER, Hotel.class)
				.setParameter("filter", "%" + filter.toLowerCase() + "%").getResultList().size();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Hotel value) {
		Hotel hotel = entityManager.find(Hotel.class, value.getId());
		entityManager.remove(hotel);
	}

	// create and update in one
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(Hotel entry) {
		Hotel hotel = entityManager.merge(entry);
		entityManager.persist(hotel);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void fillTestData(List<HotelCategory> categories) {
		if (findAll().isEmpty()) {

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
				h.setCategory(categories.get(r.nextInt(categories.size() - 1)));
				long daysOld = r.nextInt(365 * 30);
				h.setOperatesFrom(daysOld);
				h.setDescription("no description");
				h.setFreeServices(new FreeServices());
				save(h);
			}
			entityManager.flush();
		}
	}

}