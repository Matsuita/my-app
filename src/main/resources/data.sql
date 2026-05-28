INSERT INTO sale (
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
  'テスト'
);

INSERT INTO category (
  category_id,
  category_name,
  active_flg
) VALUES (
  2,
  '野菜',
  1
);
