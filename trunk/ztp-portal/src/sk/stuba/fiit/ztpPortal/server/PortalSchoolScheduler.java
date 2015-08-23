package sk.stuba.fiit.ztpPortal.server;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import sk.stuba.fiit.ztpPortal.databaseController.GlobalSettingController;
import sk.stuba.fiit.ztpPortal.databaseController.SchoolController;
import sk.stuba.fiit.ztpPortal.databaseModel.GlobalSetting;
import sk.stuba.fiit.ztpPortal.databaseModel.School;

/**
 * Scheduler pre skript deaktivacie Skol
 * 
 * @author Peter Bradac
 * 
 */
public class PortalSchoolScheduler implements Job {

	private static final Log log = LogFactory.getLog(PortalSchoolScheduler.class);
	private SchoolController schoolController;
	private int dayCount;
	private GlobalSetting deactivationSetting;

	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		schoolController = new SchoolController();
		Date now = new Date();
		deactivationSetting = new GlobalSettingController()
				.getSettingByName("jobDeactivation");
		dayCount = Integer.parseInt(deactivationSetting.getValue());

		System.out.println("Scheduler invoked in COURSE module at " + now);

		List<School> courseList = schoolController.getAllActiveSchool(); // pracuje
																			// sa
																			// so
																			// vsetkymi
																			// aktivnymi

		for (School school : courseList) {

			// mazanie ked je state = true
			if ((dayCount != 0) && school.isActive()) {

				Date createDate = school.getCreateDate();

				if (now.getTime() - createDate.getTime() > dayCount * 86400000) { // ak
					// presiahol
					// cas
					// pocet
					// dni
					school.setActive(false); // deaktivujem
					schoolController.updateSchool(school); // jasne
					log.info("Deaktivacia active SCHOOL schedulerom "
							+ school.getName());
				}
			}

			// mazanie ked je state = false, pouzivatel ju nastavil
			if (!school.isActive()) {

				Date changeDate = school.getChangeDate();

				if (now.getTime() - changeDate.getTime() > dayCount * 86400000) { // ak
					// presiahol
					// cas
					// pocet
					// dni
					school.setState(false); // deaktivujem
					schoolController.updateSchool(school); // jasne
					log.info("Deaktivacia state SCHOOL schedulerom "
							+ school.getName());
				}
			}

		}

	}
}