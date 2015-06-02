package models;

import play.data.validation.Constraints;
import org.hibernate.annotations.SQLInsert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
//@SQLInsert(sql="INSERT IGNORE INTO Register(id, phoneNumber) VALUES(?,?)")
public class Register {
	@Id
	@GeneratedValue
	public int id;

	@Constraints.Required(message = "A message is required!")
	public String phoneNumber;

}
