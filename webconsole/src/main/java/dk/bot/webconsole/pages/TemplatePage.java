package dk.bot.webconsole.pages;

import java.util.Date;

import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.util.time.Duration;

import com.ibm.icu.text.SimpleDateFormat;

public abstract class TemplatePage extends WebPage{

	/**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
	public TemplatePage(final PageParameters parameters) {
		super();
		Label clock = new Label("clock",new ClockModel());
		clock.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(1)));
		add(clock);
		
		add(new Label("view",getView()));
		
		add(new BookmarkablePageLink("betFairConsole",BetFairConsolePage.class));
		add(new BookmarkablePageLink("virtualConsole",VirtualConsolePage.class));
	}

	/** Name of the view to display in header, e.g. BetFair Console*/
	public abstract String getView();

    private static class ClockModel extends AbstractReadOnlyModel
    {
        private final SimpleDateFormat df;

        /**
         * @param tz
         */
        public ClockModel()
        {
            df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            
        }

        /**
         * @see org.apache.wicket.model.AbstractReadOnlyModel#getObject()
         */
        @Override
        public String getObject()
        {
            return df.format(new Date());
        }
    }
}
