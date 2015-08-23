package sk.stuba.fiit.ztpPortal.module.event;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.databaseModel.Event;

public class ModalContentPage extends CorePage
{

	private static FeedbackPanel feedbackModalPanel;
	
	/**
	 * 
	 * @param modalWindowPage
	 * @param window
	 */
	public ModalContentPage(final ModalWindowPage modalWindowPage, final Event event, final ModalWindow window)
	{
		add(new AjaxLink("closeOK")
		{
			public void onClick(AjaxRequestTarget target)
			{
				if (modalWindowPage != null)
//					modalWindowPage.setResult("Modal window 1 - close link OK");
				window.close(target);
			}
		});
		
		
		feedbackModalPanel = new FeedbackPanel("feedback",new ContainerFeedbackMessageFilter(this));
		add(feedbackModalPanel);
		
		/// vyber
		PictureUpload uploadPage = new PictureUpload();
		
		add(uploadPage.getFileUploadForm(event,feedbackModalPanel));
		///
	}
}
