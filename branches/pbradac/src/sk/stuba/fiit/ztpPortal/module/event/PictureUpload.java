/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sk.stuba.fiit.ztpPortal.module.event;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.lang.Bytes;

import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.databaseController.PictureController;
import sk.stuba.fiit.ztpPortal.databaseModel.Event;
import sk.stuba.fiit.ztpPortal.databaseModel.Picture;


/**
 * Upload example.
 * 
 * @author Eelco Hillenius
 */
public class PictureUpload extends CorePage
{

	/**
	 * Form for uploads.
	 */
	private class FileUploadForm extends Form
	{
		// collection that will hold uploaded FileUpload objects
		private final Collection uploads = new ArrayList();
		private Event event;

		/**
		 * TODO
		 * 
		 * @return Collection
		 */
		public Collection getUploads()
		{
			return uploads;
		}

		/**
		 * Construct.
		 * 
		 * @param name
		 *            Component name
		 */
		public FileUploadForm(String name,Event event)
		{
			super(name);
			
			this.event=event;

			// set this form to multipart mode (allways needed for uploads!)
			setMultiPart(true);

			// Add one multi-file upload field
			add(new MultiFileUploadField("fileInput", new PropertyModel(this, "uploads"), 5));

			// Set maximum size
			setMaxSize(Bytes.kilobytes(1000));
		}

		/**
		 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
		 */
		protected void onSubmit()
		{
			Iterator it = uploads.iterator();
			while (it.hasNext())
			{
				final FileUpload upload = (FileUpload)it.next();
				// Create a new file
				File newFile = new File(getUploadFolder(), upload.getClientFileName());

				// Check new file, delete if it allready existed
				checkFileExists(newFile);
				try
				{
					// Save to new file
					newFile.createNewFile();
					upload.writeTo(newFile);
					
					
					//tvorim obrazok 
					Picture picture = new Picture();
					
					byte[] bytes = new byte[500000];
					
					DataInputStream in = new DataInputStream(new FileInputStream(newFile));
                	
	                   
                    
                    
                    // Read in the bytes
                    int offset = 0;
                    int numRead = 0;
                    
						while (offset < bytes.length
						       && (numRead=in.read(bytes, offset, bytes.length-offset)) >= 0) {
						    offset += numRead;
						    System.out.println(bytes);
						    System.out.println(" ");
						}
					
					
					picture.setImage(bytes);
					
					//not user data
					picture.setActive(true);
					picture.setState(true);
					picture.setOwner(event.getOwner());
					picture.setEvent(event);
					
					PictureController pic = new PictureController();
					pic.saveNewPicture(picture);
					

					PictureUpload.this.info("saved file: " + upload.getClientFileName());
				
					newFile.delete();
					
					feedbackModalPanel.info("V�etky s�bory boli ulo�en�. M��ete ukon�i� vkladanie.");
					
				}
				catch (Exception e)
				{
					throw new IllegalStateException("Unable to write file");
				}
			}
		}
	}

	private FeedbackPanel feedbackModalPanel;
	

	/**
	 * Constructor.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
	public PictureUpload()
	{
//		Folder uploadFolder = getUploadFolder();

		// Create feedback panels
//		final FeedbackPanel uploadFeedback = new FeedbackPanel("uploadFeedback");

		// Add uploadFeedback to the page itself
//		add(uploadFeedback);

		// Add simple upload form, which is hooked up to its feedback panel by
		// virtue of that panel being nested in the form.
//		final FileUploadForm simpleUploadForm = new FileUploadForm("simpleUpload");
//		add(simpleUploadForm);

		// Add folder view
//		add(new Label("dir", uploadFolder.getAbsolutePath()));
//		fileListView = new FileListView("fileList", new LoadableDetachableModel()
//		{
//			protected Object load()
//			{
//				return Arrays.asList(getUploadFolder().listFiles());
//			}
//		});
//		add(fileListView);
	}
	
	public FileUploadForm getFileUploadForm(Event event,FeedbackPanel feedbackModalPanel){
		this.feedbackModalPanel = feedbackModalPanel;
		final FileUploadForm simpleUploadForm = new FileUploadForm("simpleUpload",event);
		return simpleUploadForm;
		
	}
	
	
	

	/**
	 * Check whether the file allready exists, and if so, try to delete it.
	 * 
	 * @param newFile
	 *            the file to check
	 */
	private void checkFileExists(File newFile)
	{
		if (newFile.exists())
		{
			// Try to delete the file
			if (!Files.remove(newFile))
			{
				throw new IllegalStateException("Unable to overwrite " + newFile.getAbsolutePath());
			}
		}
	}

	private Folder getUploadFolder()
	{
		//return ((UploadApplication)Application.get()).getUploadFolder();
		
		Folder uploadFolder = new Folder("wicketTEMP");//new Folder(System.getProperty("java.io.tmpdir"), "wicket-uploads");
		// Ensure folder exists
		uploadFolder.mkdirs(); 
		
		System.out.println(uploadFolder.getAbsolutePath());
		
		return uploadFolder;
	}
}
