-- Table des candidatures, rattachées à un utilisateur par user_id.
CREATE TABLE job_applications (
    id           UUID          PRIMARY KEY,
    user_id      UUID          NOT NULL,
    company      VARCHAR(255)  NOT NULL,
    position     VARCHAR(255)  NOT NULL,
    url          VARCHAR(255),
    notes        VARCHAR(2000),
    status       VARCHAR(20)   NOT NULL,
    applied_date DATE          NOT NULL,
    created_at   TIMESTAMP     NOT NULL,
    updated_at   TIMESTAMP,
    CONSTRAINT fk_job_applications_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Les deux requêtes les plus fréquentes du repository filtrent par utilisateur,
-- puis par utilisateur + statut : les index suivent exactement ces deux accès.
CREATE INDEX idx_job_applications_user_id ON job_applications (user_id);
CREATE INDEX idx_job_applications_user_status ON job_applications (user_id, status);
