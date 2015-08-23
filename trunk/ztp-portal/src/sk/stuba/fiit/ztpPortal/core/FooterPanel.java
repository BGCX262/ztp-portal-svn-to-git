package sk.stuba.fiit.ztpPortal.core;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class FooterPanel extends Panel{

	private static final long serialVersionUID = 1L;
	private Label yearLabel;

	public FooterPanel(String id) {
		super(id);
		
		Calendar cal = new GregorianCalendar();
		cal.setTime(new Date());
		String year = String.valueOf(cal.get(Calendar.YEAR));
		
		yearLabel = new Label ("yearLabel",year);
		add(yearLabel);
		
	}

}
