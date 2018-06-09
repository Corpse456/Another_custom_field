package com.generation_p.hotel_demo.views.form;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings ("serial")
public class RoomsField extends CustomField<String> {

    private Map<String, String> rooms = new TreeMap<String, String>();
    private String caption = "Rooms";
    private VerticalLayout layout = new VerticalLayout();
    private ComboBox<String> roomsSelect = new ComboBox<>();
    private Image roomsPhoto = new Image();
    
    private static final String SELECTION_PLACEHOLDER = "Please select a room";
    private String currentKey;
    
    @Override
    public String getValue () {
        StringBuilder roomsIntoBase = new StringBuilder();
        Iterator<Entry<String, String>> iterator = rooms.entrySet().iterator();
        while (iterator.hasNext()) {
            Entry<String, String> curent = iterator.next();
            roomsIntoBase.append(curent.getKey() + "=");
            roomsIntoBase.append(curent.getValue());
            if (iterator.hasNext()) roomsIntoBase.append(",");
        }
        return roomsIntoBase.toString();
    }

    @Override
    protected Component initContent () {
        super.setCaption(caption);
        layout.setMargin(false);
        
        selectionSetup();
        
        roomsPhoto.setVisible(false);
        roomsPhoto.setWidth(11, Unit.EM);
        
        layout.addComponents(roomsSelect, roomsPhoto);
        layout.setWidth("100%");
        return layout;
    }

    private void selectionSetup () {
        roomsSelect.setWidth("100%");
        roomsSelect.setEmptySelectionCaption(SELECTION_PLACEHOLDER);
        roomsSelect.addSelectionListener(event -> {
            currentKey = event.getValue();
            if (currentKey == null || SELECTION_PLACEHOLDER.equals(currentKey)) {
                roomsPhoto.setVisible(false);
                return;
            }
            
            roomsPhoto.setVisible(true);
            roomsPhoto.setSource(new ExternalResource(rooms.get(currentKey)));
        });

        roomsSelect.setNewItemHandler(s -> {
            if (s == null || SELECTION_PLACEHOLDER.equals(s) || s.equals(currentKey)) return;

            String link = rooms.get(currentKey);
            rooms.remove(currentKey);
            rooms.put(s, link);
        });
    }

    @Override
    protected void doSetValue (String value) {
        rooms.clear();
        
        if (value != null) Arrays.asList(value.split(",")).forEach(val -> {
            String[] split = val.split("=");
            rooms.put(split[0], split[1]);
        });
        
        roomsSelect.setValue(roomsSelect.getEmptySelectionCaption());
        roomsSelect.setItems(rooms.keySet());
    }
}
