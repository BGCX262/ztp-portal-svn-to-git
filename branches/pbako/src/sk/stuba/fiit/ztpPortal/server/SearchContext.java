package sk.stuba.fiit.ztpPortal.server;

import java.util.List;

import sk.stuba.fiit.ztpPortal.databaseModel.SearchResultList;

public class SearchContext {

	private SearchStrategy searchStrategy;
	
	public SearchContext(SearchStrategy searchStrategy){
		this.searchStrategy=searchStrategy;
	}
	
	public List<SearchResultList> execute(String searchString){
		return searchStrategy.searchData(searchString);	
	}
}
