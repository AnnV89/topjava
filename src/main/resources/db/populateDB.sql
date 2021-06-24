DELETE
FROM user_roles;
DELETE
FROM meals;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2021-06-23 09:00:00', 'Завтрак', 500, 100000),
       ('2021-06-23 08:00:00', 'Завтрак', 500, 100001),
       ('2021-06-22 08:00:00', 'Завтрак', 800, 100001),
       ('2021-06-22 08:00:00', 'Завтрак', 600, 100000),
       ('2021-06-23 12:00:00', 'Обед', 1200, 100000),
       ('2021-06-23 12:30:00', 'Обед', 1000, 100001),
       ('2021-06-22 12:40:00', 'Обед', 900, 100001),
       ('2021-06-22 13:30:00', 'Обед', 1100, 100000);
