package sk.stuba.fiit.ztpPortal.module.dayCare;

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

import sk.stuba.fiit.ztpPortal.databaseController.DayCareController;
import sk.stuba.fiit.ztpPortal.databaseModel.County;
import sk.stuba.fiit.ztpPortal.databaseModel.DayCare;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class DayCareProvider extends SortableDataProvider implements
		IFilterStateLocator {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<DayCare> list = new DayCareController().getAllActiveDayCares();
	private DayCare filter = new DayCare();
	
	private County preferredCounty = null;

	private boolean prefer;
	
	private RegisteredUser dayCareOwner;

	public DayCareProvider() {
		setSort("creationDate", true);
	}

	public void setUserPreferredTownFilter(County town){
		preferredCounty = town;
	}
	
	public void setUserPreferTown(boolean prefer){
		this.prefer = prefer;
	}
	
	public RegisteredUser getDayCareOwner() {
		return dayCareOwner;
	}

	public void setDayCareOwner(RegisteredUser dayCareOwner) {
		this.dayCareOwner = dayCareOwner;
	}

	public Iterator<DayCare> iterator(int first, int count) {
		List<DayCare> newList = new ArrayList<DayCare>();
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
		int strlen=0;
		for (final DayCare dayCare : list) {
			strlen=dayCare.getDescription().length();
			if(strlen>20) {
				strlen=20;
				dayCare.setDescription(dayCare.getDescription().substring(0,strlen)+"...");
			}
			if ((filter.getAdvertType() != null)
					&& !dayCare.getAdvertType().getName().toLowerCase().contains(
							filter.getAdvertType().getName().toLowerCase())) {
				newList.remove(dayCare);
			}
			
			if ((filter.getShortDesc() != null)
					&& !dayCare.getShortDesc().toLowerCase().contains(
							filter.getShortDesc().toLowerCase())) {
				newList.remove(dayCare);
			}
			
			if ((filter.getCounty() != null)
					&& !dayCare.getCounty().getName().toLowerCase().contains(
							filter.getCounty().getName().toLowerCase())) {
				newList.remove(dayCare);
			}
			
			if ((filter.getTown() != null)
					&& !dayCare.getTown().toLowerCase().contains(
							filter.getTown().toLowerCase())) {
				newList.remove(dayCare);
			}
			
			if ((filter.getCreator() != null)
					&& !dayCare.getCreator().getLogin().toLowerCase().contains(
							filter.getCreator().getLogin().toLowerCase())) {
				newList.remove(dayCare);
			}
			
			// preferencia mesta
			if (dayCare.getCounty()!=null)
				if (prefer && !dayCare.getCounty().getName().equals(preferredCounty.getName())) newList.remove(dayCare);
			
			// ak je deaktivovany prispevok userom, moze ho vidiet iba dotycny user
//			if (jobOwner!=null){
//				if (!job.isActive() && job.getCreator()!=jobOwner)
//				
//			}
			
			if ((dayCareOwner==null && !dayCare.isActive())){
				newList.remove(dayCare);
			}
			
			if ((dayCareOwner==null && !dayCare.isActive()) && dayCare.getCreator()!=dayCareOwner){
				newList.remove(dayCare);
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
		filter = (DayCare) arg0;
	}

}
