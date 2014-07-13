package com.ifgov.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

public class AboutPopup {
	PopupPanel popupPanel = new PopupPanel();

	public AboutPopup() {
		FlowPanel flowPanel = new FlowPanel();
		flowPanel.add(new HTML("<h1>If Gov Then That</h1>"));
		flowPanel
				.add(new HTML(
						"<p>Search for your address, take a look at the data, then subscribe to get notified when it changes!</p>"));

		Anchor anchor = new Anchor("OK lets go!");

		anchor.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				popupPanel.hide();
			}
		});
		flowPanel.add(anchor);

		flowPanel
				.add(new HTML(
						"<p /><p><i>Created by Brendan Gordon, Kelvin Wauchope, and James Caunce.</i></p>"));

		flowPanel.setWidth("500px");
		flowPanel.setHeight("400px");

		popupPanel.add(flowPanel);
		popupPanel.setGlassEnabled(true);
		popupPanel.setAnimationEnabled(true);
		popupPanel.addStyleName("rounded-panel");

	}

	public void show() {
		popupPanel.center();
		popupPanel.show();
	}

}
