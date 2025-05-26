
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
