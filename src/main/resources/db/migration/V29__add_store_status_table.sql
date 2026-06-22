CREATE TABLE store_status (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    is_taking_orders BOOLEAN NOT NULL DEFAULT true,
    is_maintenance_mode BOOLEAN NOT NULL DEFAULT false,
    status_message TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Insert the single default row
INSERT INTO store_status (is_taking_orders, is_maintenance_mode, status_message)
VALUES (true, false, 'Welcome to the store!');
