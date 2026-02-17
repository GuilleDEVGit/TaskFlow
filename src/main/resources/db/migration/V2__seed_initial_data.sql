INSERT INTO users (id, username, email, password, role, createdAt) VALUES
(1,'testuser','test@test.com','password','ADMIN','2025-12-22 12:38:13'),
(5,'Admin','admin@gmail.com','$2a$10$euRmr5Ipq2jiY9.FZqwYxu04PXBITNeSjDY2M13F/tP/W74LrBrZO','ADMIN','2026-01-12 16:21:41'),
(9,'Guille','gg@mail.com','$2a$10$0Y0/pxEM08O5G/Z0NHkxlO6BpMQH2aLKtOivHaiuCM1u8yLI2SxRO','USER','2026-01-13 10:52:39'),
(17,'123','guilledev30@gmail.com','$2a$10$hloMk9giKeqDya04gOiHgeYKRIgzMSC3tpJezj2JwWZepm18FJF8e','USER','2026-02-11 11:07:38'),
(19,'ivan','guilledev30@gmail.com','$2a$10$9JKfHnCKQHnZJlyT3Gg2Keva3EPPWnY4C1cl7sok3E/DY2H0w/z0C','USER','2026-02-11 12:41:28');

INSERT INTO task (id, title, description, status, createdAt, dueDate, user_id) VALUES
(2,'Task real','Probando con la BD existente','IN_PROGRESS','2025-12-22 11:38:25','2025-01-10 17:00:00',1),
(3,'Task real 3','Probando con la BD existente','TODO','2026-01-12 10:54:54','2025-01-10 17:00:00',1);
