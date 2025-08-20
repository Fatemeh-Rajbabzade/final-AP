 Project title : MiniMail – Simple Email System

یک پروژه‌ی آموزشی نوشته‌شده با Java + Hibernate + MySQL برای شبیه‌سازی یک سرویس ایمیل ساده.
کاربرها می‌تونن ثبت‌نام کنن، وارد بشن، ایمیل بفرستن، مشاهده کنن، ریپلای یا فوروارد کنن.

 ویژگی‌ها

ثبت‌نام (Sign up): کاربر جدید ایجاد می‌شود (ایمیل یکتا).

ورود (Login): با ایمیل و پسورد.

ارسال ایمیل (Send): به یک یا چند گیرنده.

مشاهده ایمیل‌ها (View):

همه (All)

خوانده‌نشده‌ها (Unread)

ارسال‌شده‌ها (Sent)

بر اساس کد (Code)

پاسخ (Reply): جواب به یک ایمیل موجود.

فوروارد (Forward): ارسال دوباره ایمیل برای دیگران.

خروج (Logout/ Quit): پایان یا خروج کاربر.

 تکنولوژی‌ها

Java 17+ (پروژه روی JDK 23 تست شده)

Hibernate ORM (برای مدیریت دیتابیس)

MySQL (پایگاه داده)

Maven (مدیریت وابستگی‌ها)

راه‌اندازی پروژه
1. ساخت دیتابیس

در MySQL:

CREATE DATABASE final CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

2. کانفیگ Hibernate

در فایل hibernate.cfg.xml (داخل resources/) تنظیم کن:

<property name="hibernate.connection.url">jdbc:mysql://localhost:3306/final</property>
<property name="hibernate.connection.username">your-username</property>
<property name="hibernate.connection.password">your-password</property>

3. اجرای پروژه
mvn clean install
mvn exec:java -Dexec.mainClass="aut.ap.Main"

مثال اجرا
[L]ogin, [S]ign up, [Q]uit: s
Name: Tara
Email: tara
Password: 12345678
Your new account is created.
Go ahead and login!

[L]ogin, [S]ign up, [Q]uit: l
Email: tara
Password: 12345678
Welcome back, tara!

[S]end, [V]iew, [R]eply, [F]orward, [L]ogout: s
Recipient(s): morad@milou.com
Subject: hi
Body: are you ok?
 Successfully sent your email.
Code: a1b2c3

ساختار پوشه‌ها
src/main/java/aut/ap/
│── Main.java          # نقطه شروع برنامه
│── model/             # کلاس‌های مدل (User, Email)
│── service/           # منطق برنامه (UserService, EmailService)
│── util/              # HibernateUtil

شاخه ها:
master
main
model
service
