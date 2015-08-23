package sk.stuba.fiit.ztpPortal.module.information;

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

import sk.stuba.fiit.ztpPortal.databaseController.InformationController;
import sk.stuba.fiit.ztpPortal.databaseModel.Information;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class InfoProvider extends SortableDataProvider implements
		IFilterStateLocator {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Information> list = new InformationController().getAllActiveInformation();
	
	private Information filter = new Information();
	
	private RegisteredUser livingOwner;

	private int strlen;

	public InfoProvider() {
		setSort("createDate", true);
	}
	
	public RegisteredUser getLivingOwner() {
		return livingOwner;
	}

	public void setLivingOwner(RegisteredUser LivingOwner) {
		this.livingOwner = LivingOwner;
	}

	public Iterator<Information> iterator(int first, int count) {
		List<Information> newList = new ArrayList<Information>();
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
		
		for (final Information information : list) {
			
			strlen=information.getName().length();
			if(strlen>20) {
				strlen=20;
				information.setName(information.getName().substring(0,strlen)+"...");
			}
			if ((filter.getName() != null)
					&& !information.getName().toLowerCase().contains(
							filter.getName().toLowerCase())) {
				newList.remove(information);
			}
			
			if ((filter.getOwner()!=null) && (filter.getOwner().getLogin() != null)
					&& !information.getOwner().getLogin().toLowerCase().contains(
							filter.getOwner().getLogin().toLowerCase())) {
				newList.remove(information);
			}
			
			// ak je deaktivovany prispevok userom, moze ho vidiet iba dotycny user
			if (livingOwner==null){
				if (!information.isActive() && information.getOwner()!=livingOwner)
					newList.remove(information);
			}
			
			if ((livingOwner==null && !information.isActive())){
				newList.remove(information);
			}
			
			if ((livingOwner==null && !information.isActive()) && information.getOwner()!=livingOwner){
				newList.remove(information);
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
		filter = (Information) arg0;

	}

}
