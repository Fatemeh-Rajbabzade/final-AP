package aut.ap;

import aut.ap.model.Email;
import aut.ap.model.User;
import aut.ap.service.EmailService;
import aut.ap.service.UserService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();
        EmailService emailService = new EmailService();
        User loggedInUser = null;

        while (true) {
            System.out.print("[L]ogin, [S]ign up: ");
            String cmd = sc.nextLine().trim().toLowerCase();

            if (cmd.equals("s") || cmd.equals("sign up")) {
                System.out.print("Name: ");
                String name = sc.nextLine();

                System.out.print("Email: ");
                String email = sc.nextLine();
                if (!email.contains("@")) email += "@milou.com";

                System.out.print("Password: ");
                String password = sc.nextLine();

                if (userService.signUp(name, email, password)) {
                    System.out.println("✅ Your new account is created.");
                    System.out.println("Go ahead and login!");
                }

            } else if (cmd.equals("l") || cmd.equals("login")) {
                System.out.print("Email: ");
                String email = sc.nextLine();
                if (!email.contains("@")) email += "@milou.com";

                System.out.print("Password: ");
                String password = sc.nextLine();

                loggedInUser = userService.login(email, password);
                if (loggedInUser != null) {
                    String username = loggedInUser.getEmail().split("@")[0];
                    System.out.println("✨ Welcome back, " + username + "!");

                    // دستورات بعد از ورود
                    while (true) {
                        System.out.print("[S]end, [V]iew, [R]eply, [F]orward: ");
                        String action = sc.nextLine().trim().toLowerCase();

                        if (action.equals("s")) { // ارسال ایمیل
                            System.out.print("Recipient(s): ");
                            String recipients = sc.nextLine();

                            System.out.print("Subject: ");
                            String subject = sc.nextLine();

                            System.out.print("Body: ");
                            String body = sc.nextLine();

                            String code = emailService.sendEmail(
                                    loggedInUser.getEmail(),
                                    recipients,
                                    subject,
                                    body
                            );

                            System.out.println("✅ Successfully sent your email.");
                            System.out.println("Code: " + code);

                        } else if (action.equals("v")) { // نمایش ایمیل‌ها
                            System.out.print("[A]ll, [U]nread, [S]ent, [C]ode: ");
                            String viewChoice = sc.nextLine().trim().toLowerCase();

                            switch (viewChoice) {
                                case "a":
                                    System.out.println("All Emails:");
                                    emailService.getAllEmails(loggedInUser.getEmail())
                                            .forEach(e -> System.out.println("+ " + e.getSender() + " - " + e.getSubject() + " (" + e.getCode() + ")"));
                                    break;

                                case "u":
                                    System.out.println("Unread Emails:");
                                    emailService.getUnreadEmails(loggedInUser.getEmail())
                                            .forEach(e -> System.out.println("+ " + e.getSender() + " - " + e.getSubject() + " (" + e.getCode() + ")"));
                                    break;

                                case "s":
                                    System.out.println("Sent Emails:");
                                    emailService.getSentEmails(loggedInUser.getEmail())
                                            .forEach(e -> System.out.println("+ " + e.getRecipients() + " - " + e.getSubject() + " (" + e.getCode() + ")"));
                                    break;

                                case "c":
                                    System.out.print("Enter code: ");
                                    String code = sc.nextLine().trim();
                                    Email emailObj = emailService.getEmailByCode(code);
                                    if (emailObj != null) {
                                        // فقط اگر کاربر مجاز باشه
                                        if (emailObj.getSender().equals(loggedInUser.getEmail())
                                                || emailObj.getRecipients().contains(loggedInUser.getEmail())) {
                                            System.out.println("Code: " + emailObj.getCode());
                                            System.out.println("Recipient(s): " + emailObj.getRecipients());
                                            System.out.println("Subject: " + emailObj.getSubject());
                                            System.out.println("Date: " + emailObj.getSentAt());
                                            System.out.println("\n" + emailObj.getBody());
                                            emailService.markAsRead(code);
                                        } else {
                                            System.out.println("❌ You cannot read this email.");
                                        }
                                    } else {
                                        System.out.println("❌ Email not found.");
                                    }
                                    break;
                            }

                        } else if (action.equals("r")) { // پاسخ دادن
                            System.out.print("Code: ");
                            String code = sc.nextLine().trim();

                            System.out.print("Body: ");
                            String body = sc.nextLine();

                            String replyCode = emailService.replyEmail(code, loggedInUser.getEmail(), body);
                            if (replyCode != null) {
                                System.out.println("✅ Successfully sent your reply to email " + code + ".");
                                System.out.println("Code: " + replyCode);
                            } else {
                                System.out.println("❌ Original email not found.");
                            }

                        } else if (action.equals("f")) { // فوروارد
                            System.out.print("Code: ");
                            String code = sc.nextLine().trim();

                            System.out.print("Recipient(s): ");
                            String recipients = sc.nextLine();

                            String forwardCode = emailService.forwardEmail(code, loggedInUser.getEmail(), recipients);
                            if (forwardCode != null) {
                                System.out.println("✅ Successfully forwarded your email.");
                                System.out.println("Code: " + forwardCode);
                            } else {
                                System.out.println("❌ Original email not found.");
                            }

                        } else {
                            System.out.println("⚠️ Invalid action.");
                        }
                    }
                } else {
                    System.out.println("Invalid email or password.");
                }
            } else {
                System.out.println("Invalid command. Try again.");
            }
        }
    }
}
