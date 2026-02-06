CREATE TABLE products (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    owner_id UUID NOT NULL,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(300),
    quantity INTEGER NOT NULL CHECK ( quantity >= 0),
    price NUMERIC(10, 2) NOT NULL CHECK ( price >= 0 )
);

CREATE INDEX idx_products_owner_id ON products(owner_id);