package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import play.data.validation.Constraints;

@Entity
public class Message {

	@Id
	@GeneratedValue
	public int id;

	@Constraints.Required(message = "A message is required!")
	public String contents;

}
