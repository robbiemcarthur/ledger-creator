CREATE TABLE IF NOT EXISTS tax_year_summaries (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    tax_year_start INTEGER NOT NULL,
    total_income_amount DECIMAL(19, 2) NOT NULL,
    total_income_currency VARCHAR(3) NOT NULL,
    total_expenses_amount DECIMAL(19, 2) NOT NULL,
    total_expenses_currency VARCHAR(3) NOT NULL,
    category_totals_json TEXT,
    CONSTRAINT fk_tax_summary_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_tax_summary_user_id ON tax_year_summaries(user_id);
CREATE INDEX idx_tax_summary_tax_year ON tax_year_summaries(tax_year_start);
CREATE UNIQUE INDEX idx_tax_summary_user_year ON tax_year_summaries(user_id, tax_year_start);
