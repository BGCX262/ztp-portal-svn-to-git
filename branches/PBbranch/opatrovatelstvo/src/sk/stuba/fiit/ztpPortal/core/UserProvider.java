package sk.stuba.fiit.ztpPortal.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import sk.stuba.fiit.ztpPortal.databaseController.JobController;
import sk.stuba.fiit.ztpPortal.databaseModel.Job;

public class UserProvider extends SortableDataProvider {

    List<Job> list = new JobController().getAllJob();

    public UserProvider() {
        // important or you'll get a null pointer on line 40
        setSort("creationDate", true);

//        list.add(new Contact(new Name("Abby", "Zerind")));
//        list.add(new Contact(new Name("Bernard", "Youst")));
//        list.add(new Contact(new Name("Charlie", "Xerg")));
//        list.add(new Contact(new Name("Deitri", "West")));
//        list.add(new Contact(new Name("Ernie", "Vuntang")));
//        list.add(new Contact(new Name("Frank", "Unter")));
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

class Contact {

    private final Name name;

    public Contact(Name name) {
        this.name = name;
    }

    public Name getName() {
        return name;
    }
}

class Name {

    private String firstName;
    private String lastName;

    public Name(String fName, String lName) {
        firstName = fName;
        lastName = lName;
    }

    public String getFirst() {
        return firstName;
    }

    public void setFirst(String firstName) {
        this.firstName = firstName;
    }

    public String getLast() {
        return lastName;
    }

    public void setLast(String lastName) {
        this.lastName = lastName;
    }
}
