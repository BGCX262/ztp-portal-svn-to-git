package sk.stuba.fiit.ztpPortal.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import sk.stuba.fiit.ztpPortal.databaseController.CountyController;
import sk.stuba.fiit.ztpPortal.databaseModel.Country;
import sk.stuba.fiit.ztpPortal.databaseModel.County;

public class RegionSelectorView implements Serializable{

	private static final long serialVersionUID = 1L;

	private static Map countyMap = new HashMap(); // map:company->model

	private County okres;
	private Country kraj;
	
	private String selectedCountry;
	private String selectedCounty;
	
	private String myCounty;
	
	public String getMyCounty() {
		return myCounty;
	}

	DropDownChoice country;
	DropDownChoice county;

	public String getSelectedCountry() {
		return selectedCountry;
	}

	public void setSelectedCountry(String selectedCountry) {
		this.selectedCountry = selectedCountry;
	}
	
	public String getSelectedCounty() {
		return selectedCounty;
	}

	public void setSelectedCounty(String selectedCounty) {
		this.selectedCounty = selectedCounty;
	}

	private static Map getMapping(){
		CountyController countyController = new CountyController();
		countyMap.put("Nitriansky kraj", Arrays.asList(countyController
				.getCountyListByCountry("Nitriansky kraj")));
		countyMap.put("Bratislavsk� kraj", Arrays.asList(countyController
				.getCountyListByCountry("Bratislavsk� kraj")));
		countyMap.put("Trnavsk� kraj", Arrays.asList(countyController
				.getCountyListByCountry("Trnavsk� kraj")));
		countyMap.put("Tren�iansky kraj", Arrays.asList(countyController
				.getCountyListByCountry("Tren�iansky kraj")));
		countyMap.put("Banskobystrick� kraj", Arrays.asList(countyController
				.getCountyListByCountry("Banskobystrick� kraj")));
		countyMap.put("�ilinsk� kraj", Arrays.asList(countyController
				.getCountyListByCountry("�ilinsk� kraj")));
		countyMap.put("Pre�ovsk� kraj", Arrays.asList(countyController
				.getCountyListByCountry("Pre�ovsk� kraj")));
		countyMap.put("Ko�ick� kraj", Arrays.asList(countyController
				.getCountyListByCountry("Ko�ick� kraj")));
		
		return countyMap;
	}
	
	public RegionSelectorView(County concreteOkres) {
		
		selectedCountry = concreteOkres.getCountry().getName();
		myCounty=concreteOkres.getName();
			
		getMapping();
		
//		CountyController countyController = new CountyController();
//		countyMap.put("Nitriansky kraj", Arrays.asList(countyController
//				.getCountyListByCountry("Nitriansky kraj")));
//		countyMap.put("Bratislavsk� kraj", Arrays.asList(countyController
//				.getCountyListByCountry("Bratislavsk� kraj")));
//		countyMap.put("Trnavsk� kraj", Arrays.asList(countyController
//				.getCountyListByCountry("Trnavsk� kraj")));
//		countyMap.put("Tren�iansky kraj", Arrays.asList(countyController
//				.getCountyListByCountry("Tren�iansky kraj")));
//		countyMap.put("Banskobystrick� kraj", Arrays.asList(countyController
//				.getCountyListByCountry("Banskobystrick� kraj")));
//		countyMap.put("�ilinsk� kraj", Arrays.asList(countyController
//				.getCountyListByCountry("�ilinsk� kraj")));
//		countyMap.put("Pre�ovsk� kraj", Arrays.asList(countyController
//				.getCountyListByCountry("Pre�ovsk� kraj")));
//		countyMap.put("Ko�ick� kraj", Arrays.asList(countyController
//				.getCountyListByCountry("Ko�ick� kraj")));

		IModel countryChoices = new AbstractReadOnlyModel() {
			public Object getObject() {
				Set keys = countyMap.keySet();
				List list = new ArrayList(keys);
				return list;
			}

		};

		IModel countyChoices = new AbstractReadOnlyModel() {
			public Object getObject() {
				List models = (List) countyMap.get(selectedCountry);
				if (models == null) {
					models = Collections.EMPTY_LIST;
				}
				return models;
			}

		};
		
		kraj = new Country();
		kraj.setName(concreteOkres.getCountry().getName());
		
		country = new DropDownChoice("country", new PropertyModel(kraj,
				"name"), countryChoices);
		
		okres = new County();
		okres.setName(concreteOkres.getName());
		
		county = new DropDownChoice("county", new PropertyModel(okres, 
				"name"), countyChoices);

		selectedCountry=concreteOkres.getCountry().getName();
		
		country.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			protected void onUpdate(AjaxRequestTarget target) {
				target.addComponent(county);
				selectedCountry = kraj.getName();
				
			}
		});
		
		county.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			protected void onUpdate(AjaxRequestTarget target) {
				myCounty=okres.getName();
			}
		});

		country.setOutputMarkupId(true);
		county.setOutputMarkupId(true);
	}

	public DropDownChoice getCountryDropDownChoice() {
		return country;
	}

	public DropDownChoice getCountyDropDownChoice() {
		return county;
	}

}
