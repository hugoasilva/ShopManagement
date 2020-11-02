CREATE TABLE `users`
(
    `id`       int(10) unsigned NOT NULL UNIQUE AUTO_INCREMENT,
    `username` varchar(30)      NOT NULL UNIQUE,
    `password` varchar(30)      NOT NULL,
    `level`    varchar(10)      NOT NULL,
    PRIMARY KEY (`id`)
);

Tabela
Moradas

CREATE TABLE `addresses`
(
    `id`       int(11)      NOT NULL UNIQUE AUTO_INCREMENT,
    `street`   varchar(255) NOT NULL,
    `number`   int(11)      NOT NULL,
    `town`     varchar(100) NOT NULL,
    `city`     varchar(100) NOT NULL,
    `district` varchar(100) NOT NULL,
    `zip`      varchar(9)   NOT NULL,
    PRIMARY KEY (`id`)
);

Tabela 
Marcas

CREATE TABLE `brands`
(
    `id`   int(11)      NOT NULL UNIQUE AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    `type` varchar(20)  NOT NULL,
    PRIMARY KEY (`id`)
);

Tabela
Clientes

CREATE TABLE `customers`
(
    `id`         int(11)        NOT NULL UNIQUE AUTO_INCREMENT,
    `name`       varchar(255)   NOT NULL,
    `address_id` int(11)        NOT NULL UNIQUE,
    `phone`      decimal(10, 0) NOT NULL UNIQUE,
    `email`      varchar(100)   NOT NULL UNIQUE,
    `nif`        decimal(10, 0) NOT NULL UNIQUE,
    PRIMARY KEY (`id`)
);

Tabela
Empregados

CREATE TABLE `employees`
(
    `id`         int(11)        NOT NULL UNIQUE AUTO_INCREMENT,
    `name`       varchar(255)   NOT NULL,
    `address_id` int(11)        NOT NULL UNIQUE,
    `phone`      decimal(10, 0) NOT NULL UNIQUE,
    `email`      varchar(100)   NOT NULL UNIQUE,
    `nif`        decimal(10, 0) NOT NULL UNIQUE,
    PRIMARY KEY (`id`)
);

Tabela
Faturas

CREATE TABLE `invoices`
(
    `id`          int(11)      NOT NULL UNIQUE AUTO_INCREMENT,
    `customer_id` int(11)      NOT NULL,
    `employee_id` int(11)      NOT NULL,
    `date`        date         NOT NULL,
    `pdf`         varchar(255) NOT NULL UNIQUE,
    PRIMARY KEY (`id`),
    KEY `customer_id` (`customer_id`),
    KEY `employee_id` (`employee_id`),
    CONSTRAINT `fk_invoices_customers` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`),
    CONSTRAINT `fk_invoices_employees` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`)
        ON DELETE CASCADE
);

Tabela
Produtos

CREATE TABLE `products`
(
    `id`          int(11)      NOT NULL UNIQUE AUTO_INCREMENT,
    `name`        varchar(255) NOT NULL,
    `price`       int(11)      NOT NULL,
    `supplier_id` int(11)      NOT NULL,
    `quantity`    int(11)      NOT NULL,
    `image`       varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `supplier_id` (`supplier_id`),
    CONSTRAINT `fk_products_suppliers` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`)
        ON DELETE CASCADE
) Tabela
Notas
Clientes

CREATE TABLE `notes_customers`
(
    `id`          int(11)      NOT NULL UNIQUE AUTO_INCREMENT,
    `customer_id` int(11)      NOT NULL,
    `message`     varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `customer_id` (`customer_id`),
    CONSTRAINT `fk_notes_customers` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`)
        ON DELETE CASCADE
);

Tabela
Notas
Empregados

CREATE TABLE `notes_employees`
(
    `id`          int(11)      NOT NULL UNIQUE AUTO_INCREMENT,
    `employee_id` int(11)      NOT NULL,
    `message`     varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `employee_id` (`employee_id`),
    CONSTRAINT `fk_notes_employees` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`)
        ON DELETE CASCADE
);

Tabela
Notas
Fornecedores

CREATE TABLE `notes_suppliers`
(
    `id`          int(11)      NOT NULL UNIQUE AUTO_INCREMENT,
    `supplier_id` int(11)      NOT NULL,
    `message`     varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `supplier_id` (`supplier_id`),
    CONSTRAINT `fk_notes_suppliers` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`)
        ON DELETE CASCADE
);

Tabela
Fornecedores

CREATE TABLE `suppliers`
(
    `id`         int(11)        NOT NULL UNIQUE AUTO_INCREMENT,
    `name`       varchar(255)   NOT NULL,
    `address_id` int(11)        NOT NULL UNIQUE,
    `phone`      decimal(10, 0) NOT NULL UNIQUE,
    `email`      varchar(100)   NOT NULL UNIQUE,
    `nif`        decimal(10, 0) NOT NULL UNIQUE,
    PRIMARY KEY (`id`)
);

Tabela
Produtos_Fatura

CREATE TABLE `products_invoices`
(
    `id`         int(11) NOT NULL UNIQUE AUTO_INCREMENT,
    `invoice_id` int(11) NOT NULL,
    `product_id` int(11) NOT NULL,
    `quantity`   int(6)  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `invoice_id` (`invoice_id`),
    KEY `product_id` (`product_id`),
    CONSTRAINT `fk_products_invoices_invoices` FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`id`),
    CONSTRAINT `fk_products_invoices_products` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
        ON DELETE CASCADE
);

CREATE TABLE `products_quotes`
(
    `id`         int(11) NOT NULL UNIQUE AUTO_INCREMENT,
    `quote_id`   int(11) NOT NULL,
    `product_id` int(11) NOT NULL,
    `quantity`   int(6)  NOT NULL,
    PRIMARY KEY (`id`),
    KEY `quote_id` (`quote_id`),
    KEY `product_id` (`product_id`),
    CONSTRAINT `fk_products_quotes_quotes` FOREIGN KEY (`quote_id`) REFERENCES `quotes` (`id`),
    CONSTRAINT `fk_products_quotes_products` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
        ON DELETE CASCADE
);


CREATE TABLE `requests`
(
    `id`                int(11)      NOT NULL UNIQUE AUTO_INCREMENT,
    `date`              date         NOT NULL,
    `type`              varchar(100) NOT NULL,
    `quote_id`          int(11)      NOT NULL UNIQUE,
    `description`       varchar(100) NOT NULL,
    `intervention_date` date         NOT NULL,
    `invoice_id`        int(11)      NOT NULL UNIQUE,
    `paid`              BOOLEAN      NOT NULL,
    `payment_method`    varchar(100) NOT NULL,
    `payment_proof`     varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `quote_id` (`quote_id`),
    KEY `invoice_id` (`invoice_id`),
    CONSTRAINT `fk_requests_quotes` FOREIGN KEY (`quote_id`) REFERENCES `quotes` (`id`),
    CONSTRAINT `fk_requests_invoices` FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`id`)
        ON DELETE CASCADE
);


CREATE TABLE `quotes`
(
    `id`          int(11)      NOT NULL UNIQUE AUTO_INCREMENT,
    `customer_id` int(11)      NOT NULL,
    `date`        date         NOT NULL,
    `pdf`         varchar(255) NOT NULL UNIQUE,
    PRIMARY KEY (`id`),
    KEY `customer_id` (`customer_id`),
    CONSTRAINT `fk_quotes_customers` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`),
    ON DELETE CASCADE
);

insert into users (username, password)
values ('admin', '123');

Inserir
Clientes

INSERT INTO customers (name, address, phone, email, nif)
VALUES ('Tiago Matias', 'Rua das Flores', 912354567, 'bla@bla.com', 123456789);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('Pedro Santos', 'Rua da Flor', 912385567, 'cla@cla.com', 987654321);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('Nuno Soares', 'Rua D. Henrique', 934254712, 'nuno.soares@nsoares.com', 123542168);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('João Manuel', 'Rua D Manuel', 913254275, 'joao@joao.com', 254326857);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('João Faria', 'Rua Dr Manuel', 923651254, 'joao@bla.com', 241325124);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('João Nunes', 'Rua das Flores', 921531458, 'bla@cla.com', 254325617);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('Manuel Nunes', 'Rua das Flores', 921145632, '123@123.com', 254136521);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('Manuel Faria', 'Rua das Flores', 925635145, 'bla@bla.com', 236541235);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('Tiago Santos', 'Rua D Manuel', 925364214, '123@456.com', 245362157);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('Manuel Faria', 'Rua das Flores', 923654687, '123@234.com', 245321625);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('Tiago Manuel', 'Rua dos Abetos', 925345724, '456@123.com', 285463214);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('Manuel Faria', 'Rua D Manuel', 921365247, 'manuel@faria.com', 365214587);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('Tiago Fontes', 'Av Cmdt. Ramiro Correia', 925321254, 'tiagofontes123@gmail.com', 24123214);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('Miguel Martins', 'Rua das Flores n12', 932512452, 'miguelmartins@gmail.com', 243212541);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('João Martins', 'Rua Manuel Barbosa 12', 913020154, 'joao.martins123@gmail.com', 236254124);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('Miguel Monteiro', 'Rua D Henrique 12', 914125847, 'miguelmonteiro135@gmail.com', 263214258);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('Jorge Nunes', 'Rua Maria 123', 932142563, 'jorge.nunes@hotmail.com', 253214265);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('Maria Teresa', 'Rua José Manuel', 921325621, 'maria.teresa@yahoo.org', 253212421);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('José Maria', 'Rua Dom Manuel', 923532154, 'josemaria@yahoo.org', 232532124);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('Manuel Barbosa', 'Rua D Manuel', 912321456, 'manuel.barbosa@gmail.com', 213212546);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('Tiago Manuel', 'Rua D Tiago', 923232521, 'tiago.manuel@gmail.com', 212321231);
INSERT INTO customers (name, address, phone, email, nif)
VALUES ('Manuel Torres', 'Rua D Manuel', 9123214232, 'manue.torres@gmail.com', 213235621);

Inserir
empregados

INSERT INTO employees (name, address, phone, email, nif)
VALUES ('João Maria', 'Rua dos Abetos', 912345647, 'email@email.com', 123543276);
INSERT INTO employees (name, address, phone, email, nif)
VALUES ('José Maria', 'Rua Maria', 936524854, 'jose@maria.com', 265314257);
INSERT INTO employees (name, address, phone, email, nif)
VALUES ('Manuel Barbosa', 'Rua Maria Barbosa', 923652147, 'barbosa@maria.com', 241325624);
INSERT INTO employees (name, address, phone, email, nif)
VALUES ('Pedro Manuel', 'Rua Afonso Henriques 12', 923625412, 'pedro.manuel@yahoo.com', 253262142);
INSERT INTO employees (name, address, phone, email, nif)
VALUES ('Manuel Torres', 'Rua Maria Barbbosa', 923521247, 'manuel.torres@gmail.com', 253214527);
INSERT INTO employees (name, address, phone, email, nif)
VALUES ('Manuel Barbosa', 'Rua Pedro Soares', 913252458, 'manuel@barbosa.com', 24325136);
INSERT INTO employees (name, address, phone, email, nif)
VALUES ('Manuel Nunes', 'Rua das Flores', 923521425, 'manuelnunes89@gmail.com', 231254213);
INSERT INTO employees (name, address, phone, email, nif)
VALUES ('João Silva', 'Rua dos Abetos', 923124235, 'joaosilva@hotmail.com', 232521245);
INSERT INTO employees (name, address, phone, email, nif)
VALUES ('Pedro Manuel', 'Rua dos Abetos', 923124231, 'pedro.manuel@gmail.com', 213242123);
INSERT INTO employees (name, address, phone, email, nif)
VALUES ('Pedro Manuel', 'Rua dos Abetos', 923125321, 'pedro.manuel@gmail.com', 263212352);

Inserir
faturas

INSERT INTO invoices (customer_id, employee_id, date, products, pdf)
VALUES (1, 1, '2020-09-25', '1', '/home/pi/management/invoices/1.pdf');
INSERT INTO invoices (customer_id, employee_id, date, products, pdf)
VALUES (3, 1, '2020-09-25', '1', '/home/pi/management/invoices/2.pdf');
INSERT INTO invoices (customer_id, employee_id, date, products, pdf)
VALUES (5, 2, '2020-10-09', '2', '/home/pi/management/invoices/3.pdf');
INSERT INTO invoices (customer_id, employee_id, date, products, pdf)
VALUES (4, 1, '2020-10-09', '3', '/home/pi/management/invoices/4.pdf');

Inserir
produtos

INSERT INTO products (name, price, supplier_id, quantity, image)
values ('Peça 1', 50, 1, 10, 'home/pi/management/products/1.png');
INSERT INTO products (name, price, supplier_id, quantity, image)
values ('Peça 2', 20, 1, 10, 'home/pi/management/products/2.png');
INSERT INTO products (name, price, supplier_id, quantity, image)
values ('Peça 3', 100, 2, 5, 'home/pi/management/products/3.png');
INSERT INTO products (name, price, supplier_id, quantity, image)
VALUES ('Peça 4', 50, 1, 10, 'home/pi/management/products/4.png');
INSERT INTO products (name, price, supplier_id, quantity, image)
VALUES ('Peça 5', 20, 3, 10, 'home/pi/management/products/5.png');
INSERT INTO products (name, price, supplier_id, quantity, image)
VALUES ('Peça 6', 100, 2, 5, 'home/pi/management/products/6.png');

Inserir
produtos em faturas

INSERT INTO products_invoices (invoice_id, product_id, quantity)
VALUES (1, 1, 2);
INSERT INTO products_invoices (invoice_id, product_id, quantity)
VALUES (1, 2, 2);
INSERT INTO products_invoices (invoice_id, product_id, quantity)
VALUES (1, 3, 2);
INSERT INTO products_invoices (invoice_id, product_id, quantity)
VALUES (2, 1, 2);
INSERT INTO products_invoices (invoice_id, product_id, quantity)
VALUES (2, 2, 2);
INSERT INTO products_invoices (invoice_id, product_id, quantity)
VALUES (2, 3, 2);

Insirer
Notas Clientes

INSERT INTO notes_customers (customer_id, message)
VALUES (1, 'Falta Pagamento');
INSERT INTO notes_customers (customer_id, message)
VALUES (2, 'Falta Pagamento');

Inserir
Fornecedores

INSERT INTO suppliers (name, address, phone, email, nif)
VALUES ('Fornecedor 1', 'Rua das Flores', 912354567, 'bla@bla.com', 123456789);
INSERT INTO suppliers (name, address, phone, email, nif)
VALUES ('Fornecedor 2', 'Rua das Flores', 912354567, 'bla@bla.com', 123456789);
INSERT INTO suppliers (name, address, phone, email, nif)
VALUES ('Fornecedor 3', 'Rua das Flores', 912354567, 'bla@bla.com', 123456789);
INSERT INTO suppliers (name, address, phone, email, nif)
VALUES ('Fornecedor 4', 'Rua das Flores', 912354567, 'bla@bla.com', 123456789);
INSERT INTO suppliers (name, address, phone, email, nif)
VALUES ('Fornecedor 5', 'Rua das Flores', 912354567, 'bla@bla.com', 123456789);

Seleccionar
faturas por data

select *
from invoices
where date >= '2020-10-09'
  and date <= '2021-01-01' Seleccionar todas as faturas com nome de cliente e empregado

SELECT management.invoices.*
     , customers.name AS customer_name
     , employees.name AS employee_name

FROM invoices
         INNER JOIN customers ON customers.id = invoices.customer_id
         INNER JOIN employees ON employees.id = invoices.employee_id Selecionar todos os produtos com nome de fornecedor

SELECT management.products.*
     , suppliers.name AS supplier_name

FROM products
         INNER JOIN suppliers ON suppliers.id = products.supplier_id


