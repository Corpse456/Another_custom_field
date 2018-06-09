package com.generation_p.hotel_demo.views;

import com.generation_p.hotel_demo.entity.HotelCategory;
import com.generation_p.hotel_demo.services.ServiceProvider;
import com.generation_p.hotel_demo.views.form.CategoryForm;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

@SuppressWarnings({ "serial" })
@SpringUI
public class CategoryView extends AbstractEntityView {
	public static String VIEW_NAME = "categories";

	private ListSelect<HotelCategory> list = new ListSelect<>();
	private CategoryForm form;

	/**
	 * Method that is called each time on view enter
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		form = new CategoryForm(this);
		// add buttons
		HorizontalLayout main = new HorizontalLayout(list, form);
		HorizontalLayout buttons = getButtons();
		buttons.removeComponent(fillRooms);
		addComponents(buttons, main);
		updateList();

		list.setItemCaptionGenerator(HotelCategory::getCategoryName);
		list.setWidth(16, Unit.EM);

		form.setVisible(false);

		list.addSelectionListener(selEvent -> {
			if (selEvent == null) {
				enableButtons(false, false);
				return;
			}
			// manage buttons
			int selected = selEvent.getAllSelectedItems().size();
			// edit available only for 1 selected element
			// delete -- for several
			enableButtons(selected == 1, selected > 0);
		});
	}

	@Override
	protected void delete() {
		list.getSelectedItems().stream().forEach(i -> ServiceProvider.getCategoryService().delete(i));
		Notification not = new Notification("Category sucessfully deleted!");
		not.show(UI.getCurrent().getPage());
		updateList();
	}

	@Override
	protected void edit() {
		form.setVisible(true);
		form.setItem(list.getSelectedItems().iterator().next());
	}

	@Override
	protected void add() {
		HotelCategory cat = new HotelCategory();
		form.setVisible(true);
		form.setItem(cat);
	}

	public void updateList() {
		list.setItems(ServiceProvider.getCategoryService().findAll());
	}

}
