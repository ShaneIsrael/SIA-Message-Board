package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Message {

	@Id
	@GeneratedValue
	private int id;

	private String contents;

	public String getContents() {
		return contents;
	}

	public void setContents(String in) {
		this.contents = in;
	}
}
