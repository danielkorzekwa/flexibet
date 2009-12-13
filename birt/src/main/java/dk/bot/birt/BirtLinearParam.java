package dk.bot.birt;

import java.io.Serializable;
import java.util.List;

/** Birt linear parameter */
public class BirtLinearParam implements Serializable{

	private String paramName;
	
	/** Parameter values.*/
	private List<String> values;

	private String selectedValue=null;
	
	public BirtLinearParam() {
	}
	
	public BirtLinearParam(String paramName,String selectedValue) {
		this.paramName=paramName;
		this.selectedValue=selectedValue;
	}
	
	
	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public String getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}
}
