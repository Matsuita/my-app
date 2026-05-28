ALTER TABLE accounts ADD CONSTRAINT mail UNIQUE (mail);

DELETE FROM accounts
WHERE id NOT IN (
  SELECT MIN(id)
  FROM accounts
  GROUP BY mail
);

INSERT INTO accounts (name, mail, password, sales_authority, accounts_authority, is_active)
VALUES ('admin', 'admin@test.com', 'admin', 2, 2, true)
ON CONFLICT (mail) DO NOTHING;
