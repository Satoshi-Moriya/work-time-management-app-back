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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
  user_email           VARCHAR(255) NOT NULL,
  user_password        VARCHAR(255) NOT NULL,
  created_at           DATETIME NOT NULL,
  updated_at           DATETIME,
  deleted_at           DATETIME,
  PRIMARY KEY (user_id),
  UNIQUE KEY (user_email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO users
  (user_id, user_email, user_password, created_at, updated_at, deleted_at)
VALUES
  (0, "moriyas@example.com", "$2a$08$5K4IKtevp8CisDhWnL9GPu1Htp5.8rzXG8ci8upb2MIZMMTiiq8bi", "2023-06-26 10:00:00", null, null);

INSERT INTO users
  (user_id, user_email, user_password, created_at, updated_at, deleted_at)
VALUES
  (0, "test@example.com", "$2a$08$5K4IKtevp8CisDhWnL9GPu1Htp5.8rzXG8ci8upb2MIZMMTiiq8bi", "2023-06-30 10:00:00", null, null);

INSERT INTO users
  (user_id, user_email, user_password, created_at, updated_at, deleted_at)
VALUES
  (0, "ichirou@example.com", "$2a$08$5K4IKtevp8CisDhWnL9GPu1Htp5.8rzXG8ci8upb2MIZMMTiiq8bi", "2023-07-01 10:00:00", "2023-07-03 10:00:00", "2023-07-05 10:00:00");

INSERT INTO users
  (user_id, user_email, user_password, created_at, updated_at, deleted_at)
VALUES
  (0, "jirou@example.com", "$2a$08$5K4IKtevp8CisDhWnL9GPu1Htp5.8rzXG8ci8upb2MIZMMTiiq8bi", "2023-07-01 10:00:00", "2023-07-03 10:00:00", null);

INSERT INTO users
  (user_id, user_email, user_password, created_at, updated_at, deleted_at)
VALUES
  (0, "saburou@example.com", "$2a$08$5K4IKtevp8CisDhWnL9GPu1Htp5.8rzXG8ci8upb2MIZMMTiiq8bi", "2023-07-01 10:00:00", null, "2023-07-03 10:00:00");

DROP TABLE IF EXISTS refresh_token;

CREATE TABLE IF NOT EXISTS refresh_token
(
  refresh_token_id     INT NOT NULL AUTO_INCREMENT,
  user_email           VARCHAR(255) NOT NULL,
  refresh_token        VARCHAR(255) NOT NULL,
  refresh_token_iat    DATETIME NOT NULL,
  PRIMARY KEY (refresh_token_id),
  UNIQUE KEY (user_email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS record_items;

CREATE TABLE IF NOT EXISTS record_items
(
  record_item_id      INT NOT NULL AUTO_INCREMENT,
  user_id             INT NOT NULL,
  record_item_name    VARCHAR(255) NOT NULL,
  PRIMARY KEY (record_item_id),
  UNIQUE KEY(user_id, record_item_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO record_items
  (record_item_id, user_id, record_item_name)
VALUES
  (0, 1, "ドラム練習");

INSERT INTO record_items
  (record_item_id, user_id, record_item_name)
VALUES
  (0, 1, "仕事稼働時間");

INSERT INTO record_items
  (record_item_id, user_id, record_item_name)
VALUES
  (0, 2, "稼働時間");