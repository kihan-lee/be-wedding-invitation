SET search_path TO "wedding-invitation";

CREATE TABLE IF NOT EXISTS admin
(
    id         BIGSERIAL    NOT NULL,
    username   VARCHAR(50)  NOT NULL,
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at TIMESTAMP    NOT NULL DEFAULT now(),
    CONSTRAINT pk_admin PRIMARY KEY (id),
    CONSTRAINT uq_admin_username UNIQUE (username)
    );

CREATE TABLE IF NOT EXISTS guest
(
    id         BIGSERIAL   NOT NULL,
    name       VARCHAR(50) NOT NULL,
    side       VARCHAR(10) NOT NULL,
    token      VARCHAR(36) NOT NULL,
    is_active  BOOLEAN     NOT NULL DEFAULT true,
    created_at TIMESTAMP   NOT NULL DEFAULT now(),
    CONSTRAINT pk_guest PRIMARY KEY (id),
    CONSTRAINT uq_guest_token UNIQUE (token),
    CONSTRAINT uq_guest_name UNIQUE (name),
    CONSTRAINT chk_guest_side CHECK (side IN ('GROOM', 'BRIDE'))
    );

CREATE TABLE IF NOT EXISTS message
(
    id          BIGSERIAL   NOT NULL,
    guest_id    BIGINT,
    guest_name  VARCHAR(50),
    access_type VARCHAR(10) NOT NULL,
    content     TEXT        NOT NULL,
    created_at  TIMESTAMP   NOT NULL DEFAULT now(),
    CONSTRAINT pk_message PRIMARY KEY (id),
    CONSTRAINT fk_message_guest FOREIGN KEY (guest_id) REFERENCES guest (id),
    CONSTRAINT chk_message_access_type CHECK (access_type IN ('LINK', 'QR'))
    );

CREATE TABLE IF NOT EXISTS invitation_view
(
    id          BIGSERIAL   NOT NULL,
    guest_id    BIGINT,
    access_type VARCHAR(10) NOT NULL,
    viewed_at   TIMESTAMP   NOT NULL DEFAULT now(),
    CONSTRAINT pk_invitation_view PRIMARY KEY (id),
    CONSTRAINT fk_invitation_view_guest FOREIGN KEY (guest_id) REFERENCES guest (id),
    CONSTRAINT chk_invitation_view_access_type CHECK (access_type IN ('LINK', 'QR'))
    );