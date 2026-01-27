CREATE TABLE `kindergarten_activity`(
                                     `id` INT(11) NOT NULL AUTO_INCREMENT,
                                     `kindergarten_id` INT (11) NOT NULL,
                                     `activity_id` INT(11) NOT NULL,
                                     PRIMARY KEY (`id`) USING BTREE,
                                     UNIQUE KEY uq_kindergarten_activity (kindergarten_id, activity_id) USING BTREE,
                                     CONSTRAINT `fk_kindergarten_activity_kindergarten` FOREIGN KEY (`kindergarten_id`) REFERENCES kindergarten(`id`) ON UPDATE CASCADE ON DELETE NO ACTION,
                                     CONSTRAINT `fk_kindergarten_activity_activity` FOREIGN KEY (`activity_id`) REFERENCES `activity`(`id`) ON UPDATE CASCADE ON DELETE NO ACTION

) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;