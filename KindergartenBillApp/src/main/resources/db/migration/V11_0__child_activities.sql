CREATE TABLE `child_activities`(
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `child_id` INT (11) NOT NULL,
    `activity_id` INT(11) NOT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY uq_child_activity (child_id, activity_id) USING BTREE,
    CONSTRAINT `fk_child_activity_child_id` FOREIGN KEY (`child_id`) REFERENCES child (`id`) ON UPDATE CASCADE ON DELETE NO ACTION,
    CONSTRAINT `fk_child_activity_activity_id` FOREIGN KEY (`activity_id`) REFERENCES activity(`id`) ON UPDATE CASCADE ON DELETE NO ACTION

) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;