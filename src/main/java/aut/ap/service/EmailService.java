package aut.ap.service;

import aut.ap.model.Email;
import aut.ap.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Random;

public class EmailService {

    // تولید کد ۶ حرفی برای هر ایمیل
    private String generateCode() {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random r = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(chars.charAt(r.nextInt(chars.length())));
        }
        return code.toString();
    }

    // ارسال ایمیل
    public String sendEmail(String sender, String recipients, String subject, String body) {
        String code = generateCode();
        Email email = new Email(code, sender, recipients, subject, body);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(email);
            tx.commit();
        }
        return code;
    }

    // همه ایمیل‌های دریافتی
    public List<Email> getAllEmails(String userEmail) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Email e WHERE e.recipients LIKE :user ORDER BY e.sentAt DESC",
                    Email.class
            ).setParameter("user", "%" + userEmail + "%").list();
        }
    }

    // ایمیل‌های خوانده نشده
    public List<Email> getUnreadEmails(String userEmail) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Email e WHERE e.recipients LIKE :user AND e.isRead = false ORDER BY e.sentAt DESC",
                    Email.class
            ).setParameter("user", "%" + userEmail + "%").list();
        }
    }

    // ایمیل‌های ارسالی
    public List<Email> getSentEmails(String userEmail) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Email e WHERE e.sender = :user ORDER BY e.sentAt DESC",
                    Email.class
            ).setParameter("user", userEmail).list();
        }
    }

    // گرفتن ایمیل با کد
    public Email getEmailByCode(String code) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Email e WHERE e.code = :code",
                    Email.class
            ).setParameter("code", code).uniqueResult();
        }
    }

    // مارک کردن به عنوان خوانده‌شده
    public void markAsRead(String code) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Email email = session.createQuery("FROM Email e WHERE e.code = :c", Email.class)
                    .setParameter("c", code).uniqueResult();
            if (email != null) {
                email.setRead(true);
                session.merge(email);
            }
            tx.commit();
        }
    }

    public String replyEmail(String originalCode, String replier, String body) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Email original = session.createQuery(
                            "FROM Email e WHERE e.code = :code", Email.class)
                    .setParameter("code", originalCode)
                    .uniqueResult();

            if (original == null) return null;

            String recipients = original.getSender() + ", " + original.getRecipients();


            String subject = "[Re] " + original.getSubject();

            return sendEmail(replier, recipients, subject, body);
        }
    }


    public String forwardEmail(String originalCode, String forwarder, String recipients) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Email original = session.createQuery(
                            "FROM Email e WHERE e.code = :code", Email.class)
                    .setParameter("code", originalCode)
                    .uniqueResult();

            if (original == null) return null;


            String subject = "[Fw] " + original.getSubject();

            // همون بدنه ایمیل اصلی
            return sendEmail(forwarder, recipients, subject, original.getBody());
        }
    }
}
