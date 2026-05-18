CREATE TABLE activity_logs (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               username VARCHAR(255),
                               action VARCHAR(255),
                               details VARCHAR(255),
                               created_at TIMESTAMP
);