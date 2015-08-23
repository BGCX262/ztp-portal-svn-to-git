package sk.stuba.fiit.ztpPortal.server;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Trieda zabezpecujuca styly CSS pre stranky
 * @author Peter Bradac
 *
 */
public class Style {

	public static final String NORMAL = "./style/webStyle.css";
	public static final String NORMAL_WINTER = "./style/webStyleWinter.css";
	public static final String NORMAL_SPRING = "./style/webStyleSpring.css";
	public static final String NORMAL_SUMMER = "./style/webStyleSummer.css";
	public static final String NORMAL_AUTUMN = "./style/webStyleAutumn.css";
	public static final String HIGH_CONTRAST = "./style/webStyleContrast.css";
	public static final String SIMPLE = "./style/webStyleSimple.css";

	public String getNormalSeasonStyle(){
		
		GregorianCalendar cal;
		try{
		cal= new GregorianCalendar();
		cal.setTime(new Date());
		
		if ((cal.MONTH>1) & (cal.MONTH<5)) return NORMAL_SPRING;
		if ((cal.MONTH>4) & (cal.MONTH<8)) return NORMAL_SUMMER;
		if ((cal.MONTH>7) & (cal.MONTH<11)) return NORMAL_AUTUMN;
		return NORMAL_WINTER;
		
		}finally{
			cal = null;
		}
	}
	
}
