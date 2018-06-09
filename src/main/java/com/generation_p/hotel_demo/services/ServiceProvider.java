package com.generation_p.hotel_demo.services;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ServiceProvider implements ApplicationContextAware {

	private static ApplicationContext appContext;

	@Override
	@Autowired
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}

	public static HotelService getHotelService() {
		return appContext.getBean("hotelService", HotelService.class);
	}

	public static CategoryService getCategoryService() {
		return appContext.getBean("categoryService", CategoryService.class);
	}

}
