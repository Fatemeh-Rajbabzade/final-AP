package aut.ap.service;

import aut.ap.model.Email;
import aut.ap.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class EmailService {

    // ✨ متد ساخت کد یکتا برای ایمیل
    private String generateCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    // ✨ متد استانداردسازی گیرنده‌ها
    private String normalizeRecipients(String recipients) {
        String[] recList = recipients.split(",");
        for (int i = 0; i < recList.length; i++) {
            recList[i] = recList[i].trim();
            if (!recList[i].contains("@")) {
                recList[i] += "@milou.com";
            }
        }
        return String.join(",", recList);
    }

    // ✉ ارسال ایمیل
    public String sendEmail(String sender, String recipients, String subject, String body) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            recipients = normalizeRecipients(recipients); // ✅ نرمال‌سازی

            Email email = new Email();
            email.setSender(sender);
            email.setRecipients(recipients);
            email.setSubject(subject);
            email.setBody(body);
            email.setSentAt(LocalDateTime.now());
            email.setRead(false);
            email.setCode(generateCode());

            session.persist(email);
            tx.commit();

            return email.getCode();
        }
    }

    // 📥 گرفتن همه ایمیل‌ها
    public List<Email> getAllEmails(String userEmail) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Email e where e.sender = :email or e.recipients like :email order by e.sentAt desc",
                            Email.class
                    ).setParameter("email", "%" + userEmail + "%")
                    .list();
        }
    }

    // 📥 گرفتن ایمیل‌های نخوانده
    public List<Email> getUnreadEmails(String userEmail) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Email e where e.recipients like :email and e.isRead = false order by e.sentAt desc",
                            Email.class
                    ).setParameter("email", "%" + userEmail + "%")
                    .list();
        }
    }

    // 📤 گرفتن ایمیل‌های ارسال‌شده
    public List<Email> getSentEmails(String userEmail) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Email e where e.sender = :email order by e.sentAt desc",
                            Email.class
                    ).setParameter("email", userEmail)
                    .list();
        }
    }

    // 📩 گرفتن ایمیل با کد
    public Email getEmailByCode(String code) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Email e where e.code = :code", Email.class)
                    .setParameter("code", code)
                    .uniqueResult();
        }
    }

    // ✅ مارک کردن ایمیل به عنوان خوانده‌شده
    public void markAsRead(String code) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Email email = getEmailByCode(code);
            if (email != null) {
                email.setRead(true);
                session.merge(email);
            }
            tx.commit();
        }
    }

    // 🔁 ریپلای به ایمیل
    public String replyEmail(String originalCode, String replier, String body) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Email original = getEmailByCode(originalCode);
            if (original == null) return null;

            String recipient = original.getSender();
            if (!recipient.contains("@")) {
                recipient += "@milou.com"; // ✅ نرمال‌سازی
            }

            Email reply = new Email();
            reply.setSender(replier);
            reply.setRecipients(recipient);
            reply.setSubject("Re: " + original.getSubject());
            reply.setBody(body);
            reply.setSentAt(LocalDateTime.now());
            reply.setRead(false);
            reply.setCode(generateCode());

            session.persist(reply);
            tx.commit();

            return reply.getCode();
        }
    }

    // 📤 فوروارد ایمیل
    public String forwardEmail(String originalCode, String forwarder, String recipients) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Email original = getEmailByCode(originalCode);
            if (original == null) return null;

            recipients = normalizeRecipients(recipients); // ✅ نرمال‌سازی

            Email forward = new Email();
            forward.setSender(forwarder);
            forward.setRecipients(recipients);
            forward.setSubject("Fwd: " + original.getSubject());
            forward.setBody(original.getBody());
            forward.setSentAt(LocalDateTime.now());
            forward.setRead(false);
            forward.setCode(generateCode());

            session.persist(forward);
            tx.commit();

            return forward.getCode();
        }
    }
}
