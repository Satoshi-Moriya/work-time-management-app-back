DROP TABLE IF EXISTS work_logs;

CREATE TABLE IF NOT EXISTS work_logs
(
  work_log_id            INT NOT NULL AUTO_INCREMENT,
  work_log_user_id       INT NOT NULL,
  work_log_date          DATE NOT NULL,
  work_log_start_time    DATETIME NOT NULL,
  work_log_end_time      DATETIME NOT NULL,
  work_log_seconds       MEDIUMINT NOT NULL,
  PRIMARY KEY (work_log_id)
);

INSERT INTO work_logs
  (work_log_id, work_log_user_id, work_log_date, work_log_start_time, work_log_end_time, work_log_seconds)
VALUES
  (0, 1, "2023-06-26", "2023-06-26 10:00:00", "2023-06-26 12:20:12", 8412);

INSERT INTO work_logs
  (work_log_id, work_log_user_id, work_log_date, work_log_start_time, work_log_end_time, work_log_seconds)
VALUES
  (0, 1, "2023-06-26", "2023-06-26 13:27:32", "2023-06-26 19:30:12", 21760);

INSERT INTO work_logs
  (work_log_id, work_log_user_id, work_log_date, work_log_start_time, work_log_end_time, work_log_seconds)
VALUES
  (0, 1, "2023-06-27", "2023-06-27 09:30:00", "2023-06-27 18:00:03", 30603);

INSERT INTO work_logs
  (work_log_id, work_log_user_id, work_log_date, work_log_start_time, work_log_end_time, work_log_seconds)
VALUES
  (0, 2, "2023-06-26", "2023-06-26 09:00:00", "2023-06-26 12:20:00", 12000);

INSERT INTO work_logs
  (work_log_id, work_log_user_id, work_log_date, work_log_start_time, work_log_end_time, work_log_seconds)
VALUES
  (0, 2, "2023-06-26", "2023-06-26 13:00:00", "2023-06-26 17:00:03", 14403);

INSERT INTO work_logs
  (work_log_id, work_log_user_id, work_log_date, work_log_start_time, work_log_end_time, work_log_seconds)
VALUES
  (0, 1, "2023-07-01", "2023-07-01 13:00:00", "2023-07-01 17:00:00", 14400);


DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users
(
  user_id              INT NOT NULL AUTO_INCREMENT,
  user_mail_address    VARCHAR(255) NOT NULL,
  user_password        VARCHAR(255) NOT NULL,
  created_at           DATETIME NOT NULL,
  updated_at           DATETIME,
  deleted_at           DATETIME,
  PRIMARY KEY (user_id),
  UNIQUE KEY (user_mail_address)
);

INSERT INTO users
  (user_id, user_mail_address, user_password, created_at, updated_at, deleted_at)
VALUES
  (0, "moriyas@example.com", "937e8d5fbb48bd4949536cd65b8d35c426b80d2f830c5c308e2cdec422ae2244", "2023-06-26 10:00:00", null, null);

INSERT INTO users
  (user_id, user_mail_address, user_password, created_at, updated_at, deleted_at)
VALUES
  (0, "test@example.com", "937e8d5fbb48bd4949536cd65b8d35c426b80d2f830c5c308e2cdec422ae2244", "2023-06-30 10:00:00", null, null);

INSERT INTO users
  (user_id, user_mail_address, user_password, created_at, updated_at, deleted_at)
VALUES
  (0, "ichirou@example.com", "6fec2a9601d5b3581c94f2150fc07fa3d6e45808079428354b868e412b76e6bb", "2023-07-01 10:00:00", "2023-07-03 10:00:00", "2023-07-05 10:00:00");

INSERT INTO users
  (user_id, user_mail_address, user_password, created_at, updated_at, deleted_at)
VALUES
  (0, "jirou@example.com", "6fec2a9601d5b3581c94f2150fc07fa3d6e45808079428354b868e412b76e6bb", "2023-07-01 10:00:00", "2023-07-03 10:00:00", null);

INSERT INTO users
  (user_id, user_mail_address, user_password, created_at, updated_at, deleted_at)
VALUES
  (0, "saburou@example.com", "6fec2a9601d5b3581c94f2150fc07fa3d6e45808079428354b868e412b76e6bb", "2023-07-01 10:00:00", null, "2023-07-03 10:00:00");