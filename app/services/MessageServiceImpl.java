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

    /**
     * Adds a message to the database
     */
	public void addMessage(Message msg) {
		em.persist(msg);
	}

	/**
	 * Registers a user with the database. This user will now receive
	 * new message board replies to their phone.
	 *
	 * @return true if success, false if failed.
	 */
	public boolean registerNumber(Register rgstr) {
		/*
		 * Don't add duplicate phone numbers to the database since multiple people
		 * can't have the same phone number.
		 */
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

	/**
	 * Unregisters a user with the database. This users will no longer
	 * receive messages to their phone.
	 */
	public boolean unregisterNumber(Register rgstr) {
		return (em.createQuery("DELETE FROM Register WHERE phoneNumber=:number")
				.setParameter("number", rgstr.getPhoneNumber())
				.executeUpdate() > 0);
	}

	/**
	 * @return The last MESSAGES_MAX messages from the database.
	 */
	public List<Message> getMessages() {
		CriteriaQuery<Message> c = em.getCriteriaBuilder().createQuery(Message.class);
		c.from(Message.class);
		TypedQuery<Message> query = em.createQuery(c);
		int maxResultsWanted = MESSAGES_MAX;
		int maxDBResults = query.getResultList().size();

		/*
		 * Only grabs the last MESSAGE_MAX defined messages. Default is 15 since there
		 * is no  reason to grab the entire database of messages.
		 */
		if (maxDBResults > maxResultsWanted) {
			query.setFirstResult(maxDBResults - maxResultsWanted);
		}
		return query.getResultList();
	}

	/**
	 * @return The last message sent to the message board.
	 */
	public Message getLastMessage() {
		CriteriaQuery<Message> c = em.getCriteriaBuilder().createQuery(Message.class);
		c.from(Message.class);
		TypedQuery<Message> query = em.createQuery(c);
		int maxDBResults = query.getResultList().size();
		query.setFirstResult(maxDBResults - 1);
		return query.getResultList().get(0);
	}

	/**
	 * @return A list of all registered phone numbers
	 */
	public List<Register> getRegisteredNumbers() {
		CriteriaQuery<Register> c = em.getCriteriaBuilder().createQuery(Register.class);
		c.from(Register.class);
		return em.createQuery(c).getResultList();
	}
}