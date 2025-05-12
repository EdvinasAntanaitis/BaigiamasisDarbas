CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name TEXT NOT NULL,
                       email TEXT UNIQUE,
                       age INTEGER,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
