DROP TABLE IF EXISTS ticketCatalogue cascade;

CREATE TABLE ticketCatalogue (
    id serial PRIMARY KEY,
    type VARCHAR ( 50 ) NOT NULL,
    price NUMERIC NOT NULL,
    age_restriction INT
);

DROP TABLE IF EXISTS orders;

CREATE TABLE orders (
    id serial PRIMARY KEY,
    status VARCHAR ( 50 ) NOT NULL,
    ticket_id INT NOT NULL,
    quantity INT NOT NULL,
    user_id INT NOT NULL,
    FOREIGN KEY (ticket_id)
        REFERENCES ticketCatalogue (id)
);