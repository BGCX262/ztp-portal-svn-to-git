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
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;

import sk.stuba.fiit.ztpPortal.core.CorePage;
import sk.stuba.fiit.ztpPortal.databaseModel.Event;


/**
 * @author Matej Knopp
 */
public class ModalWindowPage extends CorePage
{
	final ModalWindow modal1;
	final Label resultLabel;
	final AjaxLink ajaxLink;
	
	private Event event;
	/**
	 */
	public ModalWindowPage(final Event event)
	{
		this.event=event;
		
		add(resultLabel = new Label("result", new PropertyModel(this, "result")));
		resultLabel.setOutputMarkupId(true);

		/*
		 * First modal window
		 */

		add(modal1 = new ModalWindow("modal1"));

		modal1.setPageMapName("modal-1");
		modal1.setCookieName("modal-1");

		modal1.setPageCreator(new ModalWindow.PageCreator()
		{
			public Page createPage()
			{
				return new ModalContentPage(ModalWindowPage.this,event, modal1);
			}
		});
		modal1.setWindowClosedCallback(new ModalWindow.WindowClosedCallback()
		{
			public void onClose(AjaxRequestTarget target)
			{
				target.addComponent(resultLabel);
			}
		});
		modal1.setCloseButtonCallback(new ModalWindow.CloseButtonCallback()
		{
			public boolean onCloseButtonClicked(AjaxRequestTarget target)
			{
				//setResult("Modal window 1 - close button");
				return true;
			}
		});

		add(ajaxLink = new AjaxLink("pictureUpload")
		{
			public void onClick(AjaxRequestTarget target)
			{
				modal1.show(target);
			}
		});
	}
	
	public ModalWindow getModal(){
		return modal1;
	}

	public Label getLabel(){
		return resultLabel;
	} 
	
	public AjaxLink getLink(){
		return ajaxLink;
	}
	
	/**
	 * @return the result
	 */
	public String getResult()
	{
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(String result)
	{
		this.result = result;
	}

	private String result;
	public void setLinkVisible(boolean b) {
		ajaxLink.setVisible(b);
		
	}

}
