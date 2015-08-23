package sk.stuba.fiit.ztpPortal.module.event;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.MultiFileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.file.Folder;
import org.apache.wicket.util.lang.Bytes;

import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.databaseController.PictureController;
import sk.stuba.fiit.ztpPortal.databaseModel.Event;
import sk.stuba.fiit.ztpPortal.databaseModel.Picture;

public class PictureUpload extends CorePage {

	private static final Log log = LogFactory.getLog(PictureUpload.class);
	/**
	 * Form for uploads.
	 */
	private class FileUploadForm extends Form {

		private static final long serialVersionUID = 1L;
		// collection that will hold uploaded FileUpload objects
		private final Collection uploads = new ArrayList();
		private Event event;

		/**
		 * TODO
		 * 
		 * @return Collection
		 */
		public Collection getUploads() {
			return uploads;
		}

		/**
		 * Construct.
		 * 
		 * @param name
		 *            Component name
		 */
		public FileUploadForm(String name, Event event) {
			super(name);

			this.event = event;

			// set this form to multipart mode (allways needed for uploads!)
			setMultiPart(true);

			// Add one multi-file upload field
			add(new MultiFileUploadField("fileInput", new PropertyModel(this,
					"uploads"), 5));

			// Set maximum size
			setMaxSize(Bytes.kilobytes(1000));
		}

		/**
		 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
		 */
		protected void onSubmit() {
			Iterator it = uploads.iterator();
			boolean success = false;
			while (it.hasNext()) {
				final FileUpload upload = (FileUpload) it.next();
				// Create a new file
				File newFile = new File(getUploadFolder(), upload
						.getClientFileName());

				if (!new MimetypesFileTypeMap().getContentType(newFile)
						.toString().equals("image/jpeg")) {
					feedbackModalPanel.info("S˙bor" + newFile.getName()
							+ " nie je podporovan˝ obr·zok");
					continue;
				}

				// Check new file, delete if it already existed
				checkFileExists(newFile);
				try {
					// Save to new file
					newFile.createNewFile();
					upload.writeTo(newFile);

					// tvorim obrazok
					Picture picture = new Picture();

					byte[] bytes = new byte[500000];

					DataInputStream in = new DataInputStream(
							new FileInputStream(newFile));

					// Read in the bytes
					int offset = 0;
					int numRead = 0;

					while (offset < bytes.length
							&& (numRead = in.read(bytes, offset, bytes.length
									- offset)) >= 0) {
						offset += numRead;
					}

					picture.setImage(bytes);

					// not user data
					picture.setActive(true);
					picture.setState(true);
					picture.setOwner(event.getOwner());
					picture.setEvent(event);

					PictureController pic = new PictureController();
					pic.saveNewPicture(picture);

					PictureUpload.this.info("saved file: "
							+ upload.getClientFileName());

					newFile.delete();

					success = true;
				} catch (Exception e) {
					log.warn("Unable to write file");
				}
			}
			if (success) {
				feedbackModalPanel
						.info("S˙bory boli uloûenÈ. MÙûete ukonËiù vkladanie.");
			} else
				feedbackModalPanel
						.info("Nebol uloûen˝ ûiadny obr·zok. Sk˙ste znova.");
		}
	}

	private FeedbackPanel feedbackModalPanel;

	/**
	 * Constructor.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
	public PictureUpload() {
	}

	public FileUploadForm getFileUploadForm(Event event,
			FeedbackPanel feedbackModalPanel) {
		this.feedbackModalPanel = feedbackModalPanel;
		final FileUploadForm simpleUploadForm = new FileUploadForm(
				"simpleUpload", event);
		return simpleUploadForm;

	}

	/**
	 * Check whether the file allready exists, and if so, try to delete it.
	 * 
	 * @param newFile
	 *            the file to check
	 */
	private void checkFileExists(File newFile) {
		if (newFile.exists()) {
			// Try to delete the file
			if (!Files.remove(newFile)) {
				log.warn("Unable to overwrite "
						+ newFile.getAbsolutePath());
			}
		}
	}

	private Folder getUploadFolder() {
		Folder uploadFolder = new Folder("wicketTEMP");
		// Ensure folder exists
		uploadFolder.mkdirs();

		return uploadFolder;
	}
}
