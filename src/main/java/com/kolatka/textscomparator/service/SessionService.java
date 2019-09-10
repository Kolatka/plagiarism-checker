package com.kolatka.textscomparator.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class SessionService {

	private Session session;

	public void startSession() {
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		session = sessionFactory.openSession();
		session.beginTransaction();
	}

	public void stopSession() {
		session.close();
	}

	public void commit() {
		session.getTransaction().commit();
		session.beginTransaction();
	}

	public void commitAndClose() {
		session.getTransaction().commit();
		session.beginTransaction();
		session.close();
	}


	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}


}
