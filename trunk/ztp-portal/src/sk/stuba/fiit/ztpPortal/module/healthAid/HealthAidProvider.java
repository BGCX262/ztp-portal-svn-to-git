package sk.stuba.fiit.ztpPortal.module.healthAid;

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

import sk.stuba.fiit.ztpPortal.databaseController.HealthAidController;
import sk.stuba.fiit.ztpPortal.databaseModel.HealthAid;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class HealthAidProvider extends SortableDataProvider implements
		IFilterStateLocator {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<HealthAid> list = new HealthAidController().getAllActiveHealthAid();
	
	private HealthAid filter = new HealthAid();
	
	private RegisteredUser livingOwner;

	private int strlen;

	public HealthAidProvider() {
		setSort("createDate", true);
	}
	
	public RegisteredUser getLivingOwner() {
		return livingOwner;
	}

	public void setLivingOwner(RegisteredUser LivingOwner) {
		this.livingOwner = LivingOwner;
	}

	public Iterator<HealthAid> iterator(int first, int count) {
		List<HealthAid> newList = new ArrayList<HealthAid>();
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
		
		for (final HealthAid healthAid : list) {
			
			strlen=healthAid.getName().length();
			if(strlen>20) {
				strlen=20;
				healthAid.setName(healthAid.getName().substring(0,strlen)+"...");
			}
			if ((filter.getName() != null)
					&& !healthAid.getName().toLowerCase().contains(
							filter.getName().toLowerCase())) {
				newList.remove(healthAid);
			}
			
			if ((filter.getOwner()!=null) && (filter.getOwner().getLogin() != null)
					&& !healthAid.getOwner().getLogin().toLowerCase().contains(
							filter.getOwner().getLogin().toLowerCase())) {
				newList.remove(healthAid);
			}
			
			
			// ak je deaktivovany prispevok userom, moze ho vidiet iba dotycny user
			if (livingOwner==null){
				if (!healthAid.isActive() && healthAid.getOwner()!=livingOwner)
					newList.remove(healthAid);
			}
			
			if ((livingOwner==null && !healthAid.isActive())){
				newList.remove(healthAid);
			}
			
			if ((livingOwner==null && !healthAid.isActive()) && healthAid.getOwner()!=livingOwner){
				newList.remove(healthAid);
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
		filter = (HealthAid) arg0;

	}

}
