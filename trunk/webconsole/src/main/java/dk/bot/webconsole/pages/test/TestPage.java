package dk.bot.webconsole.pages.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.extensions.markup.html.tabs.TabbedPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import dk.bot.webconsole.modules.system.SystemModuleIndex;
import dk.bot.webconsole.pages.TemplatePage;

/**
 * Index page for the bread crumb example.
 * 
 * @author Eelco Hillenius
 */
public class TestPage extends TemplatePage
{
    /**
     * Construct.
     */
    public TestPage(final PageParameters parameters)
    {
        super(parameters);
    	List<ITab> tabs = new ArrayList<ITab>();
		
//		tabs.add(new AbstractTab(new Model("System")) {
//
//			public Panel getPanel(String panelId) {
//				return new AjaxLazyLoadPanel(panelId) {
//					@Override
//					public Component getLazyLoadComponent(String id) {
//							SystemModuleIndex systemModule = new SystemModuleIndex(id);
//							return systemModule;
//					}
//				};
//			}
//		});

		tabs.add(new AbstractTab(new Model("System")) {

			public Panel getPanel(String panelId) {
				return new SystemModuleIndex(panelId);
			}
		});
		tabs.add(new AbstractTab(new Model("System2")) {

			public Panel getPanel(String panelId) {
				return new SystemModuleIndex(panelId);
			}
		});
		
		add(new TabbedPanel("tabs",tabs));
    }
    
    @Override
    public String getView() {
    	return "aaa";
    }
}