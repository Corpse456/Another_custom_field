package com.generation_p.hotel_demo.views;

import java.util.Set;

import org.vaadin.viritin.util.HtmlElementPropertySetter;

import com.generation_p.hotel_demo.entity.Hotel;
import com.generation_p.hotel_demo.services.HotelDataProvider;
import com.generation_p.hotel_demo.services.RoomService;
import com.generation_p.hotel_demo.services.ServiceProvider;
import com.generation_p.hotel_demo.views.form.HotelForm;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@SpringUI
public class HotelView extends AbstractEntityView {

	public static String VIEW_NAME = "hotels";

	private HotelForm form;
	private Grid<Hotel> grid = new Grid<>(Hotel.class);
	private TextField filterText = new TextField();
	private ConfigurableFilterDataProvider<Hotel, Void, String> dataProvider;
	private RoomService room = new RoomService();

	/**
	 * Method that is called each time on view enter
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		form = new HotelForm(this);
		HorizontalLayout controlPannel = new HorizontalLayout();
		initControls(controlPannel);

		initGridComponent();
		// hide form by default
		form.setVisible(false);

		HorizontalLayout main = new HorizontalLayout(grid, form);
		main.setSizeFull();
		main.setExpandRatio(grid, 1);

		addComponents(controlPannel, main);
	}

	private void initControls(HorizontalLayout controlPannel) {
		filterText.setPlaceholder("filter by name...");
		filterText.addValueChangeListener(e -> updateList());
		filterText.setValueChangeMode(ValueChangeMode.LAZY);
		filterText.setStyleName(ValoTheme.TEXTFIELD_TINY);
		// make the field look like HTML5 search
		HtmlElementPropertySetter s1 = new HtmlElementPropertySetter(filterText);
		s1.setProperty("type", "search");

		HorizontalLayout buttons = getButtons();
		fillRooms.addClickListener(l -> {
            try {
                room.click();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        });
		buttons.removeComponent(editButton);
		controlPannel.addComponents(filterText, buttons);
	}

	private void initGridComponent() {
		grid.setColumns("name", "rating", "address");
		grid.addColumn(hotel -> hotel.getCategory() == null ? "Not defined" : hotel.getCategory().getCategoryName(),
				new HtmlRenderer()).setCaption("Category");
		grid.addColumn(hotel -> "<a href='" + hotel.getUrl() + "' target='_blank'>More info</a>", new HtmlRenderer())
				.setCaption("Info link").setId("url");

		grid.asSingleSelect();
		// author: Sergey Nikonov
		grid.addItemClickListener(e -> {
			enableButtons(false, e.getItem() != null);
			if (e.getItem() == null) {
				form.setVisible(false);
			} else if (!e.getColumn().equals(grid.getColumn("url"))) {
				form.setVisible(true);
				form.setHotel(e.getItem());
			}
		});
		grid.setSizeFull();
		grid.setHeight(31, Unit.EM);
		
		dataProvider = new HotelDataProvider().withConfigurableFilter();
		grid.setDataProvider(dataProvider);
		grid.setId("HotelGrid");

		updateList();
	}

	public void updateList() {
		String filter = filterText.getValue();
		dataProvider.setFilter(filter);
		// grid.getDataProvider().refreshAll();
		// List<Hotel> hotels =
		// EntityService.getHotelService().findAll(filterText.getValue());
		// grid.setItems(hotels);
	}

	public SerializablePredicate<Hotel> getFilter() {
		String filter = filterText.getValue();
		SerializablePredicate<Hotel> predicate = hotel -> hotel.getName().toLowerCase()
				.contains(filter == null ? "" : filter.toLowerCase());
		return predicate;
	}

	@Override
	protected void delete() {
		Set<Hotel> selected = grid.getSelectedItems();
		selected.forEach(item -> ServiceProvider.getHotelService().delete(item));
		form.closeForm();
		updateList();
	}

	@Override
	protected void edit() {}

	@Override
	protected void add() {
		Hotel h = new Hotel();
		form.setVisible(true);
		form.setHotel(h);

	}

}
