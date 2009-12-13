package dk.flexibet.server.model;

import java.io.Serializable;
import java.util.Date;

/** Represents one account transaction, win, lose or commission
 * 
 * @author daniel
 *
 */
public class DetailedAccountStatementItem implements Serializable,Comparable<DetailedAccountStatementItem> {

	/** account statement unique id*/
	private int id;
	
	private int marketId;
	private String marketName;
	private String marketType;
	private String fullMarketName;
	
	private int selectionId;
	private String selectionName;
	
	private long betId;
	
	private int eventTypeId;
	private String betCategoryType;
	private String betType;
	private double betSize;
	private double avgPrice;
	
	private Date placedDate;
	private Date startDate;
	private Date settledDate;
	
	/**True for commission item.*/
	private boolean commission;
	
	/** amount of win/lose or commission*/
	private double amount;
	
	private String stateMachineId;
	
	private String stateName;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMarketId() {
		return marketId;
	}

	public void setMarketId(int marketId) {
		this.marketId = marketId;
	}

	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	public String getMarketType() {
		return marketType;
	}

	public void setMarketType(String marketType) {
		this.marketType = marketType;
	}

	public String getFullMarketName() {
		return fullMarketName;
	}

	public void setFullMarketName(String fullMarketName) {
		this.fullMarketName = fullMarketName;
	}

	public int getSelectionId() {
		return selectionId;
	}

	public void setSelectionId(int selectionId) {
		this.selectionId = selectionId;
	}

	public String getSelectionName() {
		return selectionName;
	}

	public void setSelectionName(String selectionName) {
		this.selectionName = selectionName;
	}

	public long getBetId() {
		return betId;
	}

	public void setBetId(long betId) {
		this.betId = betId;
	}

	public int getEventTypeId() {
		return eventTypeId;
	}

	public void setEventTypeId(int eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	public String getBetCategoryType() {
		return betCategoryType;
	}

	public void setBetCategoryType(String betCategoryType) {
		this.betCategoryType = betCategoryType;
	}

	public String getBetType() {
		return betType;
	}

	public void setBetType(String betType) {
		this.betType = betType;
	}

	public double getBetSize() {
		return betSize;
	}

	public void setBetSize(double betSize) {
		this.betSize = betSize;
	}

	public double getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(double avgPrice) {
		this.avgPrice = avgPrice;
	}

	public Date getPlacedDate() {
		return placedDate;
	}

	public void setPlacedDate(Date placedDate) {
		this.placedDate = placedDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getSettledDate() {
		return settledDate;
	}

	public void setSettledDate(Date settledDate) {
		this.settledDate = settledDate;
	}

	public boolean isCommission() {
		return commission;
	}

	public void setCommission(boolean commission) {
		this.commission = commission;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getStateMachineId() {
		return stateMachineId;
	}

	public void setStateMachineId(String stateMachineId) {
		this.stateMachineId = stateMachineId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public int compareTo(DetailedAccountStatementItem o) {
		return o.getSettledDate().compareTo(getSettledDate());
	}
	
}
