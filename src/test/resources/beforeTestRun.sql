SET foreign_key_checks = 0;

REPLACE INTO role (id, role) VALUES (1, 'ADMIN');
DELETE FROM role WHERE id != 1;
INSERT INTO role (id, role) VALUES (2, 'USER');
INSERT INTO role (id, role) VALUES (3, 'GUEST');

REPLACE INTO user (id, email, first_name, last_name, level, password, created_at, updated_at) VALUES
(1, 'admin@medinvention.ext.io', 'Marouan', 'MOHAMED', 1, '$2a$10$8ID5xYKQFY4V2X.PiMZTWeo8WQ4Utnhs69Aygux65Cbzmv/WRFiDy', NOW(), null);
DELETE FROM user WHERE id != 1;
INSERT INTO user (id, email, first_name, last_name, level, password, created_at, updated_at) VALUES
(2, 'user@medinvention.ext.io', 'Philippe', 'DUBOIS', 2, '$2a$10$8ID5xYKQFY4V2X.PiMZTWeo8WQ4Utnhs69Aygux65Cbzmv/WRFiDy', NOW(), NOW()),
(3, 'guest@medinvention.ext.io', 'Guest', 'MY', 3, '$2a$10$8ID5xYKQFY4V2X.PiMZTWeo8WQ4Utnhs69Aygux65Cbzmv/WRFiDy', NOW(), NOW());

REPLACE INTO user_role (role_id, user_id) VALUES
(1, 1);
DELETE FROM user_role WHERE role_id != 1 OR user_id != 1;
REPLACE INTO user_role (role_id, user_id) VALUES
(2, 2), (3, 3);

SET foreign_key_checks = 1;