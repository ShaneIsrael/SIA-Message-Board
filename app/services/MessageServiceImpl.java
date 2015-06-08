package services;

import controllers.Application;
import models.Message;
import models.Register;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import play.Play;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

	@PersistenceContext
	private EntityManager em;

    private static final Logger logger = LoggerFactory
            .getLogger(Application.class);

	public void addMessage(Message msg) {
		em.persist(msg);
	}

	public boolean registerNumber(Register rgstr) {
		//we don't want to add duplicates
		boolean exists = false;
		for (Register registered : getRegisteredNumbers()) {
			if (registered.getPhoneNumber().equals(rgstr.getPhoneNumber())) {
				logger.info("Duplicate User Found: The database already contains a users phone number.");
				exists = true;
				break;
			}
		}

		if (!exists) {
			em.persist(rgstr);
			return true;
		}
		return false;
	}

	public boolean unregisterNumber(Register rgstr) {
		return (em.createQuery("DELETE FROM Register WHERE phoneNumber=:number")
				.setParameter("number", rgstr.getPhoneNumber())
				.executeUpdate() > 0);
	}

	public List<Message> getMessages() {
		CriteriaQuery<Message> c = em.getCriteriaBuilder().createQuery(Message.class);
		c.from(Message.class);
		TypedQuery<Message> query = em.createQuery(c);
		int maxResultsWanted = Play.application().configuration().getInt("messages.max");
		int maxDBResults = query.getResultList().size();
		if (maxDBResults > maxResultsWanted) {
			query.setFirstResult(maxDBResults - maxResultsWanted);
		}
		return query.getResultList();
	}

	public Message getLastMessage() {
		CriteriaQuery<Message> c = em.getCriteriaBuilder().createQuery(Message.class);
		c.from(Message.class);
		TypedQuery<Message> query = em.createQuery(c);
		int maxDBResults = query.getResultList().size();
		query.setFirstResult(maxDBResults - 1);
		return query.getResultList().get(0);
	}

	public List<models.Register> getRegisteredNumbers() {
		CriteriaQuery<models.Register> c = em.getCriteriaBuilder().createQuery(models.Register.class);
		c.from(models.Register.class);
		return em.createQuery(c).getResultList();
	}
}
