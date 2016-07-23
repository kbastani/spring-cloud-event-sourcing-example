DROP TABLE IF EXISTS catalog_products;
DROP TABLE IF EXISTS shipment_inventories;
DROP TABLE IF EXISTS catalog;
DROP TABLE IF EXISTS shipment;
DROP TABLE IF EXISTS inventory;
DROP TABLE IF EXISTS warehouse;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS address;

CREATE TABLE address (
  id       BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  city     VARCHAR(255),
  country  VARCHAR(255),
  STATE    VARCHAR(255),
  street1  VARCHAR(255),
  street2  VARCHAR(255),
  zip_code INTEGER
);

CREATE TABLE catalog (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  catalog_number BIGINT,
  name           VARCHAR(255)
);

CREATE TABLE catalog_products (
  catalog_id  BIGINT NOT NULL,
  products_id BIGINT NOT NULL
);

CREATE TABLE inventory (
  id               BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  inventory_number VARCHAR(255),
  status           VARCHAR(255),
  product_id       BIGINT,
  warehouse_id     BIGINT
);

CREATE TABLE product (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  description TEXT,
  name        VARCHAR(255),
  product_id  VARCHAR(255),
  unit_price  DOUBLE
);

CREATE TABLE shipment (
  id                  BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  shipment_status     VARCHAR(255),
  delivery_address_id BIGINT,
  from_warehouse_id   BIGINT
);

CREATE TABLE shipment_inventories (
  shipment_id    BIGINT NOT NULL,
  inventories_id BIGINT NOT NULL,
  CONSTRAINT constraint_9 PRIMARY KEY (shipment_id, inventories_id)
);

CREATE TABLE warehouse (
  id         BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  name       VARCHAR(255),
  address_id BIGINT
);

ALTER TABLE catalog_products
  ADD
  FOREIGN KEY (catalog_id) REFERENCES catalog (id);

ALTER TABLE catalog_products
  ADD
  FOREIGN KEY (products_id) REFERENCES product (id);

CREATE UNIQUE INDEX uk_owhgqfid9t53qkbst5q9worum_index_7 ON catalog_products (products_id);

CREATE INDEX fk_dyk0dwcaumjs8hfni3tffkse1_index_7 ON catalog_products (catalog_id);

ALTER TABLE inventory
  ADD
  FOREIGN KEY (product_id) REFERENCES product (id);

ALTER TABLE inventory
  ADD
  FOREIGN KEY (warehouse_id) REFERENCES warehouse (id);

CREATE INDEX fk_ce3rbi3bfstbvvyne34c1dvyv_index_2 ON inventory (product_id);

CREATE INDEX fk_t4xjpic3v3ayluu40ty85imr6_index_2 ON inventory (warehouse_id);

ALTER TABLE shipment
  ADD
  FOREIGN KEY (delivery_address_id) REFERENCES address (id);

ALTER TABLE shipment
  ADD
  FOREIGN KEY (from_warehouse_id) REFERENCES warehouse (id);

CREATE INDEX fk_655icxuqmet6k2xvb8pansgsr_index_f ON shipment (from_warehouse_id);

CREATE INDEX fk_mlkdhyy0e5or58oh63l7u41mg_index_f ON shipment (delivery_address_id);

ALTER TABLE shipment_inventories
  ADD
  FOREIGN KEY (inventories_id) REFERENCES inventory (id);

ALTER TABLE shipment_inventories
  ADD
  FOREIGN KEY (shipment_id) REFERENCES shipment (id);

CREATE UNIQUE INDEX uk_kt7w1n7kkl2310gbwwasqrcp3_index_9 ON shipment_inventories (inventories_id);

CREATE INDEX fk_fbdxaqjbnyf0y7j0p6tld2y0d_index_9 ON shipment_inventories (shipment_id);

ALTER TABLE warehouse
  ADD
  FOREIGN KEY (address_id) REFERENCES address (id);

CREATE INDEX fk_5hyew1b3bewu839bc54o2jo05_index_2 ON warehouse (address_id);
