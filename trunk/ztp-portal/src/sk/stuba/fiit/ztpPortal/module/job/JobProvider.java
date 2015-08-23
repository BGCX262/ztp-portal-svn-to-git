package sk.stuba.fiit.ztpPortal.module.job;

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
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class JobProvider extends SortableDataProvider implements
		IFilterStateLocator {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Job> list = new JobController().getAllActiveJob();
	private Job filter = new Job();
	
	private County preferredCounty = null;

	private boolean prefer;
	
	private RegisteredUser jobOwner;

	private int strlen;

	public JobProvider() {
		setSort("creationDate", true);
	}

	public void setUserPreferredTownFilter(County town){
		preferredCounty = town;
	}
	
	public void setUserPreferTown(boolean prefer){
		this.prefer = prefer;
	}
	
	public RegisteredUser getJobOwner() {
		return jobOwner;
	}

	public void setJobOwner(RegisteredUser jobOwner) {
		this.jobOwner = jobOwner;
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
			
			strlen=job.getSpecification().length();
			if(strlen>20) {
				strlen=20;
				job.setSpecification(job.getSpecification().substring(0,strlen)+"...");
			}
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
			
			if ((filter.getCounty() != null)
					&& !job.getCounty().getName().toLowerCase().contains(
							filter.getCounty().getName().toLowerCase())) {
				newList.remove(job);
			}
			
			if ((filter.getCreator() != null)
					&& !job.getCreator().getLogin().toLowerCase().contains(
							filter.getCreator().getLogin().toLowerCase())) {
				newList.remove(job);
			}
			
			/// preferencia mesta
			if (prefer && !job.getCounty().getName().equals(preferredCounty.getName())) newList.remove(job);
			
			// ak je deaktivovany prispevok userom, moze ho vidiet iba dotycny user
//			if (jobOwner!=null){
//				if (!job.isActive() && job.getCreator()!=jobOwner)
//				
//			}
			
			if ((jobOwner==null && !job.isActive())){
				newList.remove(job);
			}
			
			if ((jobOwner==null && !job.isActive()) && job.getCreator()!=jobOwner){
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
