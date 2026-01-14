CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE ticket (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(30) NOT NULL,
    priority VARCHAR(30) NOT NULL,

    customer_id UUID NOT NULL,
    responsible_id UUID NOT NULL,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,

    CONSTRAINT fk_ticket_customer FOREIGN KEY (customer_id) REFERENCES users(id),
    CONSTRAINT fk_ticket_responsible FOREIGN KEY (responsible_id) REFERENCES users(id)
);

CREATE TABLE ticket_comment (
    id UUID PRIMARY KEY,

    ticket_id UUID NOT NULL,
    author_id UUID NOT NULL,

    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_comment_ticket FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    CONSTRAINT fk_comment_author FOREIGN KEY (author_id) REFERENCES users(id)

);

CREATE TABLE ticket_history (
    id UUID PRIMARY KEY,
    old_status VARCHAR(30) NOT NULL,
    new_status VARCHAR(30) NOT NULL,

    ticket_id UUID NOT NULL,
    changed_by UUID NOT NULL,

    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_history_ticket FOREIGN KEY (ticket_id) REFERENCES ticket(id),
    CONSTRAINT fk_history_user FOREIGN KEY (changed_by) REFERENCES users(id)

);

CREATE INDEX idx_ticket_status ON ticket(status);
CREATE INDEX idx_ticket_priority ON ticket(priority);
CREATE INDEX idx_ticket_customer ON ticket(customer_id);
CREATE INDEX idx_ticket_responsible ON ticket(responsible_id);
CREATE INDEX idx_ticket_comment_ticket ON ticket_comment(ticket_id);
CREATE INDEX idx_ticket_history_ticket ON ticket_history(ticket_id);