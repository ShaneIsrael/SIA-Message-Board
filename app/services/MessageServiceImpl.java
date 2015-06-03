package services;

import models.Message;
import models.Register;

import org.h2.command.dml.Query;
import org.springframework.stereotype.Service;

import play.Play;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.TypedQuery;

import java.util.List;

@Service
@org.springframework.transaction.annotation.Transactional
public class MessageServiceImpl implements MessageService {

	@PersistenceContext
	EntityManager em;

	public void addMessage(Message msg) {
		em.persist(msg);
	}

	public boolean registerNumber(Register rgstr) {
		//we don't want to add duplicates
		boolean exists = false;
		for(Register registered : getRegisteredNumbers()) {
			if(registered.phoneNumber.equals(rgstr.phoneNumber)) {
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
		javax.persistence.Query query = em.createQuery("DELETE FROM Register WHERE phoneNumber=:number");
		query.setParameter("number", rgstr.phoneNumber);
		int result = query.executeUpdate();
		if (result > 0) {
			return true;
		}
		return false;
	}

	public List<Message> getMessages() {
		CriteriaQuery<Message> c = em.getCriteriaBuilder().createQuery(Message.class);
		c.from(Message.class);
		TypedQuery<Message> query = em.createQuery(c);
		int maxResultsWanted = Play.application().configuration().getInt("messages.max");
		int maxDBResults = query.getResultList().size();
		query.setFirstResult(maxDBResults - maxResultsWanted);
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
