package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import play.db.ebean.Model;

@Entity
public class Task extends Model {

	@Id
	public String id;

	@play.data.validation.Constraints.Required
	public String contents;
}
