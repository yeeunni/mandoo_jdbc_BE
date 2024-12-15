CREATE TABLE IF NOT EXISTS `Member` (
   `id` BIGINT NOT NULL AUTO_INCREMENT,
   `password` VARCHAR(45) NOT NULL,
   `email` VARCHAR(45) NOT NULL,
   `status` INT NOT NULL DEFAULT 0,
    `is_login`  INTEGER NULL DEFAULT 0,
   `nickname` VARCHAR(15) NOT NULL,
   `write_sell_post_count` INT NOT NULL DEFAULT 0,
   `like_sell_post_count` INT NOT NULL DEFAULT 0,
   `completed_sell_post_count` INT NULL DEFAULT 0,
   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `login_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
);


CREATE TABLE IF NOT EXISTS `SellPost` (
   `sell_post_id` BIGINT NOT NULL AUTO_INCREMENT,
   `member_id` BIGINT NOT NULL,
   `title` VARCHAR(45) NOT NULL,
   `price` INT NOT NULL,
   `description` VARCHAR(100) NULL,
   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   `status` INT NOT NULL DEFAULT 0,
   `city` VARCHAR(45) NOT NULL,
   `gu` VARCHAR(45) NULL,
   `dong` VARCHAR(45) NULL,
   `like_count` INT NOT NULL DEFAULT 0,
   `comment_count` INT NOT NULL DEFAULT 0,
   PRIMARY KEY (`sell_post_id`),
   FOREIGN KEY (`member_id`) REFERENCES `Member`(`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `Comment` (
   `id` BIGINT NOT NULL AUTO_INCREMENT,
   `content` VARCHAR(50) NOT NULL,
   `comment_status` INT NOT NULL,
   `member_id` BIGINT NOT NULL,
   `sell_post_id` BIGINT NOT NULL,
   `parent_comment_id` BIGINT NULL,
   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`),
   FOREIGN KEY (`member_id`) REFERENCES `Member`(`id`) ON DELETE CASCADE,
   FOREIGN KEY (`sell_post_id`) REFERENCES `SellPost`(`sell_post_id`) ON DELETE CASCADE,
   FOREIGN KEY (`parent_comment_id`) REFERENCES `Comment`(`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `Likes` (
   `id` BIGINT NOT NULL AUTO_INCREMENT,
   `member_id` BIGINT NOT NULL,
   `sell_post_id` BIGINT NOT NULL,
   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`),
   FOREIGN KEY (`member_id`) REFERENCES `Member`(`id`) ON DELETE CASCADE,
   FOREIGN KEY (`sell_post_id`) REFERENCES `SellPost`(`sell_post_id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `Postreport` (
   `id` BIGINT NOT NULL AUTO_INCREMENT,
   `sell_post_id` BIGINT NOT NULL,
   `member_id` BIGINT NOT NULL,
   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   `post_report_count` INT NOT NULL DEFAULT 0,
   PRIMARY KEY (`id`),
   FOREIGN KEY (`sell_post_id`) REFERENCES `SellPost`(`sell_post_id`) ON DELETE CASCADE,
   FOREIGN KEY (`member_id`) REFERENCES `Member`(`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `Category` (
   `category_id` BIGINT NOT NULL AUTO_INCREMENT,
   `name` VARCHAR(50) NOT NULL,
   PRIMARY KEY (`category_id`),
   UNIQUE (`name`)
);

CREATE TABLE IF NOT EXISTS `Commentreport` (
   `id` BIGINT NOT NULL AUTO_INCREMENT,
   `comment_report_count` INT NOT NULL DEFAULT 0,
   `member_id` BIGINT NOT NULL,
   `comment_id` BIGINT NOT NULL,
   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `updated_at` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`),
   FOREIGN KEY (`member_id`) REFERENCES `Member`(`id`) ON DELETE CASCADE,
   FOREIGN KEY (`comment_id`) REFERENCES `Comment`(`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `SellPostCategory` (
   `sell_post_category_id` BIGINT NOT NULL AUTO_INCREMENT,
   `category_id` BIGINT NOT NULL,
   `sell_post_id` BIGINT NOT NULL,
   PRIMARY KEY (`sell_post_category_id`),
   FOREIGN KEY (`sell_post_id`) REFERENCES `SellPost`(`sell_post_id`) ON DELETE CASCADE,
   FOREIGN KEY (`category_id`) REFERENCES `Category`(`category_id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `SellImagePath` (
   `image_id` BIGINT NOT NULL AUTO_INCREMENT,
   `sell_post_id` BIGINT NOT NULL,
   `path` VARCHAR(255) NOT NULL,
   PRIMARY KEY (`image_id`),
   FOREIGN KEY (`sell_post_id`) REFERENCES `SellPost`(`sell_post_id`) ON DELETE CASCADE
);

