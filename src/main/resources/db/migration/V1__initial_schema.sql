-- Initial schema for CreatorLedger
-- Creates tables for users, events, and income

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_users_email ON users(email);

-- Events table
CREATE TABLE IF NOT EXISTS events (
    id UUID PRIMARY KEY,
    event_date DATE NOT NULL,
    client_name VARCHAR(200) NOT NULL,
    description VARCHAR(1000) NOT NULL
);

CREATE INDEX idx_events_date ON events(event_date);

-- Income table
CREATE TABLE IF NOT EXISTS income (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    event_id UUID NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    received_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_income_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_income_event FOREIGN KEY (event_id) REFERENCES events(id)
);

CREATE INDEX idx_income_user_id ON income(user_id);
CREATE INDEX idx_income_event_id ON income(event_id);
CREATE INDEX idx_income_status ON income(status);
CREATE INDEX idx_income_received_date ON income(received_date);
