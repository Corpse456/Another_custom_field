package com.generation_p.hotel_demo.services;

import java.util.stream.Stream;

import com.generation_p.hotel_demo.entity.Hotel;
import com.vaadin.data.provider.AbstractBackEndDataProvider;
import com.vaadin.data.provider.Query;

@SuppressWarnings("serial")
public class HotelDataProvider extends AbstractBackEndDataProvider<Hotel, String> {

	private HotelService hotelService;

	public HotelDataProvider() {
		hotelService = ServiceProvider.getHotelService();
	}

	@Override
	protected Stream<Hotel> fetchFromBackEnd(Query<Hotel, String> query) {
		String filter = query.getFilter().orElse(null);
		return hotelService.findAll(filter, query.getOffset(), query.getLimit()).stream();
	}

	@Override
	protected int sizeInBackEnd(Query<Hotel, String> query) {
		String filter = query.getFilter().orElse(null);
		return hotelService.findAll(filter).size();
	}

}
