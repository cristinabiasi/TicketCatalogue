DROP TABLE IF EXISTS ticketCatalogue;

CREATE TABLE ticketCatalogue (
    id serial PRIMARY KEY,
    type VARCHAR ( 50 ) NOT NULL,
    price NUMERIC NOT NULL
);

DROP TABLE IF EXISTS orders;

CREATE TABLE orders (
    id serial PRIMARY KEY,
    status VARCHAR ( 50 ) NOT NULL,
    ticketId INT NOT NULL,
    quantity INT NOT NULL,
    userId INT NOT NULL,
    FOREIGN KEY (ticketId)
        REFERENCES ticketCatalogue (id)
);