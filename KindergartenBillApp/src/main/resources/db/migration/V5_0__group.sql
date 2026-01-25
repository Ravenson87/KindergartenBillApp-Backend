CREATE TABLE `group`(
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `price` DECIMAL(10,2) NOT NULL,
    `discount` INT(11) DEFAULT 0,
    `active` BOOLEAN DEFAULT true,
    `created_by`         VARCHAR(255)   DEFAULT NULL,
    `last_modified_by`   VARCHAR(255)   DEFAULT NULL,
    `created_date`       DATETIME      DEFAULT NULL,
    `last_modified_date` DATETIME      DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uq_name` (`name`) USING BTREE

) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;