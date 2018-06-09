package com.generation_p.hotel_demo.views.form;

import com.generation_p.hotel_demo.entity.Hotel;
import com.generation_p.hotel_demo.entity.HotelCategory;
import com.generation_p.hotel_demo.services.CategoryService;
import com.generation_p.hotel_demo.services.HotelService;
import com.generation_p.hotel_demo.services.ServiceProvider;
import com.generation_p.hotel_demo.views.HotelView;
import com.generation_p.hotel_demo.views.convertor.DateConverter;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.ui.DateField;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

public class HotelForm extends AbstractForm<Hotel> {

	private static final long serialVersionUID = -2249768301112428822L;
	private TextField name = new TextField("Name");
	private TextField address = new TextField("Address");
	private TextField rating = new TextField("Rating");
	private NativeSelect<HotelCategory> category = new NativeSelect<>("Category");
	private DateField operatesFrom = new DateField("Since");
	private TextField url = new TextField("URL");
	private TextArea description = new TextArea("Description");
	private FreeServiceField freeService = new FreeServiceField("Fee services");
	private RoomsField rooms = new RoomsField();

	private HotelService hotelDAO;
	@SuppressWarnings ("unused")
    private CategoryService categoryDAO;

	private Hotel hotel;
	private HotelView hotelUI;

	public HotelForm(HotelView myUI) {
		super();
		this.hotelUI = myUI;
		this.hotelDAO = ServiceProvider.getHotelService();
		this.categoryDAO = ServiceProvider.getCategoryService();

		addComponents(name, address, rating, category, rooms, freeService, operatesFrom, url, description, buttons);
		category.setItems(ServiceProvider.getCategoryService().findAll());
		category.setItemCaptionGenerator(item -> item.getCategoryName());
		category.setWidth(11, Unit.EM);
		rooms.setWidth(11, Unit.EM);
		
		bindFields();
	}

	@Override
	protected void bindFields() {
		binder.forField(rating).asRequired("Field is required").withNullRepresentation("")
				.withConverter(new StringToIntegerConverter(0, "Only digits!"))
				.withValidator(v -> v < 6, "Rating should be a possitive number less then 6")
				.withValidator(v -> v > 0, "Rating should be a possitive number")
				.bind(Hotel::getRating, Hotel::setRating);
		binder.forField(name).asRequired("Field is required").withNullRepresentation("").bind(Hotel::getName,
				Hotel::setName);
		binder.forField(address).asRequired("Field is required").withNullRepresentation("").bind(Hotel::getAddress,
				Hotel::setAddress);
		binder.forField(category).asRequired("Field is required").withNullRepresentation(null).bind(Hotel::getCategory,
				Hotel::setCategory);
		binder.forField(url).asRequired("Field is required").withNullRepresentation("").bind(Hotel::getUrl,
				Hotel::setUrl);
		binder.forField(operatesFrom).asRequired("Field is required").withNullRepresentation(null)
				.withConverter(new DateConverter()).bind(Hotel::getOperatesFrom, Hotel::setOperatesFrom);
		binder.forField(description).bind(Hotel::getDescription, Hotel::setDescription);
		binder.forField(freeService).bind(Hotel::getFreeServices, Hotel::setFreeServices);
		binder.forField(rooms).bind(Hotel::getRooms, Hotel::setRooms);
		// binder.bindInstanceFields(this);
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
		binder.readBean(hotel);
		close.setVisible(true);
		setVisible(true);
	}

	@Override
	protected void save() {
		binder.validate();
		if (binder.isValid()) {
			try {
				binder.writeBean(hotel);
				hotelDAO.save(hotel);
				hotelUI.updateList();
				closeForm();
			} catch (ValidationException e) {
				e.printStackTrace();
			}
		} else {
			Notification notification = new Notification(
					"There are some unvalid values, please check the input and try again.");
			notification.show(UI.getCurrent().getPage());
		}
	}

}
