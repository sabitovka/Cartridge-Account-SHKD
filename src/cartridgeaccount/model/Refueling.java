package cartridgeaccount.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.property.SimpleObjectProperty;

public class Refueling implements DateDelegate {

	private SimpleObjectProperty<Date> mStartDate;
	private SimpleObjectProperty<Date> mEndDate;
	
	public Refueling(Date startDate, Date endDate) {
		super();
		mStartDate = new SimpleObjectProperty<Date>(startDate);
		mEndDate = new SimpleObjectProperty<Date>(endDate);
	}
	
	@Override
	public void setStartDate(Date date) {
		mStartDate.set(date);		
	}
	@Override
	public void setEndDate(Date date) {
		mEndDate.set(date);
	}
	
	public Date getStartDate() {
		 return mStartDate.get();
	}
	
}
