package sk.stuba.fiit.ztpPortal.module.accomodation;

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

import sk.stuba.fiit.ztpPortal.databaseController.LivingController;
import sk.stuba.fiit.ztpPortal.databaseModel.County;
import sk.stuba.fiit.ztpPortal.databaseModel.Living;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class LivingProvider extends SortableDataProvider implements
		IFilterStateLocator {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Living> list = new LivingController().getAllActiveLiving();
	
	private Living filter = new Living();
	
	private County preferredCounty = null;

	private boolean prefer;
	
	private RegisteredUser livingOwner;

	private int strlen;

	public LivingProvider() {
		setSort("createDate", true);
	}

	public void setUserPreferredCountyFilter(County county){
		preferredCounty = county;
	}
	
	public void setUserPreferredCounty(boolean prefer){
		this.prefer = prefer;
	}
	
	public RegisteredUser getLivingOwner() {
		return livingOwner;
	}

	public void setLivingOwner(RegisteredUser LivingOwner) {
		this.livingOwner = LivingOwner;
	}

	public Iterator<Living> iterator(int first, int count) {
		List<Living> newList = new ArrayList<Living>();
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
		
		for (final Living living : list) {
			
//			strlen=living.getName().length();
//			if(strlen>20) {
//				strlen=20;
//				living.setName(living.getName().substring(0,strlen)+"...");
//			}
			
			if ((filter.getLivingType() != null)
					&& !living.getLivingType().getName().toLowerCase().contains(
							filter.getLivingType().getName().toLowerCase())) {
				newList.remove(living);
			}
			
			if ((filter.getOwner()!=null) && (filter.getOwner().getLogin() != null)
					&& !living.getOwner().getLogin().toLowerCase().contains(
							filter.getOwner().getLogin().toLowerCase())) {
				newList.remove(living);
			}
			
			if ((filter.getTown() != null)
					&& !living.getTown().toLowerCase().contains(
							filter.getTown().toLowerCase())) {
				newList.remove(living);
			}
			
			/// preferencia okresu
			if (prefer && !living.getCounty().getName().equals(preferredCounty.getName())) {
				newList.remove(living);
			}
			
			// ak je deaktivovany prispevok userom, moze ho vidiet iba dotycny user
			if (livingOwner==null){
				if (!living.isActive() && living.getOwner()!=livingOwner)
					newList.remove(living);
			}
			
			if ((livingOwner==null && !living.isActive())){
				newList.remove(living);
			}
			
			if ((livingOwner==null && !living.isActive()) && living.getOwner()!=livingOwner){
				newList.remove(living);
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
		filter = (Living) arg0;

	}

}
