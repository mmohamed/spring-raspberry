SET foreign_key_checks = 0;

REPLACE INTO role (id, role) VALUES
(1, 'ADMIN');

REPLACE INTO user (id, email, first_name, last_name, level, password, created_at, updated_at) VALUES
(1, 'admin@medinvention.ext.io', 'Marouan', 'MOHAMED', 1, '$2a$10$8ID5xYKQFY4V2X.PiMZTWeo8WQ4Utnhs69Aygux65Cbzmv/WRFiDy', NOW(), null);

REPLACE INTO user_role (role_id, user_id) VALUES
(1, 1);

SET foreign_key_checks = 1;