DROP TABLE IF EXISTS invoice_orders;
DROP TABLE IF EXISTS orders_line_items;
DROP TABLE IF EXISTS invoice;
DROP TABLE IF EXISTS line_item;
DROP TABLE IF EXISTS order_event;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS address;

CREATE TABLE address (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  address_type VARCHAR(255),
  city         VARCHAR(255),
  country      VARCHAR(255),
  STATE        VARCHAR(255),
  street1      VARCHAR(255),
  street2      VARCHAR(255),
  zip_code     INTEGER
);

CREATE TABLE invoice (
  invoice_id         VARCHAR(255) PRIMARY KEY NOT NULL,
  created_at         BIGINT,
  last_modified      BIGINT,
  customer_id        VARCHAR(255),
  invoice_status     VARCHAR(255),
  billing_address_id BIGINT
);

CREATE TABLE invoice_orders (
  invoice_invoiceid VARCHAR(255) NOT NULL,
  orders_orderid    VARCHAR(255) NOT NULL,
  CONSTRAINT constraint_3 PRIMARY KEY (invoice_invoiceid, orders_orderid)
);

CREATE TABLE line_item (
  id         BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  name       VARCHAR(255),
  price      DOUBLE,
  product_id VARCHAR(255),
  quantity   INTEGER,
  tax        DOUBLE
);

CREATE TABLE order_event (
  id            BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
  created_at    BIGINT,
  last_modified BIGINT,
  order_id      VARCHAR(255),
  TYPE          VARCHAR(255)
);

CREATE TABLE orders (
  order_id            VARCHAR(255) PRIMARY KEY NOT NULL,
  created_at          BIGINT,
  last_modified       BIGINT,
  account_number      VARCHAR(255),
  order_status        INTEGER,
  shipping_address_id BIGINT
);

CREATE TABLE orders_line_items (
  orders_orderid VARCHAR(255) NOT NULL,
  line_items_id  BIGINT       NOT NULL,
  CONSTRAINT constraint_6 PRIMARY KEY (orders_orderid, line_items_id)
);

ALTER TABLE invoice
  ADD
  FOREIGN KEY (billing_address_id) REFERENCES address (id);

CREATE INDEX fk_c2fcw5sa2lj5p18js8pi68cld_index_9 ON invoice (billing_address_id);

ALTER TABLE invoice_orders
  ADD
  FOREIGN KEY (invoice_invoiceid) REFERENCES invoice (invoice_id);

ALTER TABLE invoice_orders
  ADD
  FOREIGN KEY (orders_orderid) REFERENCES orders (order_id);

CREATE UNIQUE INDEX uk_ivfpej2xy47jq7s12a6v0epn6_index_3 ON invoice_orders (orders_orderid);

CREATE INDEX fk_messjw8yb9yoewmjv20ycl44q_index_3 ON invoice_orders (invoice_invoiceid);

ALTER TABLE orders
  ADD
  FOREIGN KEY (shipping_address_id) REFERENCES address (id);

CREATE INDEX fk_sdv8vvdhj9gxm0dfoeh2rqvkh_index_8 ON orders (shipping_address_id);

ALTER TABLE orders_line_items
  ADD
  FOREIGN KEY (line_items_id) REFERENCES line_item (id);

ALTER TABLE orders_line_items
  ADD
  FOREIGN KEY (orders_orderid) REFERENCES orders (order_id);

CREATE UNIQUE INDEX uk_frp259x5fvoyc35w6dd1rnj84_index_6 ON orders_line_items (line_items_id);

CREATE INDEX fk_sbs9bq1hgkawxyvau17jhuvkx_index_6 ON orders_line_items (orders_orderid);
