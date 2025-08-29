-- Create apparel_order_shipment table
CREATE TABLE apparel_order_shipment (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    created_date TIMESTAMP,
    update_date TIMESTAMP,
    apparel_order_id INT,
    shipment_date TIMESTAMP NOT NULL,
    carrier VARCHAR(255),
    tracking_number VARCHAR(255),
    FOREIGN KEY (apparel_order_id) REFERENCES apparel_order(id)
);