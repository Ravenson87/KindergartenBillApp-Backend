CREATE TABLE `kindergarten` (
    `id` INT (11) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR (255) NOT NULL,
    `account_id` INT (11) NOT NULL,
    `address` VARCHAR(255) NOT NULL,
    `telephone_number` INT(11) DEFAULT NULL,
    `email` VARCHAR(255) NOT NULL,
    PRIMARY KEY(`id`) USING BTREE,
    UNIQUE KEY `name_uq` (`name`) USING BTREE,
    UNIQUE KEY `telephone_number_uq`(`telephone_number`) USING BTREE,
    UNIQUE KEY `email_uq` (`email`) USING BTREE

) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;