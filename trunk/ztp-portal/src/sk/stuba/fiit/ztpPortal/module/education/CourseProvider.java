package sk.stuba.fiit.ztpPortal.module.education;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.IFilterStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import sk.stuba.fiit.ztpPortal.databaseController.CourseController;
import sk.stuba.fiit.ztpPortal.databaseModel.County;
import sk.stuba.fiit.ztpPortal.databaseModel.Course;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class CourseProvider extends SortableDataProvider implements
IFilterStateLocator{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Course> list = new CourseController().getAllActiveCourse();
	
	private Course filter = new Course();
	
	private County preferredTown = null;

	private boolean prefer;
	
	private RegisteredUser CourseOwner;

	private int strlen;

	public CourseProvider() {
		setSort("createDate", true);
	}
	
	public void setUserPreferredTownFilter(County town){
		preferredTown = town;
	}
	
	public void setUserPreferTown(boolean prefer){
		this.prefer = prefer;
	}
	
	public RegisteredUser getCourseOwner() {
		return CourseOwner;
	}

	public void setCourseOwner(RegisteredUser CourseOwner) {
		this.CourseOwner = CourseOwner;
	}
	
	
	public Iterator<Course> iterator(int first, int count) {
		List<Course> newList = new ArrayList<Course>();
		newList.addAll(list.subList(first, first + count));

		final String sortColumn = this.getSort().getProperty();
		final boolean ascending = this.getSort().isAscending();

		Collections.sort(newList, new Comparator<Object>() {

			public int compare(Object obj1, Object obj2) {
				PropertyModel model1 = new PropertyModel(obj1, sortColumn);
				PropertyModel model2 = new PropertyModel(obj2, sortColumn);

				Object modelObject1 = model1.getObject();
				Object modelObject2 = model2.getObject();

				int compare = ((Comparable<Object>) modelObject1)
						.compareTo(modelObject2);

				if (!ascending)
					compare *= -1;

				return compare;
			}
		});
		
		for (final Course course : list) {
			
			strlen=course.getName().length();
			if(strlen>20) {
				strlen=20;
				course.setName(course.getName().substring(0,strlen)+"...");
			}
			if ((filter.getName() != null)
					&& !course.getName().toLowerCase().contains(
							filter.getName().toLowerCase())) {
				newList.remove(course);
			}
			
			if ((filter.getOwner()!=null) &&
					(filter.getOwner()!=null) && (filter.getOwner().getLogin() != null)
					&& !course.getOwner().getName().toLowerCase().contains(
							filter.getOwner().getName().toLowerCase())) {
				newList.remove(course);
			}
			
			if ((filter.getPrice()!=0) && (filter.getPrice()> course.getPrice())){
				newList.remove(course);
			}
			
			if ((filter.getTown() != null)
					&& !course.getTown().toLowerCase().contains(
							filter.getTown().toLowerCase())) {
				newList.remove(course);
			}
			
			if ((filter.getCourseType() != null)
					&& !course.getCourseType().getName().toLowerCase().contains(
							filter.getCourseType().getName().toLowerCase())) {
				newList.remove(course);
			}
			
			if ((filter.getCounty() != null)
					&& !course.getCounty().getName().toLowerCase().contains(
							filter.getCounty().getName().toLowerCase())) {
				newList.remove(course);
			}
			
			/// preferencia mesta
			if (prefer && !course.getCounty().getName().equals(preferredTown.getName())) newList.remove(course);
			
			// ak je deaktivovany prispevok userom, moze ho vidiet iba dotycny user
			if (CourseOwner==null){
				if (!course.isActive() && course.getOwner()!=CourseOwner)
					newList.remove(course);
			}
			
			if ((CourseOwner==null && !course.isActive())){
				newList.remove(course);
			}
			
			if ((CourseOwner==null && !course.isActive()) && course.getOwner()!=CourseOwner){
				newList.remove(course);
			}
			
		}

		return newList.iterator();
	}

	public int size() {
		return list.size();
	}

	public IModel model(final Object object) {
		return new AbstractReadOnlyModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Object getObject() {
				return object;
			}
		};
	}

	public Object getFilterState() {
		// TODO Auto-generated method stub
		return filter;
	}

	public void setFilterState(Object arg0) {
		filter = (Course) arg0;

	}

}
