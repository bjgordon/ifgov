package com.ifgov.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.ifgov.shared.SubscriptionDto;

public class SubscribePopup extends Composite {
	PopupPanel popupPanel = new PopupPanel(false, true);

	HTML error = new HTML();
	TextBox name = new TextBox();

	ListBox when = new ListBox();
	Label changesAt = new Label();
	ListBox then = new ListBox();
	TextBox meAt = new TextBox();
	Button subscribe = new Button("Subscribe");
	Button cancel = new Button("Cancel");

	Double lat;
	Double lon;

	public SubscribePopup(Double lat, Double lon) {
		this.lat = lat;
		this.lon = lon;

		changesAt.setText("" + lat + " °North, " + lon + " °East");
		when.addItem("Broadband");
		when.addItem("Average salary");
		then.addItem("email");
		then.addItem("tweet");

		FlowPanel flowPanel = new FlowPanel();
		flowPanel.getElement().setClassName("form-group");

		name.getElement().setAttribute("placeholder", "Name");

		// Set bootstrap classnames so we look shiny
		name.getElement().setClassName("form-control");
		when.getElement().setClassName("form-control");
		then.getElement().setClassName("form-control");
		meAt.getElement().setClassName("form-control");
		subscribe.getElement().setClassName("btn");
		subscribe.getElement().addClassName("btn-primary");
		cancel.getElement().setClassName("btn");
		cancel.getElement().addClassName("btn-primary");

		flowPanel.add(new HTML("<h1>Create new subscription</h1>"));
		flowPanel.add(new HTML("<label>Name</label>"));
		flowPanel.add(name);
		flowPanel.add(new HTML("<label>When</label>"));
		flowPanel.add(when);
		flowPanel.add(new HTML("<label>changes at</label>"));
		flowPanel.add(changesAt);
		flowPanel.add(new HTML("<label>then</label>"));
		flowPanel.add(then);
		flowPanel.add(new HTML("<label>me at</label>"));
		flowPanel.add(meAt);
		flowPanel.add(error);
		flowPanel.add(subscribe);
		flowPanel.add(cancel);

		popupPanel.add(flowPanel);
		popupPanel.setWidth("500px");
		popupPanel.setHeight("400px");

		subscribe.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				List<String> failedMessages = new ArrayList<String>();
				if (name.getText() == null || name.getText().trim().isEmpty()) {
					failedMessages.add("Username is required");
				}

				if (meAt.getText() == null || meAt.getText().trim().isEmpty()) {
					failedMessages.add("Me At is required");
				}

				if (!failedMessages.isEmpty()) {
					String html = "<ul>";
					for (String s : failedMessages) {
						html += "<li>" + s + "</li>";
					}
					html += "</ul>";
					error.setHTML(html);
					error.setVisible(true);
				} else {
					error.setVisible(false);
					doSubscribe();
				}
			}
		});

		cancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				popupPanel.hide();
			}
		});

		error.setVisible(false);
		error.getElement().getStyle().setColor("red");

	}

	public void show() {
		popupPanel.center();
		popupPanel.show();
	}

	public void hide() {
		popupPanel.hide();
	}

	public void doSubscribe() {
		SubscriptionDto subscriptionDto = new SubscriptionDto();
		subscriptionDto.setName(name.getText());
		subscriptionDto.setWhen(when.getItemText(when.getSelectedIndex()));
		subscriptionDto.setThen(then.getItemText(then.getSelectedIndex()));
		subscriptionDto.setAt(meAt.getText());

		subscribe.setEnabled(false);
		subscribe.setText("Subscribing...");
		IfGovServiceAsync service = GWT.create(IfGovService.class);
		service.subscribe(subscriptionDto, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable arg0) {
				error.setHTML("Problem saving subscription: "
						+ arg0.getMessage());
				error.setVisible(true);
				subscribe.setEnabled(true);
				subscribe.setText("Subscribe");

			}

			@Override
			public void onSuccess(Void arg0) {
				popupPanel.hide();
			}
		});

	}
}
