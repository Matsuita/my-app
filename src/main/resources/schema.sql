CREATE TABLE categories (
  category_id SERIAL PRIMARY KEY,
  category_name VARCHAR(255) ,
  active_flg INTEGER NOT NULL　DEFAULT 1
);

CREATE TABLE sales (
  sale_id SERIAL PRIMARY KEY,
  sale_date DATE NOT NULL,
  account_id INTEGER NOT NULL,
  category_id INTEGER NOT NULL,
  trade_name VARCHAR(255) DEFAULT NULL,
  unit_price INTEGER NOT NULL,
  sale_number INTEGER NOT NULL,
  note VARCHAR(255) DEFAULT NULL,
  FOREIGN KEY (account_id) REFERENCES accounts(account_id),
  FOREIGN KEY (category_id) REFERENCES categories(category_id)
);
