CREATE TABLE `child`(
    `id` INT(11) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `surname` VARCHAR(255) NOT NULL,
    `sibling_order` SMALLINT UNSIGNED DEFAULT 1,
    `birthday` DATE DEFAULT NULL,
    `status` BOOLEAN DEFAULT true,
    `parent_id` INT(11) NOT NULL,
    `group_id` INT(11) NOT NULL,
    `kindergarten_id` INT(11) NOT NULL,
    `created_by`         VARCHAR(255)   DEFAULT NULL,
    `last_modified_by`   VARCHAR(255)   DEFAULT NULL,
    `created_date`       DATETIME      DEFAULT NULL,
    `last_modified_date` DATETIME      DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `child_uq`  (`name`, `surname`, `parent_id`),
    CONSTRAINT `fk_child_group` FOREIGN KEY (`group_id`) REFERENCES `group`(`id`)
                    ON UPDATE CASCADE ON DELETE NO ACTION,
    CONSTRAINT `fk_child_kindergarten` FOREIGN KEY (`kindergarten_id`) REFERENCES kindergarten(`id`)
                    ON UPDATE CASCADE ON DELETE NO ACTION,
    CONSTRAINT `fk_child_parent` FOREIGN KEY (`parent_id`) REFERENCES parent(`id`)
        ON UPDATE CASCADE ON DELETE NO ACTION


) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;