CREATE TABLE colors (
    id UUID PRIMARY KEY,
    color_name VARCHAR(255) NOT NULL,
    hex_code VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sizes (
    id UUID PRIMARY KEY,
    size_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE product_colors (
    product_id UUID NOT NULL,
    color_id UUID NOT NULL,
    PRIMARY KEY (product_id, color_id),
    CONSTRAINT fk_pc_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_pc_color FOREIGN KEY (color_id) REFERENCES colors(id) ON DELETE CASCADE
);

CREATE TABLE product_sizes (
    product_id UUID NOT NULL,
    size_id UUID NOT NULL,
    PRIMARY KEY (product_id, size_id),
    CONSTRAINT fk_ps_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT fk_ps_size FOREIGN KEY (size_id) REFERENCES sizes(id) ON DELETE CASCADE
);

ALTER TABLE products DROP COLUMN IF EXISTS available_sizes;
