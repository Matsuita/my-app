INSERT INTO categories (
  category_id,
  category_name,
  active_flg
) VALUES (1, '果物', 1),
(2, '飲み物', 1),
(3, 'その他', 1);

INSERT INTO sales (
  sale_date,
  account_id,
  category_id,
  trade_name,
  unit_price,
  sale_number,
  note
) VALUES (
  '2026-05-28',
  6,
  1,
  'みかん',
  500,
  3,
  'テス'
);


