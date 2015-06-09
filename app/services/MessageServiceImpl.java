package services;

import models.Message;
import models.Register;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

	@PersistenceContext
	private EntityManager em;

    private static final Logger logger = LoggerFactory
            .getLogger(MessageServiceImpl.class);

    @Value("${messages.max}")
    private int MESSAGES_MAX;

	public void addMessage(Message msg) {
		em.persist(msg);
	}

	public boolean registerNumber(Register rgstr) {
		//we don't want to add duplicates
		boolean exists = false;
		for (Register registered : getRegisteredNumbers()) {
			if (registered.getPhoneNumber().equals(rgstr.getPhoneNumber())) {
				logger.info("Duplicate User {} Found: The database already contains a users phone number.", rgstr.getPhoneNumber());
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
		int maxResultsWanted = MESSAGES_MAX;
		int maxDBResults = query.getResultList().size();
		//returns the last results message
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

	public List<Register> getRegisteredNumbers() {
		CriteriaQuery<Register> c = em.getCriteriaBuilder().createQuery(Register.class);
		c.from(Register.class);
		return em.createQuery(c).getResultList();
	}
}