package services;

import models.Message;
import org.springframework.stereotype.Service;
import play.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
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
		return em.createQuery(c).getResultList();
	}

}
