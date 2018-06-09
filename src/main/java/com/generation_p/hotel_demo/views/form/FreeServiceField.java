package com.generation_p.hotel_demo.views.form;

import com.generation_p.hotel_demo.entity.FreeServices;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class FreeServiceField extends CustomField<FreeServices> {

	CheckBox breakfast = new CheckBox();
	CheckBox towels = new CheckBox();
	CheckBox spirits = new CheckBox();

	private FreeServices value;
	private String caption = "Free";

	public FreeServiceField(String caption) {
		super();
		this.caption = caption;
	}

	@Override
	public FreeServices getValue() {
		return value;
	}

	@Override
	protected Component initContent() {
		HorizontalLayout hor = new HorizontalLayout();
		super.setCaption(caption);
		breakfast.setIcon(VaadinIcons.SPOON);
		towels.setIcon(VaadinIcons.SQUARE_SHADOW);
		spirits.setIcon(VaadinIcons.TROPHY);
		
		breakfast.setDescription("Breakfast");
		towels.setDescription("Towels");
		spirits.setDescription("Spirits");

		breakfast.addValueChangeListener(l -> value.setBrekfast(l.getValue()));
		towels.addValueChangeListener(l -> value.setTowels(l.getValue()));
		spirits.addValueChangeListener(l -> value.setColdSpirits(l.getValue()));

		hor.addComponent(breakfast);
		hor.addComponent(towels);
		hor.addComponent(spirits);

		updateValues();

		// value = new FreeServices();
		return hor;
	}

	private void updateValues() {
		if (getValue() != null) {
			breakfast.setValue(value.isBrekfast());
			towels.setValue(value.isTowels());
			spirits.setValue(value.isColdSpirits());
		}

	}

	@Override
	protected void doSetValue(FreeServices value) {
		this.value = new FreeServices(value);
		updateValues();
	}

}
