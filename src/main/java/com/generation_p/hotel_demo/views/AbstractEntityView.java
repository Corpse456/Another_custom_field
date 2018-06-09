package com.generation_p.hotel_demo.views;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public abstract class AbstractEntityView extends VerticalLayout implements View {

	private static String NEW_BUTTON = "New";
	private static String EDIT_BUTTON = "Edit";
	private static String DELETE_BUTTON = "Delete";

	// buttons
	protected Button newButton = new Button(NEW_BUTTON, VaadinIcons.PLUS);
	protected Button editButton = new Button(EDIT_BUTTON, VaadinIcons.EDIT);
	protected Button deleteButton = new Button(DELETE_BUTTON, VaadinIcons.CLOSE);

	/**
	 * All general controls will be initialized here
	 * 
	 * @return Layout with configured buttons
	 */
	public HorizontalLayout getButtons() {
		// set actions listeners
		newButton.addClickListener(l -> add());
		editButton.addClickListener(l -> edit());
		deleteButton.addClickListener(l -> delete());
		// update operations disabled by default
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
		// set styles
		newButton.setStyleName(ValoTheme.BUTTON_TINY);
		editButton.setStyleName(ValoTheme.BUTTON_TINY);
		deleteButton.setStyleName(ValoTheme.BUTTON_TINY);

		// add buttons to layout
		HorizontalLayout layout = new HorizontalLayout(newButton, editButton, deleteButton);
		layout.setSpacing(true);

		return layout;
	}

	/**
	 * Manage buttons
	 * 
	 * @param enableEdit
	 * @param enableDelete
	 */
	protected void enableButtons(boolean enableEdit, boolean enableDelete) {
		editButton.setEnabled(enableEdit);
		deleteButton.setEnabled(enableDelete);
	}

	protected abstract void delete();

	protected abstract void edit();

	protected abstract void add();
}
