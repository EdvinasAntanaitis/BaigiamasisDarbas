insert into users (first_name,
                   last_name,
                   username,
                   email,
                   password,
                   phone_number,
                   enabled)
VALUES ('Admin', 'Admin1', 'admin','admin@cf.lt',
        '$2a$12$scveGQWH2wL6VWjzspc9a.6YK/kZx15X1FwpgPbQTloQlOPd1Jq3W',
        'N/A', TRUE),
       ('Worker', 'worker1', 'worker','worker@cflt',
        '$2a$12$S0YT9w1GZiAGZ3Q/dcDG7.0fERM7z8oNGKKfe7t53CVDm2kYa6QZu',
        'N/A', TRUE);
