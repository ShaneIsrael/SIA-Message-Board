package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import play.db.ebean.Model;

@Entity
public class Task {

	@Id
	public String id;

	public String contents;

	public void save() {}
}
