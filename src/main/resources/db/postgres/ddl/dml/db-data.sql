-- Sukuriamos rolės
INSERT INTO authority (name, description)
VALUES ('ROLE_ADMIN', 'Administrator'),
       ('ROLE_USER', 'Regular User');

-- Sukuriami vartotojai su Bcrypt hash slaptažodžiais
INSERT INTO users (username, password, first_name, last_name, email, phone_number, enabled)
VALUES ('admin',
        '$2a$12$scveGQWH2wL6VWjzspc9a.6YK/kZx15X1FwpgPbQTloQlOPd1Jq3W', -- admin123
        'Admin', 'Admin1', 'admin@cf.lt', 'N/A', TRUE),
       ('worker',
        '$2a$12$S0YT9w1GZiAGZ3Q/dcDG7.0fERM7z8oNGKKfe7t53CVDm2kYa6QZu', -- worker123
        'Worker', 'Worker1', 'worker@cflt', 'N/A', TRUE);

-- Priskiriamos rolės vartotojams
INSERT INTO users_authorities (user_id, authority_id)
VALUES
    ((SELECT id FROM users WHERE username='admin'),
     (SELECT id FROM authority WHERE name='ROLE_ADMIN')),
    ((SELECT id FROM users WHERE username='admin'),
     (SELECT id FROM authority WHERE name='ROLE_USER')),
    ((SELECT id FROM users WHERE username='worker'),
     (SELECT id FROM authority WHERE name='ROLE_USER'));
