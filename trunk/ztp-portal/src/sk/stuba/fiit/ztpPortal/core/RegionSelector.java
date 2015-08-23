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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import sk.stuba.fiit.ztpPortal.databaseController.CountyController;

public class RegionSelector implements Serializable{

	private static final long serialVersionUID = 1L;

	private static Map countyMap = new HashMap(); // map:company->model

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

	private static Map singletonObject;

	
	public static synchronized RegionSelector getSingletonObject() {
	//	return new RegionSelector();
		if (singletonObject == null) {
			singletonObject = getMapping();//new RegionSelector();
		}
		return new RegionSelector();
//		return singletonObject;
	}

//	public Object clone() throws CloneNotSupportedException {
//		throw new CloneNotSupportedException();
//		}
	
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
	
	
	private RegionSelector() {
		//CountyController countyController = new CountyController();
		
		//getMapping();

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

		country = new DropDownChoice("country", new PropertyModel(this,
				"selectedCountry"), countryChoices);
		country.setRequired(true);
		country.setLabel(new Model("Kraj"));

		county = new DropDownChoice("county", new PropertyModel(this,
				"selectedCounty"), countyChoices);
		
		//county.setRequired(true);
		county.setLabel(new Model("Okres"));

		country.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			protected void onUpdate(AjaxRequestTarget target) {
				target.addComponent(county);
			}
		});
		
		county.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			protected void onUpdate(AjaxRequestTarget target) {
				myCounty=selectedCounty;
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
