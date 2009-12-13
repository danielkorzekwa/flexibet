package dk.bot.webconsole.modules.moduletemplate;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;

public class ModuleTemplate extends BreadCrumbPanel{

	public ModuleTemplate(String id,final IBreadCrumbModel breadCrumbModel) {
		super(id,breadCrumbModel);
		
		add(new Label("hello","Hello World"));
	}
	
	public String getTitle() {
		return "template";
	}
}
