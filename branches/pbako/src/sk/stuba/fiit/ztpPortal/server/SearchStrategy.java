package sk.stuba.fiit.ztpPortal.server;

import java.util.List;

import sk.stuba.fiit.ztpPortal.databaseModel.SearchResultList;

public interface SearchStrategy {

	public List<SearchResultList> searchData(String searchString);
	
}
