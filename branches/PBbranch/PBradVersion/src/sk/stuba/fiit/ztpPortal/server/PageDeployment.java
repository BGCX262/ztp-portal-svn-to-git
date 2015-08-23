package sk.stuba.fiit.ztpPortal.server;

public interface PageDeployment {

	public boolean unDeployPage();
	
	public String deployPage();
	
	public String createNewPage(String text);
	
	public void setPath(String path);
	
	public String getPath();
}
