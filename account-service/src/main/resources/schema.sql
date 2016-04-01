DROP TABLE IF EXISTS account_addresses;
DROP TABLE IF EXISTS account_credit_cards;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS account;

CREATE TABLE account
(
  id              BIGINT(20) PRIMARY KEY NOT NULL,
  created_at      BIGINT(20),
  last_modified   BIGINT(20),
  account_number  VARCHAR(255),
  default_account BIT(1),
  user_id         VARCHAR(255)
);

DROP TABLE IF EXISTS address;

CREATE TABLE address
(
  id            BIGINT(20) PRIMARY KEY NOT NULL,
  created_at    BIGINT(20),
  last_modified BIGINT(20),
  address_type  INT(11),
  city          VARCHAR(255),
  country       VARCHAR(255),
  state         VARCHAR(255),
  street1       VARCHAR(255),
  street2       VARCHAR(255),
  zip_code      INT(11)
);

DROP TABLE IF EXISTS credit_card;

CREATE TABLE credit_card
(
  id            BIGINT(20) PRIMARY KEY NOT NULL,
  created_at    BIGINT(20),
  last_modified BIGINT(20),
  number        VARCHAR(255),
  type          INT(11)
);

CREATE TABLE customer
(
  id            BIGINT(20) PRIMARY KEY NOT NULL,
  created_at    BIGINT(20),
  last_modified BIGINT(20),
  email         VARCHAR(255),
  first_name    VARCHAR(255),
  last_name     VARCHAR(255),
  account_id    BIGINT(20),
  CONSTRAINT FK_jwt2qo9oj3wd7ribjkymryp8s FOREIGN KEY (account_id) REFERENCES account (id)
);
CREATE INDEX UK_jwt2qo9oj3wd7ribjkymryp8s ON customer (account_id);

CREATE TABLE account_addresses
(
  account_id   BIGINT(20) NOT NULL,
  addresses_id BIGINT(20) NOT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (account_id, addresses_id),
  CONSTRAINT FK_12vtt2maaj4yfrkbjmkf2qonq FOREIGN KEY (account_id) REFERENCES account (id),
  CONSTRAINT FK_r2ahplt2rqwvx1pnd5bbo7o70 FOREIGN KEY (addresses_id) REFERENCES address (id)
);
CREATE UNIQUE INDEX UK_r2ahplt2rqwvx1pnd5bbo7o70 ON account_addresses (addresses_id);

CREATE TABLE account_credit_cards
(
  account_id      BIGINT(20) NOT NULL,
  credit_cards_id BIGINT(20) NOT NULL,
  CONSTRAINT `PRIMARY` PRIMARY KEY (account_id, credit_cards_id),
  CONSTRAINT FK_b0tk2gq9bk6cggk5c4d33g3y4 FOREIGN KEY (credit_cards_id) REFERENCES credit_card (id),
  CONSTRAINT FK_lb5lhjdqfk50esr5g5733ppdo FOREIGN KEY (account_id) REFERENCES account (id)
);
CREATE UNIQUE INDEX UK_b0tk2gq9bk6cggk5c4d33g3y4 ON account_credit_cards (credit_cards_id);