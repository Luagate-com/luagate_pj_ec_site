CREATE DATABASE IF NOT EXISTS ec_site DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE ec_site;

CREATE TABLE IF NOT EXISTS goods (
  id BIGINT NOT NULL AUTO_INCREMENT,
  code VARCHAR(32) NOT NULL,
  name VARCHAR(255) NOT NULL,
  description TEXT NOT NULL,
  price INT NOT NULL,
  category VARCHAR(64) NOT NULL,
  image_url VARCHAR(1024) NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_goods_code (code),
  KEY idx_goods_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS stocks (
  good_id BIGINT NOT NULL,
  quantity INT NOT NULL,
  updated_at DATETIME NOT NULL,
  PRIMARY KEY (good_id),
  CONSTRAINT fk_stocks_goods FOREIGN KEY (good_id) REFERENCES goods (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT NOT NULL AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  last_name VARCHAR(32) NOT NULL,
  first_name VARCHAR(32) NOT NULL,
  address VARCHAR(255) NOT NULL,
  card_number_last4 VARCHAR(4) NULL,
  card_brand VARCHAR(32) NULL,
  card_exp_month TINYINT NULL,
  card_exp_year SMALLINT NULL,
  card_name VARCHAR(255) NULL,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS orders (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  ordered_at DATETIME NOT NULL,
  total_amount INT NOT NULL,
  created_at DATETIME NOT NULL,
  PRIMARY KEY (id),
  KEY idx_orders_user (user_id),
  CONSTRAINT fk_orders_users FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS order_items (
  id BIGINT NOT NULL AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  good_id BIGINT NOT NULL,
  unit_price INT NOT NULL,
  quantity INT NOT NULL,
  created_at DATETIME NOT NULL,
  PRIMARY KEY (id),
  KEY idx_order_items_order (order_id),
  KEY idx_order_items_good (good_id),
  CONSTRAINT fk_order_items_orders FOREIGN KEY (order_id) REFERENCES orders (id),
  CONSTRAINT fk_order_items_goods FOREIGN KEY (good_id) REFERENCES goods (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
