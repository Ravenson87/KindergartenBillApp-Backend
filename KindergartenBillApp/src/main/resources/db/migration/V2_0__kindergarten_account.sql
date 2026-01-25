CREATE TABLE `kindergarten_account`
(
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `bank_name` VARCHAR(255) NOT NULL,
    `account_number` VARCHAR(50) NOT NULL,
    `pib` VARCHAR(9) NOT NULL,
    `identification_number` VARCHAR(50) NOT NULL,
    `activity_code` INT(11),
    `created_by`         VARCHAR(255)   DEFAULT NULL,
    `last_modified_by`   VARCHAR(255)   DEFAULT NULL,
    `created_date`       DATETIME      DEFAULT NULL,
    `last_modified_date` DATETIME      DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `account_number_uq` (`account_number`) USING BTREE,
    UNIQUE KEY `identification_number_uq` (`identification_number`) USING BTREE

) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;