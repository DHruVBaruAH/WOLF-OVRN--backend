CREATE TABLE feature_carousel (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    image VARCHAR(2048) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    tagline VARCHAR(255),
    is_active BOOLEAN NOT NULL DEFAULT true,
    display_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER update_feature_carousel_modtime BEFORE UPDATE ON feature_carousel FOR EACH ROW EXECUTE FUNCTION update_modified_column();
