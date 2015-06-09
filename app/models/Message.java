package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Message {

	public static final int MAX_MESSAGE_LENGTH = 255;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
