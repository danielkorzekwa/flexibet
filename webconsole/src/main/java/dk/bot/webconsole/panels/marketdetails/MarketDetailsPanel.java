package dk.bot.webconsole.panels.marketdetails;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import dk.bot.bettingengine.mbean.BettingEngineMBean;
import dk.bot.marketobserver.model.Market;
import dk.bot.webconsole.panels.marketdetails.stathelper.MarketDataStat;
import dk.bot.webconsole.panels.marketdetails.stathelper.MarketDataStatHelper;
import dk.bot.webconsole.panels.marketdetails.stathelper.RunnerDataStat;
import dk.bot.webconsole.panels.marketdetails.stathelper.RunnerDataStatHelper;

public class MarketDetailsPanel extends BreadCrumbPanel{
	
	@SpringBean(name="bettingEngineMBean")
	BettingEngineMBean bettingEngineMBean;
	
	public MarketDetailsPanel(String id, IBreadCrumbModel breadCrumbModel,Market completeMarket) {
		super(id,breadCrumbModel);
		
		MarketDataStat marketDataStat = MarketDataStatHelper.createMarketDataStat(completeMarket);

		add(new Label("marketId","" + marketDataStat.getMarketId()));
		add(new ExternalLink("betFairLink",marketDataStat.getBetFairDeepLink()));
		
		add(getMarketData(marketDataStat));
		add(getRunnerData(completeMarket));
	}
	
	private ListView getMarketData(MarketDataStat marketDataStat) {
		
		/** [0] - param name, [1] - param value */
		List<String[]> paramStats = new ArrayList<String[]>();
		paramStats.add(new String[] { "Name", "" + marketDataStat.getMarketName() });
		paramStats.add(new String[] { "Menu path", "" + marketDataStat.getMenuPath() });
		paramStats.add(new String[] { "In play delay [s]", "" + marketDataStat.getInPlayDelay()});
		paramStats.add(new String[] { "Num of runners", "" + marketDataStat.getNumberOfRunners()});
		paramStats.add(new String[] { "Num of winners", "" + marketDataStat.getNumberOfWinners()});
		paramStats.add(new String[] { "Market time", "" + marketDataStat.getMarketTime()});
		paramStats.add(new String[] { "MarketRunners timestamp", "" + marketDataStat.getMarketRunnersTimestamp()});
		
		ListView listView = new ListView("marketData", paramStats) {

			@Override
			protected void populateItem(ListItem item) {
				String[] param = (String[]) item.getModelObject();
				item.add(new Label("paramName", param[0]));
				item.add(new Label("paramValue", param[1]));
			}

		};
		return listView;
	}

	private ListView getRunnerData(Market completeMarket) {
		List<RunnerDataStat> runnerData = new ArrayList<RunnerDataStat>();
		
		if(completeMarket!=null) {
			runnerData = RunnerDataStatHelper.createRunnerDataStat(completeMarket,bettingEngineMBean.getRunnerPricePredictions(completeMarket.getMarketRunners().getMarketId()));		
		}
		
		ListView listView = new ListView("runnerData", runnerData) {

			@Override
			protected void populateItem(ListItem item) {
				RunnerDataStat runnerData = (RunnerDataStat) item.getModelObject();
				item.add(new Label("runnerName", runnerData.getRunnerName()));
				item.add(new Label("priceToBack", "" + runnerData.getPriceToBack()));
				item.add(new Label("priceToBackWeighted", "" + runnerData.getPriceToBackWeighted()));
				item.add(new Label("masseyPrice", "" + runnerData.getMasseyPrice()));
				item.add(new Label("racingPostPrice", "" + runnerData.getRacingPostPrice()));
				item.add(new Label("priceToLay", "" + runnerData.getPriceToLay()));
				item.add(new Label("lastPriceToBack", "" + runnerData.getLastPriceToBack()));
				item.add(new Label("lastPriceToLay", "" + runnerData.getLastPriceToLay()));
				item.add(new Label("nearSP", "" + runnerData.getNearSP()));
				item.add(new Label("farSP", "" + runnerData.getFarSP()));
				item.add(new Label("actualSP", "" + runnerData.getActualSP()));
				item.add(new Label("slope", "" + runnerData.getSlope()));
				item.add(new Label("slopeErr", "" + runnerData.getSlopeErr()));
				item.add(new Label("virtWinner", runnerData.isVirtWinner() ? "true" : ""));
			}

		};

		return listView;
	}
	
	public String getTitle() {
		return "Market Details";
	}

}
