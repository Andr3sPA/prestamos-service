CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE public.tasks
(
    task_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title character varying,
    description character varying,
    priority character varying,
    completed boolean
);
CREATE TABLE states (
    state_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

-- Table: loan_type
CREATE TABLE loan_type (
    loan_type_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    minimum_amount NUMERIC(12,2) NOT NULL,
    maximum_amount NUMERIC(12,2) NOT NULL,
    interest_rate NUMERIC(5,2) NOT NULL,
    automatic_validation BOOLEAN DEFAULT FALSE
);

-- Table: loan_application
CREATE TABLE loan_application (
    application_id SERIAL PRIMARY KEY,
    amount NUMERIC(12,2) NOT NULL,
    term INT NOT NULL,
    email VARCHAR(255) NOT NULL,
    state_id INT NOT NULL,
    loan_type_id INT NOT NULL,
    CONSTRAINT fk_state
        FOREIGN KEY(state_id)
        REFERENCES states(state_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_loan_type
        FOREIGN KEY(loan_type_id)
        REFERENCES loan_type(loan_type_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);
