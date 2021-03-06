CREATE TABLE ${SEMA_ADI}.TRAINING (ID INT8 NOT NULL, ACCESS_TYPE VARCHAR(30), DESCRIPTOION VARCHAR(100), LANGUAGE VARCHAR(30), NAME VARCHAR(20), PROCESS_TYPE VARCHAR(30), VERSIYON INT8 NOT NULL, ID_USER INT8, PRIMARY KEY (ID));
CREATE TABLE ${SEMA_ADI}.TRAINING_STEP (ID INT8 NOT NULL, CODE_EXPLAINATION TEXT, STEP_DESCRITION VARCHAR(100), STEP_NAME VARCHAR(20), STEP_NO VARCHAR(100), VERSIYON INT8 NOT NULL, PRIMARY KEY (ID));
CREATE TABLE ${SEMA_ADI}.TRAINING_STEP_RELATION (ID INT8 NOT NULL, VERSIYON INT8 NOT NULL, ID_TRAINING INT8, ID_TRAINING_STEP INT8, PRIMARY KEY (ID));
CREATE TABLE ${AUDIT_SEMA_ADI}.TRAINING_AUD (ID INT8 NOT NULL, ID_REVINFO INT4 NOT NULL, REV_TYPE INT2, ACCESS_TYPE VARCHAR(30), DESCRIPTOION VARCHAR(100), LANGUAGE VARCHAR(30), NAME VARCHAR(20), PROCESS_TYPE VARCHAR(30), ID_USER INT8, PRIMARY KEY (ID, ID_REVINFO));
CREATE TABLE ${AUDIT_SEMA_ADI}.TRAINING_STEP_AUD (ID INT8 NOT NULL, ID_REVINFO INT4 NOT NULL, REV_TYPE INT2, CODE_EXPLAINATION TEXT, STEP_DESCRITION VARCHAR(100), STEP_NAME VARCHAR(20), STEP_NO VARCHAR(100), PRIMARY KEY (ID, ID_REVINFO));
CREATE TABLE ${AUDIT_SEMA_ADI}.TRAINING_STEP_RELATION_AUD (ID INT8 NOT NULL, ID_REVINFO INT4 NOT NULL, REV_TYPE INT2, ID_TRAINING INT8, ID_TRAINING_STEP INT8, PRIMARY KEY (ID, ID_REVINFO));
CREATE INDEX IX_TRAINING_USER ON ${SEMA_ADI}.TRAINING (ID_USER);
CREATE INDEX IX_TRAINING_RELATION_TRAINING ON ${SEMA_ADI}.TRAINING_STEP_RELATION (ID_TRAINING);
CREATE INDEX IX_TRAINING_STEP_RELATION ON ${SEMA_ADI}.TRAINING_STEP_RELATION (ID_TRAINING_STEP);
ALTER TABLE ${SEMA_ADI}.TRAINING ADD CONSTRAINT FK_TRAINING_USER FOREIGN KEY (ID_USER) REFERENCES ${SEMA_ADI}.USER;
ALTER TABLE ${SEMA_ADI}.TRAINING_STEP_RELATION ADD CONSTRAINT FK_TRAINING_RELATION_TRAINING FOREIGN KEY (ID_TRAINING) REFERENCES ${SEMA_ADI}.TRAINING ON DELETE CASCADE;
ALTER TABLE ${SEMA_ADI}.TRAINING_STEP_RELATION ADD CONSTRAINT FK_TRAINING_STEP_RELATION FOREIGN KEY (ID_TRAINING_STEP) REFERENCES ${SEMA_ADI}.TRAINING_STEP;
ALTER TABLE ${AUDIT_SEMA_ADI}.TRAINING_AUD ADD CONSTRAINT FK_HGHDCQC3H6R5V70RLBQRR69I1 FOREIGN KEY (ID_REVINFO) REFERENCES ${AUDIT_SEMA_ADI}.REVINFO;
ALTER TABLE ${AUDIT_SEMA_ADI}.TRAINING_STEP_AUD ADD CONSTRAINT FK_KD6BYO9L0W2GR53WBN6PDWEF FOREIGN KEY (ID_REVINFO) REFERENCES ${AUDIT_SEMA_ADI}.REVINFO;
ALTER TABLE ${AUDIT_SEMA_ADI}.TRAINING_STEP_RELATION_AUD ADD CONSTRAINT FK_QRLPFLNNM4T9LBSDOE83H5LRJ FOREIGN KEY (ID_REVINFO) REFERENCES ${AUDIT_SEMA_ADI}.REVINFO;
CREATE SEQUENCE ${SEMA_ADI}.TRAINING_ID_SEQ;
CREATE SEQUENCE ${SEMA_ADI}.TRAINING_STEP_ID_SEQ;
CREATE SEQUENCE ${SEMA_ADI}.TRAINING_STEP_RELATION_ID_SEQ;