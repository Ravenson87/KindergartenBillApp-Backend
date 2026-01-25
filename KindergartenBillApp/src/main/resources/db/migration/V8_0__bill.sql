CREATE TABLE `bill`(
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `year` SMALLINT NOT NULL,
    `month` VARCHAR(20) NOT NULL,
    `deadline` DATE DEFAULT NULL,
    `bill_code` VARCHAR(255),
    `payment_sum` DECIMAL(10,2) NOT NULL DEFAULT 0,
    `kindergarten_id` INT(11) NOT NULL,
    `child_id` INT(11) NOT NULL,
    `created_by`         VARCHAR(255)   DEFAULT NULL,
    `last_modified_by`   VARCHAR(255)   DEFAULT NULL,
    `created_date`       DATETIME      DEFAULT NULL,
    `last_modified_date` DATETIME      DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    CONSTRAINT `fk_bill_kindergarten` FOREIGN KEY (`kindergarten_id`) REFERENCES `kindergarten`(`id`) ON UPDATE CASCADE ON DELETE NO ACTION,
    CONSTRAINT `fk_bill_child` FOREIGN KEY (`child_id`) REFERENCES `child`(`id`) ON UPDATE CASCADE ON DELETE NO ACTION
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;