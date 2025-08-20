package aut.ap.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // تنظیمات از hibernate.cfg.xml خونده میشه
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("❌ Initial SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // برای استفاده در سرویس‌ها
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // بستن SessionFactory وقتی برنامه تموم میشه
    public static void shutdown() {
        getSessionFactory().close();
    }
}
