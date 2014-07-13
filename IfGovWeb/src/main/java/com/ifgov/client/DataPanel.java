package com.ifgov.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.ifgov.shared.DataDto;

public class DataPanel extends Composite {
	FlowPanel flowPanel = new FlowPanel();

	TextBox name = new TextBox();
	Label changesAt = new Label();
	DataDto dataFromServer;
	VerticalPanel dataPanel = new VerticalPanel();
	Button subscribe = new Button("Subscribe");

	double lat;
	double lon;

	public DataPanel() {

		flowPanel.getElement().setClassName("form-group");

		name.getElement().setAttribute("placeholder", "Name");

		// Set bootstrap classnames so we look shiny
		name.getElement().setClassName("form-control");
		subscribe.getElement().setClassName("btn");
		subscribe.getElement().addClassName("btn-primary");

		// flowPanel.add(new HTML("<h1>Data</h1>"));
		flowPanel.add(changesAt);
		flowPanel.add(dataPanel);
		flowPanel.add(new HTML("<p />"));
		flowPanel
				.add(new HTML(
						"<label>Would you like to know when the data at this location changes?</label>"));
		flowPanel.add(subscribe);

		flowPanel.setWidth("400px");
		// flowPanel.setHeight("400px");

		flowPanel.getElement().getStyle().setPosition(Position.ABSOLUTE);
		flowPanel.getElement().getStyle().setTop(40, Unit.PX);
		flowPanel.getElement().getStyle().setRight(10, Unit.PX);
		flowPanel.getElement().getStyle().setBackgroundColor("#fafafc");
		flowPanel.getElement().getStyle().setPadding(20, Unit.PX);

		flowPanel.addStyleName("rounded-panel");
		subscribe.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				SubscribePopup subscribePopup = new SubscribePopup(lat, lon,
						dataFromServer);
				subscribePopup.show();
			}
		});

		initWidget(flowPanel);

	}

	// Parse the json into a two column key-value grid table
	Grid getGridFromJson(String json) {
		if (json == null || json.trim().isEmpty()) {
			Grid grid = new Grid(1, 1);
			grid.setWidget(0, 0, new Label("No data found."));
			return grid;
		}

		JSONValue jsonValue = JSONParser.parseLenient(json);
		JSONObject array = jsonValue.isObject();
		Grid grid = new Grid(array.keySet().size(), 2);
		int row = 0;
		for (String key : array.keySet()) {
			grid.setWidget(row, 0, new Label(key));
			String value = array.get(key).isString().toString();
			// strip quotes from around value
			value = value.replace("\"", "");
			grid.setWidget(row, 1, new Label(value));
			row++;
		}
		return grid;
	}

	public void setLocation(Double lat, Double lon) {
		this.lat = lat;
		this.lon = lon;
		NumberFormat decimalFormat = NumberFormat.getFormat(".###");

		changesAt.setText("Data at " + decimalFormat.format(lat) + " °North, "
				+ decimalFormat.format(lon) + " °East");

		// get data at this location
		IfGovServiceAsync service = GWT.create(IfGovService.class);
		service.getData(lat, lon, new AsyncCallback<DataDto>() {
			@Override
			public void onFailure(Throwable arg0) {
				throw new RuntimeException(arg0);
			}

			@Override
			public void onSuccess(DataDto result) {
				dataFromServer = result;
				dataPanel.clear();
				if (result == null) {
					dataPanel.add(new Label("No data found."));
				} else {
					// todo parse the response show as a nice table

					// JSONValue jsonValue = JSONParser.parseLenient(result);
					// JSONObject array = jsonValue.isObject();
					Grid broadband = getGridFromJson(result.getBroadband());
					// int row = 0;
					// for (String key : array.keySet()) {
					// data.setWidget(row, 0, new Label(key));
					// String value = array.get(key).isString().toString();
					// data.setWidget(row, 1, new Label(value));
					// row++;
					// }
					dataPanel
							.add(new HTML(
									"<label><a href='https://www.mybroadband.communications.gov.au/'>Broadband</a></label>"));
					dataPanel.add(broadband);
				}

			}
		});

	}
}
