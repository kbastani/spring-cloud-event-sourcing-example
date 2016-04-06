DROP TABLE IF EXISTS cart_event;
CREATE TABLE cart_event
(
  id              BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  created_at      BIGINT(20),
  last_modified   BIGINT(20),
  cart_event_type INT(11),
  product_id      VARCHAR(255),
  quantity        INT(11),
  user_id         BIGINT(20)
);
CREATE INDEX IDX_CART_EVENT_USER ON cart_event (id, user_id);
ALTER TABLE cart_event AUTO_INCREMENT 0;
CREATE UNIQUE INDEX cart_event_id_uindex ON cart_event (id);