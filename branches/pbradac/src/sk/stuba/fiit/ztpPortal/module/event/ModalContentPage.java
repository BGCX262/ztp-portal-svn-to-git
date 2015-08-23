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

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import sk.stuba.fiit.ztpPortal.core.CoreFeedBackPanel;
import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.databaseModel.Event;


/**
 * @author Matej Knopp
 * 
 */
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
					modalWindowPage.setResult("Modal window 1 - close link OK");
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
