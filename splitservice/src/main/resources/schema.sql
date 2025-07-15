-- CREATE TABLE bills (
--     id SERIAL PRIMARY KEY,
--     name VARCHAR(100),
--     creator_id VARCHAR(50) NOT NULL,
--     extra_charges DECIMAL,
--     rounding DECIMAL,
--     created_at TIMESTAMP DEFAULT now(),
--     public_access_token VARCHAR(255))

-- CREATE TABLE bill_items (
--     id SERIAL PRIMARY KEY,
--     bill_id INTEGER REFERENCES bills(id) ON DELETE CASCADE,
--     item_name VARCHAR(100),
--     quantity INTEGER,
--     price DECIMAL
-- );


-- CREATE TABLE split_participants (
--     id SERIAL PRIMARY KEY,
--     bill_id INTEGER REFERENCES bills(id) ON DELETE CASCADE,
--     account_id VARCHAR(50),           
--     guest_name VARCHAR(100),

--     is_paid BOOLEAN DEFAULT FALSE,   
--     paid_at TIMESTAMP,                     
--     payment_proof_link TEXT,                

--     CHECK (
--         (account_id IS NOT NULL AND guest_name IS NULL) OR
--         (account_id IS NULL AND guest_name IS NOT NULL)
--     )
-- );


-- CREATE TABLE item_shares (
--     id SERIAL PRIMARY KEY,
--     bill_item_id INTEGER REFERENCES bill_items(id) ON DELETE CASCADE,
--     participant_id INTEGER REFERENCES split_participants(id) ON DELETE CASCADE,
--     quantity_share DECIMAL
-- );