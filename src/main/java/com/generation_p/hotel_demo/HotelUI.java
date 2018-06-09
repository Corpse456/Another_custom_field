package com.generation_p.hotel_demo;

import java.util.List;

import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoaderListener;

import com.generation_p.hotel_demo.entity.HotelCategory;
import com.generation_p.hotel_demo.services.ServiceProvider;
import com.generation_p.hotel_demo.views.CategoryView;
import com.generation_p.hotel_demo.views.HotelView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
@Theme("hotelTheme")
@SpringUI
public class HotelUI extends UI {
	/**
	 * page title
	 */
	private static final String PAGE_TITLE = "Demo: Hotels";
	private MenuBar menuBar;
	private Navigator navigator;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		// set name of the page
		getPage().setTitle(PAGE_TITLE);

		// init test data
		initData();

		// create layout
		VerticalLayout mainLayout = initMainView();
		// add menu
		mainLayout.addComponents(getMenuBar());

		// create panel for main layout
		Panel mainPanel = initMainPanel();
		mainLayout.addComponent(mainPanel);
		mainLayout.setExpandRatio(mainPanel, 100);

		super.setId("mainView");
		setContent(mainLayout);
		setSizeFull();

		// initialize navigator
		navigator = new Navigator(this, mainPanel);
		// register views
		navigator.addView(HotelView.VIEW_NAME, HotelView.class);
		navigator.addView(CategoryView.VIEW_NAME, CategoryView.class);

		// navigate to hotels
		navigator.navigateTo(HotelView.VIEW_NAME);

	}

	private void initData() {
		ServiceProvider.getCategoryService().fillTestData();
		List<HotelCategory> categories = ServiceProvider.getCategoryService().findAll();
		ServiceProvider.getHotelService().fillTestData(categories);
	}

	private VerticalLayout initMainView() {
		VerticalLayout mainLayout = new VerticalLayout();

		mainLayout.setId("mainLayout");
		mainLayout.setSizeFull();

		// add style and make look a bit clean
		addStyleName(ValoTheme.UI_WITH_MENU);
		// to prevent this:
		// https://drive.google.com/open?id=0B5wxLBvMe4rjZ2JnWUhPNFExMFk
		mainLayout.setMargin(false);
		// to prevent this:
		// https://drive.google.com/open?id=0B5wxLBvMe4rjaG5XUE5kWnVRTmM
		mainLayout.setSpacing(false);

		return mainLayout;
	}

	private Panel initMainPanel() {
		Panel mainPanel = new Panel();
		mainPanel.setSizeFull();
		mainPanel.setId("mainPanel");
		return mainPanel;
	}

	private MenuBar getMenuBar() {
		if (menuBar != null) {
			return menuBar;
		}
		// create menu bar
		menuBar = new MenuBar();

		// create menu items
		menuBar.addItem("Hotel", command -> navigator.navigateTo(HotelView.VIEW_NAME));
		menuBar.addItem("", null).setEnabled(false);
		menuBar.addItem("Categories", command -> navigator.navigateTo(CategoryView.VIEW_NAME));

		menuBar.setStyleName(ValoTheme.MENUBAR_SMALL);

		return menuBar;
	}

	@WebServlet(urlPatterns = "/*", name = "HotelUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = HotelUI.class, productionMode = false)
	public static class HotelUIServlet extends VaadinServlet {}

	@Configuration
	@EnableVaadin
	public static class MyConfiguration {
		@Bean
		public static ServiceProvider getEntityService() {
			return new ServiceProvider();
		};
	}

	@WebListener
	public static class MyContextLoaderListener extends ContextLoaderListener {}

}
