CREATE TABLE `user`(
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(255) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `role_id` INT(11) NOT NULL,
    `status` BOOLEAN DEFAULT true,
    `created_by`         VARCHAR(255)   DEFAULT NULL,
    `last_modified_by`   VARCHAR(255)   DEFAULT NULL,
    `created_date`       DATETIME      DEFAULT NULL,
    `last_modified_date` DATETIME      DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `username_uq` (`username`) USING BTREE,
    UNIQUE KEY `email_uq`(`email`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;