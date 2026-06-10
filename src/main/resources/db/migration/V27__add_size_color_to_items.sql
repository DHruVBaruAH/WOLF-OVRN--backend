ALTER TABLE order_items ADD COLUMN color VARCHAR(50);
ALTER TABLE cart_items ADD COLUMN size VARCHAR(50);
ALTER TABLE cart_items ADD COLUMN color VARCHAR(50);

ALTER TABLE cart_items DROP CONSTRAINT IF EXISTS cart_items_cart_id_product_id_key;
