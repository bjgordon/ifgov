package com.ifgov.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.ifgov.shared.DataDto;
import com.ifgov.shared.SubscriptionDto;

public class SubscribePopup extends Composite {
	PopupPanel popupPanel = new PopupPanel(false, true);
	FlowPanel flowPanel = new FlowPanel();
	HTML error = new HTML();
	TextBox name = new TextBox();

	ListBox when = new ListBox();
	Label changesAt = new Label();
	ListBox then = new ListBox();
	TextBox meAt = new TextBox();
	Button subscribe = new Button("Subscribe");
	Button cancel = new Button("Cancel");

	double lat;
	double lon;
	DataDto data;

	public SubscribePopup(double lat, double lon, DataDto data) {
		this.lat = lat;
		this.lon = lon;
		this.data = data;

		NumberFormat decimalFormat = NumberFormat.getFormat(".###");

		changesAt.setText("" + decimalFormat.format(lat) + " °North, "
				+ decimalFormat.format(lon) + " °East");

		// changesAt.setText("" + lat + " °North, " + lon + " °East");
		when.addItem("Broadband");
		when.addItem("Average salary");
		when.addItem("New dataset is available");
		then.addItem("Email");
		then.addItem("Tweet");
		then.addItem("SMS");
		then.addItem("XMPP");

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
		flowPanel.add(new HTML("<label>Please tell us your name</label>"));
		flowPanel.add(name);
		// flowPanel.add(new HTML("<br />"));
		flowPanel.add(new HTML("<hr />"));
		flowPanel.add(new HTML("<label>When</label>"));
		flowPanel.add(when);
		flowPanel.add(new HTML("<label>changes at</label>"));
		flowPanel.add(changesAt);
		flowPanel.add(new HTML("<label>then</label>"));
		flowPanel.add(then);
		flowPanel.add(new HTML("<label>me at</label>"));
		flowPanel.add(meAt);
		flowPanel.add(new HTML("<br />"));
		flowPanel.add(error);
		flowPanel.add(subscribe);
		flowPanel.add(cancel);

		popupPanel.add(flowPanel);
		popupPanel.setWidth("500px");
		// popupPanel.setHeight("400px");

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

		then.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				setMeAtPlaceholder();
			}
		});
		setMeAtPlaceholder();

		error.setVisible(false);
		error.getElement().getStyle().setColor("red");
		flowPanel.getElement().getStyle().setPadding(20, Unit.PX);
		popupPanel.setGlassEnabled(true);
		popupPanel.setAnimationEnabled(true);
		popupPanel.addStyleName("rounded-panel");
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
		subscriptionDto.setLat(lat);
		subscriptionDto.setLon(lon);
		subscriptionDto.setCurrentvalue(data.getBroadband());
		subscriptionDto.setNotification(1); // todo email
		subscriptionDto.setNotificationsettings(meAt.getText());

		// todo handle sourceid
		subscriptionDto.setSourceid(1);

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
				// popupPanel.hide();
				subscribe.setVisible(false);
				cancel.setText("Close");

				String confirmationMethod = then.getItemText(then
						.getSelectedIndex());
				flowPanel.add(new HTML(
						"<label>Successfully subscribed. You will receive a confirmation "
								+ confirmationMethod + ".</label>"));

			}
		});

	}

	void setMeAtPlaceholder() {

		String thenStr = then.getItemText(then.getSelectedIndex());
		String placeholder;
		if (thenStr.equalsIgnoreCase("email")) {
			placeholder = "Email address";
		} else if (thenStr.equalsIgnoreCase("tweet")) {
			placeholder = "Twitter account";
		} else if (thenStr.equalsIgnoreCase("sms")) {
			placeholder = "Mobile number";
		} else {
			placeholder = "";
		}
		meAt.getElement().setAttribute("placeholder", placeholder);
	}
}
