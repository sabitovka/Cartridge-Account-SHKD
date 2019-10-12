package cartridgeaccount.model;

import java.util.UUID;

import javafx.beans.property.SimpleStringProperty;

public class Cartridge {
	
	UUID uid;
	SimpleStringProperty mProducer;
	SimpleStringProperty mName;
	SimpleStringProperty mFullName;
	SimpleStringProperty mNum;
	SimpleStringProperty mState;
	SimpleStringProperty mNote;
	
	public Cartridge(UUID uuid) {
		uid = uuid;
	}
	
	public Cartridge(UUID uuid, String mProducer, String mName, String mNum, String mState, String mNote) {
		this(uuid);
		this.mProducer = new SimpleStringProperty(mProducer);
		this.mName = new SimpleStringProperty(mName);
		this.mFullName = new SimpleStringProperty(mProducer + " " + mName);
		this.mNum = new SimpleStringProperty(mNum);
		this.mState = new SimpleStringProperty(mState);
		this.mNote = new SimpleStringProperty(mNote);
	}

	public Cartridge(Cartridge cartridge) {
		this(UUID.randomUUID());
		this.mProducer = new SimpleStringProperty(cartridge.getProducer());
		this.mName = new SimpleStringProperty(cartridge.getName());
		this.mFullName = new SimpleStringProperty(mProducer.get() + " " + mName.get());
		this.mNum = new SimpleStringProperty(cartridge.getNum());
		this.mState = new SimpleStringProperty(cartridge.getState());
		this.mNote = new SimpleStringProperty(cartridge.getNote());
	}

	public UUID getUid() {
		return uid;
	}

	public String getProducer() {
		return mProducer.get();
	}

	public void setProducer(String producer) {
		mProducer.set(producer);
		mFullName.set(getProducer() + " " + getName());
	}

	public String getName() {
		return mName.get();
	}
	
	public String getFullName() {
		return mFullName.get();
	}

	public void setName(String name) {
		mName.set(name);
		mFullName.set(getProducer() + " " + getName());
	}

	public String getNum() {
		return mNum.get();
	}

	public void setNum(String num) {
		mNum.set(num);
	}

	public String getState() {
		return mState.get();
	}

	public void setState(String state) {
		mState.set(state);;
	}

	public String getNote() {
		return mNote.get();
	}

	public void setNote(String note) {
		mNote.set(note);
	}
	
	public SimpleStringProperty getFullNameProperty() {
		return mFullName;
	}
	
	public SimpleStringProperty getNumProperty() {
		return mNum;
	}
	
	public SimpleStringProperty getStateProperty() {
		return mState;
	}
	
	public SimpleStringProperty getNoteProperty() {
		return mNote;
	}

	@Override
	public String toString() {
		return "Cartridge [uid=" + uid + ", mProducer=" + mProducer + ", mName=" + mName + ", mFullName=" + mFullName
				+ ", mNum=" + mNum + ", mState=" + mState + ", mNote=" + mNote + "]";
	}
	
	
}
