package sk.stuba.fiit.ztpPortal.admin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import sk.stuba.fiit.ztpPortal.databaseController.JobController;
import sk.stuba.fiit.ztpPortal.databaseController.RegisteredUserController;
import sk.stuba.fiit.ztpPortal.databaseModel.Job;
import sk.stuba.fiit.ztpPortal.databaseModel.RegisteredUser;

public class UserProvider extends SortableDataProvider {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	List<RegisteredUser> list = new RegisteredUserController().getAllRegisteredUser();

    public UserProvider() {
        setSort("login", true);
    }
    

    
    
    public Iterator iterator(int first, int count) {
        List newList = new ArrayList();
        newList.addAll(list.subList(first, first + count));

        final String sortColumn = this.getSort().getProperty();
        final boolean ascending = this.getSort().isAscending();

        Collections.sort(newList, new Comparator() {

            public int compare(Object obj1, Object obj2) {
                PropertyModel model1 = new PropertyModel(obj1, sortColumn);
                PropertyModel model2 = new PropertyModel(obj2, sortColumn);

                Object modelObject1 = model1.getObject();
                Object modelObject2 = model2.getObject();

                int compare = ((Comparable) modelObject1).compareTo(modelObject2);

                if (!ascending)
                    compare *= -1;

                return compare;
            }
        });

        return newList.iterator();
    }

    public int size() {
        return list.size();
    }

    public IModel model(final Object object) {
        return new AbstractReadOnlyModel() {
            public Object getObject() {
                return object;
            }
        };
    }

}
