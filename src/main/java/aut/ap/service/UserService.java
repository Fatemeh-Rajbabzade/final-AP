package aut.ap.service;

import aut.ap.model.User;
import aut.ap.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class UserService {

    public boolean signUp(String name, String email, String password) {
        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters!!");
            return false;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Query<User> query = session.createQuery("FROM User WHERE email = :email", User.class);
            query.setParameter("email", email);
            if (!query.list().isEmpty()) {
                System.out.println("Email already exists.");
                tx.rollback();
                return false;
            }

            User user = new User(name, email, password);
            session.persist(user);

            tx.commit();
            return true;
        }
    }

    public User login(String email, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery(
                    "FROM User WHERE email = :email AND password = :password", User.class);
            query.setParameter("email", email);
            query.setParameter("password", password);

            return query.uniqueResult();
        }
    }
}
