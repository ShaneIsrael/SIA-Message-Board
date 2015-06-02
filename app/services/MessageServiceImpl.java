package services;

import models.Message;

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

	@Override
	public void addMessage(Message msg) {
		em.persist(msg);
	}

	@Override
	public List<Message> getMessages() {
		CriteriaQuery<Message> c = em.getCriteriaBuilder().createQuery(Message.class);
		c.from(Message.class);
		TypedQuery<Message> query = em.createQuery(c);
		int maxResultsWanted = Play.application().configuration().getInt("messages.max");
		int maxDBResults = query.getResultList().size();
		query.setFirstResult(maxDBResults - maxResultsWanted);
		return query.getResultList();
	}

}
