package com.generation_p.hotel_demo.views.form;

import java.security.acl.Owner;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public abstract class AbstractForm<T> extends FormLayout {

	protected Button save = new Button("Save");
	protected Button close = new Button("Close");
	HorizontalLayout buttons = new HorizontalLayout(save, close);
	Binder<T> binder = new Binder<>();

	public AbstractForm() {
		setSizeUndefined();

		// button styles
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// button actions
		save.addClickListener(e -> save());
		close.addClickListener(e -> closeForm());
	}

	public void closeForm() {
		binder.removeBean();
		this.setVisible(false);
	}

	protected abstract void save();
	protected abstract void bindFields();

}
