CREATE TABLE social_links (
    id UUID PRIMARY KEY,
    instagram_url VARCHAR(255),
    x_url VARCHAR(255),
    linkedin_url VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Insert a default record
INSERT INTO social_links (id, instagram_url, x_url, linkedin_url) 
VALUES (gen_random_uuid(), 'https://instagram.com/', 'https://x.com/', 'https://linkedin.com/');
