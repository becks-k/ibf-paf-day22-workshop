-- Create rsvp database and table
DROP DATABASE IF EXISTS rsvp;
CREATE DATABASE rsvp;
USE rsvp;

CREATE TABLE rsvp (
    customer_id INT NOT NULL AUTO INCREMENT,
    name VARCHAR(64) NOT NULL,
    email VARCHAR(125) NOT NULL UNIQUE,
    phone VARCHAR(8) NOT NULL,
    confirmation_date DATE NOT NULL,
    comments TEXT,
    food_type ENUM("Vegan", "Vegetarian", "Pescatarian", "Normal") NOT NULL,

    PRIMARY KEY(customer_id)
);

GRANT ALL PRIVILEGES ON rsvp.* TO 'becky'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;