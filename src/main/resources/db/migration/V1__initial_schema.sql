CREATE TABLE wallets (
    id BIGSERIAL PRIMARY KEY,
    balance NUMERIC(19, 2) NOT NULL,
    user_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE transactions (
    tid BIGSERIAL PRIMARY KEY,
    sender_id BIGINT,
    receiver_id BIGINT,
    transfer_amount NUMERIC(19, 2) NOT NULL,
    transaction_time TIMESTAMP NOT NULL,
    transaction_status VARCHAR(50) NOT NULL,
    transaction_type VARCHAR(50) NOT NULL
);