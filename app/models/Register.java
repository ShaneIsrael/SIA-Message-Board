package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Register {

	@Id
	@GeneratedValue
	private int id;

	private String phoneNumber;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String in) {
		this.phoneNumber = in;
	}

	public int getId() {
		return id;
	}
}
