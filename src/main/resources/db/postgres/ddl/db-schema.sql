-- 1. Autoritetų (roles) lentelė
CREATE TABLE IF NOT EXISTS authority (
                                         id SERIAL PRIMARY KEY,
                                         name VARCHAR(100) NOT NULL UNIQUE,
                                         description VARCHAR(255)
);

-- 2. Naudotojų (users) lentelė
CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     email VARCHAR(255) NOT NULL UNIQUE,
                                     first_name VARCHAR(255),
                                     last_name VARCHAR(255),
                                     password VARCHAR(255) NOT NULL,
                                     phone_number VARCHAR(255),
                                     username VARCHAR(255) NOT NULL UNIQUE,
                                     enabled BOOLEAN DEFAULT TRUE
);

-- 3. Užsakymų (orders) lentelė
CREATE TABLE IF NOT EXISTS orders (
                                      id SERIAL PRIMARY KEY,
                                      order_number VARCHAR(255) NOT NULL UNIQUE,
                                      created_at TIMESTAMP DEFAULT now()
    -- pridėk kitų laukų, jei reikia
);

-- 4. Many-to-many users ir authorities ryšio lentelė
CREATE TABLE IF NOT EXISTS users_authorities (
                                                 user_id BIGINT NOT NULL,
                                                 authority_id BIGINT NOT NULL,
                                                 PRIMARY KEY(user_id, authority_id),
                                                 CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                                 CONSTRAINT fk_authority FOREIGN KEY (authority_id) REFERENCES authority(id) ON DELETE CASCADE
);

-- 5. Work log lentelė (su foreign key į orders)
CREATE TABLE IF NOT EXISTS work_log_entity (
                                               id SERIAL PRIMARY KEY,
                                               worker_name VARCHAR(255),
                                               start_time TIMESTAMP,
                                               end_time TIMESTAMP,
                                               operation_name VARCHAR(255),
                                               paint_type VARCHAR(255),
                                               faulty BOOLEAN,
                                               fault_fixed BOOLEAN NOT NULL DEFAULT FALSE,
                                               order_id BIGINT,
                                               CONSTRAINT fk_work_log_order FOREIGN KEY (order_id) REFERENCES orders(id)
);

ALTER TABLE orders ADD COLUMN qr_code_image TEXT;