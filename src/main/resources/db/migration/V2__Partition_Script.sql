ALTER TABLE logon_service_schema.audit_logging ADD last_modified_date date NULL;

update  logon_service_schema.audit_logging set last_modified_date= to_date(substring(cast(last_modified_on as text),1, 10),'YYYY-MM-DD');

CREATE TABLE logon_service_schema.audit_logging_b
(
	audit_id int4 NOT NULL DEFAULT nextval('logon_service_schema.audit_logging_auditid_seq'::regclass),
	description text NULL,
	last_modified_by text NULL,
	last_modified_on timestamp NULL,
	last_modified_date date,
	screen text NULL,
	user_name text NULL
)
partition by Range(last_modified_date);

CREATE TABLE logon_service_schema.audit_logging_20220119 PARTITION of logon_service_schema.audit_logging_b
FOR VALUES FROM ('2022-01-19') TO (maxvalue);

insert into logon_service_schema.audit_logging_b  
select audit_id,description,last_modified_by,last_modified_on,
to_date(substring(cast(last_modified_on as text),1, 10),'YYYY-MM-DD') as last_modified_date,
screen, user_name
from logon_service_schema.audit_logging;

ALTER TABLE IF EXISTS logon_service_schema.audit_logging
RENAME TO audit_logging_old;
	
ALTER TABLE IF EXISTS logon_service_schema.audit_logging_b
RENAME TO audit_logging;

ALTER SEQUENCE logon_service_schema.audit_logging_auditid_seq OWNED BY NONE;

DROP TABLE IF EXISTS logon_service_schema.audit_logging_old;