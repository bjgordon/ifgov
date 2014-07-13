package com.ifgov.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.base.LatLngBounds;
import com.google.gwt.maps.client.events.bounds.BoundsChangeMapEvent;
import com.google.gwt.maps.client.events.bounds.BoundsChangeMapHandler;
import com.google.gwt.maps.client.events.click.ClickMapEvent;
import com.google.gwt.maps.client.events.click.ClickMapHandler;
import com.google.gwt.maps.client.events.dragend.DragEndMapEvent;
import com.google.gwt.maps.client.events.dragend.DragEndMapHandler;
import com.google.gwt.maps.client.events.place.PlaceChangeMapEvent;
import com.google.gwt.maps.client.events.place.PlaceChangeMapHandler;
import com.google.gwt.maps.client.overlays.Animation;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.maps.client.placeslib.Autocomplete;
import com.google.gwt.maps.client.placeslib.AutocompleteOptions;
import com.google.gwt.maps.client.placeslib.AutocompleteType;
import com.google.gwt.maps.client.placeslib.PlaceGeometry;
import com.google.gwt.maps.client.placeslib.PlaceResult;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class MapComposite extends Composite {

	private FlowPanel pWidget;

	private MapWidget mapWidget;

	private TextBox tbPlaces = new TextBox();

	DataPanel dataPopup;

	public MapComposite() {

		pWidget = new FlowPanel();

		initWidget(pWidget);

		pWidget.setSize("100%", "100%");
		// pWidget.addStyleName("test3");

		draw();

		Timer t = new Timer() {
			@Override
			public void run() {
				// popupPanel.center();
				// popupPanel.show();

				// SubscribePopup subscribePopup = new SubscribePopup(1.0, 2.0);
				// subscribePopup.show();

				// Start centred on Canberra
				LatLng canberra = LatLng.newInstance(-35.2819998,
						149.12868430000003);
				mapWidget.panTo(canberra);
				mapWidget.setZoom(8);
				dropMarker(canberra);

				showAboutPopup();

			}
		};

		t.schedule(200);

	}

	private void draw() {

		drawMap();
		drawAutoComplete();

	}

	private void drawMap() {
		LatLng center = LatLng.newInstance(-35.2819998, 149.12868430000003);
		;
		MapOptions opts = MapOptions.newInstance();
		opts.setZoom(4);
		opts.setCenter(center);
		opts.setMapTypeId(MapTypeId.HYBRID);

		mapWidget = new MapWidget(opts);
		pWidget.add(mapWidget);
		mapWidget.setSize("100%", "100%");

		mapWidget.addClickHandler(new ClickMapHandler() {
			public void onEvent(ClickMapEvent event) {
				// TODO fix the event getting, getting ....
				GWT.log("clicked on latlng="
						+ event.getMouseEvent().getLatLng());
			}
		});
	}

	private void drawAutoComplete() {

		Element element = tbPlaces.getElement();

		AutocompleteType[] types = new AutocompleteType[2];
		types[0] = AutocompleteType.ESTABLISHMENT;
		types[1] = AutocompleteType.GEOCODE;

		AutocompleteOptions options = AutocompleteOptions.newInstance();
		options.setTypes(types);
		options.setBounds(mapWidget.getBounds());

		final Autocomplete autoComplete = Autocomplete.newInstance(element,
				options);

		autoComplete.addPlaceChangeHandler(new PlaceChangeMapHandler() {
			public void onEvent(PlaceChangeMapEvent event) {

				PlaceResult result = autoComplete.getPlace();

				if (result == null) {
					return;
				}

				PlaceGeometry geomtry = result.getGeometry();
				LatLng center = geomtry.getLocation();

				if (center == null) {

					// todo better way to handle this?
					return;
				}
				mapWidget.panTo(center);
				mapWidget.setZoom(15);

				GWT.log("place changed center=" + center);
				dropMarker(center);
			}
		});

		mapWidget.addBoundsChangeHandler(new BoundsChangeMapHandler() {
			public void onEvent(BoundsChangeMapEvent event) {
				LatLngBounds bounds = mapWidget.getBounds();
				autoComplete.setBounds(bounds);
			}
		});

		// Position the Autocomplete on top of the map
		FlowPanel flowPanel = new FlowPanel();
		flowPanel.add(new Label("Search for a location, or drag the marker."));
		flowPanel.add(tbPlaces);

		tbPlaces.setWidth("300px");
		flowPanel.getElement().getStyle().setPosition(Position.ABSOLUTE);
		flowPanel.getElement().getStyle().setTop(6, Unit.PX);
		flowPanel.getElement().getStyle().setLeft(100, Unit.PX);
		flowPanel.getElement().getStyle().setBackgroundColor("#fafafc");
		flowPanel.getElement().getStyle().setPadding(10, Unit.PX);
		flowPanel.addStyleName("rounded-panel");
		pWidget.add(flowPanel);
	}

	private Marker marker;

	void dropMarker(LatLng latLng) {
		if (marker != null) {
			marker.setMap((MapWidget) null);

		}
		MarkerOptions options = MarkerOptions.newInstance();
		options.setPosition(latLng);
		options.setAnimation(Animation.DROP);
		options.setDraggable(true);

		marker = Marker.newInstance(options);

		marker.setMap(mapWidget);
		marker.addDragEndHandler(new DragEndMapHandler() {

			@Override
			public void onEvent(DragEndMapEvent event) {
				pinMoved();
			}
		});

		pinMoved();
	}

	void pinMoved() {
		if (dataPopup == null) {
			dataPopup = new DataPanel();
			RootPanel.get().add(dataPopup);
		}

		dataPopup.setLocation(marker.getPosition().getLatitude(), marker
				.getPosition().getLongitude());
	}

	void showAboutPopup() {
		AboutPopup aboutPopup = new AboutPopup();
		aboutPopup.show();
	}
}
