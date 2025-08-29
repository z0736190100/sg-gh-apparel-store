-- Create tables for Apparel, ApparelOrder, and ApparelOrderLine entities

-- Create apparel table
CREATE TABLE apparel (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    created_date TIMESTAMP,
    update_date TIMESTAMP,
    apparel_name VARCHAR(255) NOT NULL,
    apparel_style VARCHAR(255),
    upc VARCHAR(255),
    quantity_on_hand INT,
    price DECIMAL(19, 2)
);

-- Create apparel_order table
CREATE TABLE apparel_order (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    created_date TIMESTAMP,
    update_date TIMESTAMP,
    customer_ref VARCHAR(255),
    payment_amount DECIMAL(19, 2),
    status VARCHAR(255)
);

-- Create apparel_order_line table
CREATE TABLE apparel_order_line (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    created_date TIMESTAMP,
    update_date TIMESTAMP,
    apparel_order_id INT,
    apparel_id INT,
    order_quantity INT,
    quantity_allocated INT,
    status VARCHAR(255),
    FOREIGN KEY (apparel_order_id) REFERENCES apparel_order(id),
    FOREIGN KEY (apparel_id) REFERENCES apparel(id)
);