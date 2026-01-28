CREATE TABLE `kindergarten_groups`(
                                   `id` INT(11) NOT NULL AUTO_INCREMENT,
                                   `kindergarten_id` INT (11) NOT NULL,
                                   `group_id` INT(11) NOT NULL,
                                   PRIMARY KEY (`id`) USING BTREE,
                                   UNIQUE KEY uq_kindergarten_group (kindergarten_id, group_id) USING BTREE,
                                   CONSTRAINT `fk_kindergarten_group_kindergarten_id` FOREIGN KEY (`kindergarten_id`) REFERENCES kindergarten(`id`) ON UPDATE CASCADE ON DELETE NO ACTION,
                                   CONSTRAINT `fk_kindergarten_group_group_id` FOREIGN KEY (`group_id`) REFERENCES `groups`(`id`) ON UPDATE CASCADE ON DELETE NO ACTION

) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;