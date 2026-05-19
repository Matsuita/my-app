USE teamb;

ALTER TABLE accounts 
ADD sales_authority TINYINT NULL,
ADD accounts_authority TINYINT NULL;


UPDATE accounts
SET 
    sales_authority = CASE authority
                        WHEN '0' THEN 0
                        WHEN '1' THEN 2
                        WHEN '2' THEN 0
                        WHEN '3' THEN 2
                        ELSE 0
                    END,


    accounts_authority = CASE authority
                        WHEN '0' THEN 0
                        WHEN '1' THEN 0
                        WHEN '2' THEN 2
                        WHEN '3' THEN 2
                        ELSE 0
                    END;

ALTER TABLE accounts MODIFY sales_authority TINYINT NOT NULL;
ALTER TABLE accounts MODIFY accounts_authority TINYINT NOT NULL;
ALTER TABLE accounts MODIFY authority bit(2) NULL;



--最後の行「bit(2)」は現在のデータベースの型とサイズ

