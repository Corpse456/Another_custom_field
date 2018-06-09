package com.generation_p.hotel_demo.views.form;

import com.generation_p.hotel_demo.entity.HotelCategory;
import com.generation_p.hotel_demo.services.CategoryService;
import com.generation_p.hotel_demo.services.ServiceProvider;
import com.generation_p.hotel_demo.views.CategoryView;
import com.vaadin.data.ValidationException;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@SpringUI
public class CategoryForm extends AbstractForm<HotelCategory> {

	private TextField name = new TextField("Name");

	private CategoryService categoryDAO;

	private HotelCategory category;

	private CategoryView categoryUI;

	public CategoryForm(CategoryView myUI) {
		super();
		this.categoryDAO = ServiceProvider.getCategoryService();
		this.categoryUI = myUI;

		addComponents(name, buttons);

		bindFields();
	}

	public void setItem(HotelCategory category) {
		this.category = category;
		binder.readBean(this.category);
	}

	@Override
	protected void save() {
		// to make all validate marks appear
		binder.validate();
		if (binder.isValid()) {
			try {
				binder.writeBean(category);
				categoryDAO.save(category);
				Notification not = new Notification("Category saved sucessfully");
				not.show(UI.getCurrent().getPage());
				categoryUI.updateList();
				closeForm();
			} catch (ValidationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Notification not = new Notification("Please check the form");
			not.show(UI.getCurrent().getPage());
		}
	}

	@Override
	protected void bindFields() {
		binder.forField(name).asRequired("Name is required").bind(HotelCategory::getCategoryName,
				HotelCategory::setCategoryName);
	}

}
