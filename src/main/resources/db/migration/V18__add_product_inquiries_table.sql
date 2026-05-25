CREATE TYPE inquiry_status AS ENUM ('PENDING', 'RESOLVED');

CREATE TABLE product_inquiries (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    product_id UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    status inquiry_status NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, product_id)
);

CREATE TRIGGER update_product_inquiries_modtime
    BEFORE UPDATE ON product_inquiries
    FOR EACH ROW EXECUTE FUNCTION update_modified_column();
