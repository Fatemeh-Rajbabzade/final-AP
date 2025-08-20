package aut.ap.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "emails")
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String code; // کد ۶ حرفی

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false)
    private String recipients; // چند گیرنده با کاما

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    // ===== سازنده‌ها =====
    public Email() {
    }

    public Email(String code, String sender, String recipients, String subject, String body) {
        this.code = code;
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
        this.body = body;
        this.sentAt = LocalDateTime.now();
    }

    public Email(Integer id, String code, String sender, String recipients, String subject, String body,
                 LocalDateTime sentAt, boolean isRead) {
        this.id = id;
        this.code = code;
        this.sender = sender;
        this.recipients = recipients;
        this.subject = subject;
        this.body = body;
        this.sentAt = sentAt;
        this.isRead = isRead;
    }

    // ===== getter / setter ها =====
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) { // معمولاً لازم نیست ولی به درخواست شما گذاشته شد
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "Email{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", sender='" + sender + '\'' +
                ", recipients='" + recipients + '\'' +
                ", subject='" + subject + '\'' +
                ", sentAt=" + sentAt +
                ", isRead=" + isRead +
                '}';
    }
}
