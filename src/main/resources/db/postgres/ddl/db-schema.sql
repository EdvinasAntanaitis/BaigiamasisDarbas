
CREATE TABLE IF NOT EXISTS orders (
                        id SERIAL PRIMARY KEY,
                        order_name TEXT NOT NULL,
                        description TEXT,
                        creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS part_group (
                            id SERIAL PRIMARY KEY,
                            material TEXT,
                            paint_color TEXT,
                            order_id INT NOT NULL,
                            CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS part (
                      id SERIAL PRIMARY KEY,
                      length DOUBLE PRECISION,
                      width DOUBLE PRECISION,
                      thickness DOUBLE PRECISION,
                      amount INT NOT NULL,
                      painted_area DOUBLE PRECISION,
                      part_group_id INT NOT NULL,
                      CONSTRAINT fk_part_group FOREIGN KEY (part_group_id) REFERENCES part_group(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users (
                                     username VARCHAR(50) NOT NULL PRIMARY KEY,
                                     password VARCHAR(100) NOT NULL,
                                     first_name VARCHAR(50) NOT NULL ,
                                     last_name VARCHAR(50) NOT NULL,
                                     email VARCHAR (50) NOT NULL,
                                     phone_number VARCHAR (13) NOT NULL,
                                     enabled BOOLEAN NOT NULL
);


CREATE TABLE IF NOT EXISTS authorities (
                             username VARCHAR(50) NOT NULL,
                             authority VARCHAR(50) NOT NULL,
                             CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES users(username)
);

CREATE TABLE work_log_entity (
                                 id SERIAL PRIMARY KEY,
                                 worker_name VARCHAR(255),
                                 start_time TIMESTAMP,
                                 end_time TIMESTAMP,
                                 operation_name VARCHAR(255),
                                 paint_type VARCHAR(255),
                                 faulty BOOLEAN,
                                 order_id BIGINT,
                                 CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE UNIQUE INDEX ix_auth_username ON authorities (username, authority);

ALTER TABLE orders ADD COLUMN completed BOOLEAN DEFAULT FALSE;

ALTER TABLE work_log_entity ADD COLUMN fault_fixed BOOLEAN NOT NULL DEFAULT FALSE;


