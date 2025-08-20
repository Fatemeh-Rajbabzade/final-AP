package aut.ap.service;

import aut.ap.model.User;
import aut.ap.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserService {

    // ✨ متد نرمال‌سازی ایمیل کاربر
    private String normalizeEmail(String email) {
        email = email.trim();
        if (!email.contains("@")) {
            email += "@milou.com";
        }
        return email.toLowerCase(); // برای جلوگیری از حساسیت به حروف
    }

    // 🆕 ثبت‌نام
    public boolean signUp(String name, String email, String password) {
        email = normalizeEmail(email);

        if (password.length() < 8) {
            System.out.println("❌ Password must be at least 8 characters!");
            return false;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            if (session.createQuery("from User u where u.email = :email", User.class)
                    .setParameter("email", email)
                    .uniqueResult() != null) {
                System.out.println("❌ Email already exists.");
                return false;
            }

            Transaction tx = session.beginTransaction();
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            session.persist(user);
            tx.commit();
            return true;
        }
    }

    // 🔑 ورود
    public User login(String email, String password) {
        email = normalizeEmail(email);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from User u where u.email = :email and u.password = :password", User.class
                    ).setParameter("email", email)
                    .setParameter("password", password)
                    .uniqueResult();
        }
    }
}
