-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema barrels_of_books
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `barrels_of_books` ;

-- -----------------------------------------------------
-- Schema barrels_of_books
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `barrels_of_books` DEFAULT CHARACTER SET utf8 ;
USE `barrels_of_books` ;

-- -----------------------------------------------------
-- Table `barrels_of_books`.`book`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrels_of_books`.`book` ;

CREATE TABLE IF NOT EXISTS `barrels_of_books`.`book` (
  `book_id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  `description` VARCHAR(200) NULL,
  `price` DECIMAL(6,2) NOT NULL,
  `author` VARCHAR(45) NOT NULL,
  `quantity` INT NOT NULL,
  PRIMARY KEY (`book_id`),
  UNIQUE INDEX `bookId_UNIQUE` (`book_id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `barrels_of_books`.`store`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrels_of_books`.`store` ;

CREATE TABLE IF NOT EXISTS `barrels_of_books`.`store` (
  `store_id` INT NOT NULL AUTO_INCREMENT,
  `address` VARCHAR(100) NOT NULL,
  `city` VARCHAR(45) NOT NULL,
  `state` VARCHAR(45) NOT NULL,
  `postalCode` VARCHAR(45) NOT NULL,
  `phoneNumber` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`store_id`),
  UNIQUE INDEX `storeId_UNIQUE` (`store_id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `barrels_of_books`.`category`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrels_of_books`.`category` ;

CREATE TABLE IF NOT EXISTS `barrels_of_books`.`category` (
  `category_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(200) NULL,
  PRIMARY KEY (`category_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BarrelsOfBooks`.`transaction`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrels_of_books`.`transaction` ;

CREATE TABLE IF NOT EXISTS `barrels_of_books`.`transaction` (
  `transaction_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(45) NOT NULL,
  `date` DATETIME NOT NULL,
  PRIMARY KEY (`transaction_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `barrels_of_books`.`cart_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrels_of_books`.`cart_item` ;

CREATE TABLE IF NOT EXISTS `barrels_of_books`.`cart_item` (
  `cart_item_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(255) NOT NULL,
  `book_id` INT NOT NULL,
  `quantity` INT NOT NULL,
  `transaction_id` INT NULL,
  `isPurschased` TINYINT NOT NULL,
  PRIMARY KEY (`cart_item_id`),
  INDEX `fk_cart_book_bookId_idx` (`book_id` ASC) VISIBLE,
  INDEX `fk_cart_item_transactionId_idx` (`transaction_id` ASC) VISIBLE,
  CONSTRAINT `fk_cart_item_bookId`
    FOREIGN KEY (`book_id`)
    REFERENCES `barrels_of_books`.`book` (`book_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cart_item_transactionId`
    FOREIGN KEY (`transaction_id`)
    REFERENCES `barrels_of_books`.`transaction` (`transaction_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `barrels_of_books`.`category_book`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrels_of_books`.`category_book` ;

CREATE TABLE IF NOT EXISTS `barrels_of_books`.`category_book` (
  `category_id` INT NOT NULL,
  `book_id` INT NOT NULL,
  PRIMARY KEY (`category_id`, `book_id`),
  INDEX `fk_category_book_bookId_idx` (`book_id` ASC) VISIBLE,
  CONSTRAINT `fk_category_book_categoryId`
    FOREIGN KEY (`category_id`)
    REFERENCES `barrels_of_books`.`category` (`category_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_category_book_bookId`
    FOREIGN KEY (`book_id`)
    REFERENCES `barrels_of_books`.`book` (`book_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `barrels_of_books`.`store_book`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrels_of_books`.`store_book` ;

CREATE TABLE IF NOT EXISTS `barrels_of_books`.`store_book` (
  `store_id` INT NOT NULL,
  `book_id` INT NOT NULL,
  `quantity` INT NOT NULL,
  PRIMARY KEY (`store_id`, `book_id`),
  INDEX `fk_store_book_bookId_idx` (`book_id` ASC) VISIBLE,
  CONSTRAINT `fk_store_book_storeId`
    FOREIGN KEY (`store_id`)
    REFERENCES `barrels_of_books`.`store` (`store_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_store_book_bookId`
    FOREIGN KEY (`book_id`)
    REFERENCES `barrels_of_booksbook_id`.`book` (`book_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
