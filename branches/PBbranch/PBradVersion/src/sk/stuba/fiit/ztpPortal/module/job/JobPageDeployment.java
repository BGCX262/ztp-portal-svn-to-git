package sk.stuba.fiit.ztpPortal.module.job;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import sk.stuba.fiit.ztpPortal.server.PageDeployment;

public class JobPageDeployment implements PageDeployment {

	File file;
	String path;
	
	
	public boolean unDeployPage(){
		return false;	
	}
	
	public String deployPage() {
		
		// TODO Auto-generated method stub
		return null;
	}

	public String createNewPage(String text) {

		String name="page.html";
		
		file= new File(path + File.separator + "JobPage" + File.separator + name);
		
		createFile(text,file);
		
		return file.getAbsolutePath();
	}

	public String getPath() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setPath(String path) {
		this.path=path;
	}

	private void createFile(String text, File fos){
		BufferedWriter out =null;
		try {
			out = new BufferedWriter(new FileWriter(fos));
			String begin="<html>"+
"<head>"+
"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
"<title>Job Page</title>"+
"</head>"+
"<body>"+
"User entered: </br>"+
"<p>";
			
			String end="</p>"+
"</body>"+
"</html>";
			
			out.write(begin);
			out.write(text);
			out.write(end);
			out.flush();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		finally {
			try {
				out.flush();
				if (out != null) out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	
}
