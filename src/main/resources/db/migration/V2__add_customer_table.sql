-- Create customer table
CREATE TABLE customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    created_date TIMESTAMP,
    update_date TIMESTAMP,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone_number VARCHAR(255),
    address_line1 VARCHAR(255) NOT NULL,
    address_line2 VARCHAR(255),
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    postal_code VARCHAR(255) NOT NULL
);

-- Alter apparel_order table to add customer_id column and foreign key
-- ALTER TABLE apparel_order
--     ADD COLUMN customer_id INT,
--     ADD CONSTRAINT fk_apparel_order_customer FOREIGN KEY (customer_id) REFERENCES customer(id);

ALTER TABLE apparel_order
    ADD COLUMN customer_id INT;

ALTER TABLE apparel_order ADD CONSTRAINT fk_apparel_order_customer
    FOREIGN KEY (customer_id) REFERENCES customer(id);

-- Copy data from customer_ref to a temporary column if needed
-- This is a placeholder for data migration if required
-- In a real-world scenario, you might need to migrate existing customer references to the new customer table

-- Drop the customer_ref column
ALTER TABLE apparel_order
    DROP COLUMN customer_ref;