package sk.stuba.fiit.ztpPortal.admin.job;

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

import sk.stuba.fiit.ztpPortal.databaseController.JobController;
import sk.stuba.fiit.ztpPortal.databaseModel.County;
import sk.stuba.fiit.ztpPortal.databaseModel.Job;

public class JobProvider extends SortableDataProvider implements
		IFilterStateLocator {
	/**
* 
*/
	private static final long serialVersionUID = 1L;

	private List<Job> list = new JobController().getAllJob();
	private Job filter = new Job();

	private County preferredTown = null;

	private boolean prefer;

	public JobProvider() {
		setSort("creationDate", true);
	}

	public void setUserPreferredTownFilter(County town) {
		preferredTown = town;
	}

	public void setUserPreferTown(boolean prefer) {
		this.prefer = prefer;
	}

	public Iterator<Job> iterator(int first, int count) {
		List<Job> newList = new ArrayList<Job>();
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

		for (final Job job : list) {
			if ((filter.getSpecification() != null)
					&& !job.getSpecification().toLowerCase().contains(
							filter.getSpecification().toLowerCase())) {
				newList.remove(job);
			}

			if ((filter.getAdvertType() != null)
					&& !job.getAdvertType().getName().toLowerCase().contains(
							filter.getAdvertType().getName().toLowerCase())) {
				newList.remove(job);
			}

			if ((filter.getJobSector() != null)
					&& !job.getJobSector().getName().toLowerCase().contains(
							filter.getJobSector().getName().toLowerCase())) {
				newList.remove(job);
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
		filter = (Job) arg0;

	}
}
