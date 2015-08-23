package sk.stuba.fiit.ztpPortal.admin.education;

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

import sk.stuba.fiit.ztpPortal.databaseController.SchoolController;
import sk.stuba.fiit.ztpPortal.databaseModel.County;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;
import sk.stuba.fiit.ztpPortal.databaseModel.School;

public class SchoolProvider extends SortableDataProvider implements
		IFilterStateLocator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<School> list = new SchoolController().getAllSchools();

	private School filter = new School();

	private County preferredTown = null;

	private boolean prefer;

	private RegisteredUser schoolOwner;

	public SchoolProvider() {
		setSort("createDate", true);
	}

	public void setUserPreferredTownFilter(County town) {
		preferredTown = town;
	}

	public void setUserPreferTown(boolean prefer) {
		this.prefer = prefer;
	}

	public RegisteredUser getSchoolOwner() {
		return schoolOwner;
	}

	public void setSchoolOwner(RegisteredUser schoolOwner) {
		this.schoolOwner = schoolOwner;
	}

	public Iterator<School> iterator(int first, int count) {
		List<School> newList = new ArrayList<School>();
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

		for (final School school : list) {
			if ((filter.getSchoolType() != null)
					&& !school.getSchoolType().getName().toLowerCase().contains(
							filter.getSchoolType().getName().toLowerCase())) {
				newList.remove(school);
			}
			
			if ((filter.getCounty() != null)
					&& !school.getCounty().getName().toLowerCase().contains(
							filter.getCounty().getName().toLowerCase())) {
				newList.remove(school);
			}

			if ((filter.getOwner()!=null) && (filter.getOwner().getLogin()!= null)
					&& !school.getOwner().getName().toLowerCase().contains(
							filter.getOwner().getName().toLowerCase())) {
				newList.remove(school);
			}

			if ((filter.getTown() != null)
					&& !school.getTown().toLowerCase().contains(
							filter.getTown().toLowerCase())) {
				newList.remove(school);
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
		filter = (School) arg0;

	}

}
