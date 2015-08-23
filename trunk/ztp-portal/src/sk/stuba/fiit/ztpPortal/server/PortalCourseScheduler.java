package sk.stuba.fiit.ztpPortal.server;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import sk.stuba.fiit.ztpPortal.databaseController.CourseController;
import sk.stuba.fiit.ztpPortal.databaseController.GlobalSettingController;
import sk.stuba.fiit.ztpPortal.databaseModel.Course;
import sk.stuba.fiit.ztpPortal.databaseModel.GlobalSetting;

/**
 * Scheduler pre skript deaktivacie Kurzov
 * 
 * @author Peter Bradac
 * 
 */
public class PortalCourseScheduler implements Job {
	
	private static final Log log = LogFactory.getLog(PortalCourseScheduler.class);
	private CourseController courseController;
	private int dayCount;
	private GlobalSetting deactivationSetting;

	public void execute(JobExecutionContext arg0) throws JobExecutionException {

		courseController = new CourseController();
		Date now = new Date();
		deactivationSetting = new GlobalSettingController()
				.getSettingByName("jobDeactivation");
		dayCount = Integer.parseInt(deactivationSetting.getValue());

		List<Course> courseList = courseController.getAllActiveCourse(); //pracuje sa so vsetkymi aktivnymi

		for (Course course : courseList) {

			// mazanie ked je state = true
			if ((dayCount != 0) && course.isActive()) {

				Date createDate = course.getCreateDate();

				if (now.getTime() - createDate.getTime() > dayCount * 86400000) { // ak
																					// presiahol
																					// cas
																					// pocet
																					// dni
					course.setActive(false); // deaktivujem
					courseController.updateCourse(course); // jasne
					log.info("Deaktivacia active COURSE schedulerom "
							+ course.getName());
				}
			}

			// mazanie ked je state = false, pouzivatel ju nastavil
			if (!course.isActive()) {

				Date changeDate = course.getChangeDate();

				if (now.getTime() - changeDate.getTime() > dayCount * 86400000) { // ak
																					// presiahol
																					// cas
																					// pocet
																					// dni
					course.setState(false); // deaktivujem
					courseController.updateCourse(course); // jasne
					log.info("Deaktivacia state COURSE schedulerom "
							+ course.getName());
				}
			}

		}

	}
}