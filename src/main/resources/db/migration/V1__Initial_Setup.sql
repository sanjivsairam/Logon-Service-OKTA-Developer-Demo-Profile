CREATE SCHEMA IF NOT EXISTS logon_service_schema;

CREATE TABLE IF NOT EXISTS logon_service_schema.user_details
(
    user_id bigint NOT NULL,
    email text COLLATE pg_catalog."default",
    job_title text COLLATE pg_catalog."default",
    name text COLLATE pg_catalog."default",
    notification_alert boolean,
    profile text COLLATE pg_catalog."default",
    user_name text COLLATE pg_catalog."default",
    password text COLLATE pg_catalog."default",
    CONSTRAINT users_pkey PRIMARY KEY (user_id),
    CONSTRAINT unique_email UNIQUE (email)
);


CREATE SEQUENCE IF NOT EXISTS logon_service_schema.user_details_userid_seq
    INCREMENT 1
    START 2
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1
    OWNED BY logon_service_schema.user_details.user_id;

ALTER TABLE IF EXISTS logon_service_schema.user_details
    ALTER COLUMN user_id SET DEFAULT nextval('logon_service_schema.user_details_userid_seq'::regclass);

CREATE TABLE IF NOT EXISTS logon_service_schema.user_session
(
    id bigint NOT NULL,
    error_message text COLLATE pg_catalog."default",
    expiry_time timestamp without time zone,
    login_time timestamp without time zone,
    refresh_token text COLLATE pg_catalog."default",
    session_id text COLLATE pg_catalog."default",
    status text COLLATE pg_catalog."default",
    token_creation_time timestamp without time zone,
    user_token text COLLATE pg_catalog."default",
    user_id bigint,
    CONSTRAINT userssession_pkey PRIMARY KEY (id),
    CONSTRAINT fkh0q4kg67x7886os1konr1ufnn FOREIGN KEY (user_id)
        REFERENCES logon_service_schema.user_details (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE SEQUENCE IF NOT EXISTS logon_service_schema.user_session_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1
    OWNED BY logon_service_schema.user_session.id;

ALTER TABLE IF EXISTS logon_service_schema.user_session
    ALTER COLUMN id SET DEFAULT nextval('logon_service_schema.user_session_id_seq'::regclass);


CREATE TABLE IF NOT EXISTS logon_service_schema.audit_logging
(
audit_id integer NOT NULL,
description text COLLATE pg_catalog."default",
last_modified_by text COLLATE pg_catalog."default",
last_modified_on timestamp without time zone,
screen text COLLATE pg_catalog."default",
user_name text COLLATE pg_catalog."default",
CONSTRAINT auditlogging_pkey PRIMARY KEY (audit_id)
);

CREATE SEQUENCE IF NOT EXISTS logon_service_schema.audit_logging_auditid_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1
    OWNED BY logon_service_schema.audit_logging.audit_id;

ALTER TABLE IF EXISTS logon_service_schema.audit_logging
    ALTER COLUMN audit_id SET DEFAULT nextval('logon_service_schema.audit_logging_auditid_seq'::regclass);

INSERT INTO logon_service_schema.user_details(
	user_id, email, job_title, name, notification_alert, profile, user_name, password)
	VALUES (1, 'adminone@test.com', 'Monitor', 'AdminOne', true,
			'ADMIN', 'adminone@test.com',
			'$2a$12$6q0vgk/NuHd7TZ75NHXPs.8TdldG6pcUzmDyCG/7YWabp7PVQnjJm') ON CONFLICT DO NOTHING;