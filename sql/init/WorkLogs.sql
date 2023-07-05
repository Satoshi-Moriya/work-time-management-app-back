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