-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema barrel_of_books_test
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `barrel_of_books_test` ;

-- -----------------------------------------------------
-- Schema barrel_of_books_test
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `barrel_of_books_test` DEFAULT CHARACTER SET utf8 ;
USE `barrel_of_books_test` ;

-- -----------------------------------------------------
-- Table `barrel_of_books_test`.`book`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrel_of_books_test`.`book` ;

CREATE TABLE IF NOT EXISTS `barrel_of_books_test`.`book` (
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
-- Table `barrel_of_books_test`.`store`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrel_of_books_test`.`store` ;

CREATE TABLE IF NOT EXISTS `barrel_of_books_test`.`store` (
  `store_id` INT NOT NULL AUTO_INCREMENT,
  `address` VARCHAR(100) NOT NULL,
  `city` VARCHAR(45) NOT NULL,
  `state` VARCHAR(45) NOT NULL,
  `postal_code` VARCHAR(45) NOT NULL,
  `phone_number` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`store_id`),
  UNIQUE INDEX `storeId_UNIQUE` (`store_id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `barrel_of_books_test`.`genre`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrel_of_books_test`.`genre` ;

CREATE TABLE IF NOT EXISTS `barrel_of_books_test`.`genre` (
  `genre_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(200) NULL,
  PRIMARY KEY (`genre_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `barrel_of_books_test`.`transaction`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrel_of_books_test`.`transaction` ;

CREATE TABLE IF NOT EXISTS `barrel_of_books_test`.`transaction` (
  `transaction_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(45) NOT NULL,
  `date` DATETIME NOT NULL,
  PRIMARY KEY (`transaction_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `barrel_of_books_test`.`cart_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrel_of_books_test`.`cart_item` ;

CREATE TABLE IF NOT EXISTS `barrel_of_books_test`.`cart_item` (
  `cart_item_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(255) NOT NULL,
  `book_id` INT NOT NULL,
  `quantity` INT NOT NULL,
  `transaction_id` INT NULL,
  `is_purschased` TINYINT NOT NULL,
  PRIMARY KEY (`cart_item_id`),
  INDEX `fk_cart_book_bookId_idx` (`book_id` ASC) VISIBLE,
  INDEX `fk_cart_item_transactionId_idx` (`transaction_id` ASC) VISIBLE,
  CONSTRAINT `fk_cart_item_bookId`
    FOREIGN KEY (`book_id`)
    REFERENCES `barrel_of_books_test`.`book` (`book_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cart_item_transactionId`
    FOREIGN KEY (`transaction_id`)
    REFERENCES `barrel_of_books_test`.`transaction` (`transaction_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `barrel_of_books_test`.`genre_book`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrel_of_books_test`.`genre_book` ;

CREATE TABLE IF NOT EXISTS `barrel_of_books_test`.`genre_book` (
  `genre_id` INT NOT NULL,
  `book_id` INT NOT NULL,
  PRIMARY KEY (`genre_id`, `book_id`),
  INDEX `fk_genre_book_bookId_idx` (`book_id` ASC) VISIBLE,
  CONSTRAINT `fk_genre_book_genreId`
    FOREIGN KEY (`genre_id`)
    REFERENCES `barrel_of_books_test`.`genre` (`genre_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_genre_book_bookId`
    FOREIGN KEY (`book_id`)
    REFERENCES `barrel_of_books_test`.`book` (`book_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `barrel_of_books_test`.`store_book`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrel_of_books_test`.`store_book` ;

CREATE TABLE IF NOT EXISTS `barrel_of_books_test`.`store_book` (
  `store_id` INT NOT NULL,
  `book_id` INT NOT NULL,
  `quantity` INT NOT NULL,
  PRIMARY KEY (`store_id`, `book_id`),
  INDEX `fk_store_book_bookId_idx` (`book_id` ASC) VISIBLE,
  CONSTRAINT `fk_store_book_storeId`
    FOREIGN KEY (`store_id`)
    REFERENCES `barrel_of_books_test`.`store` (`store_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_store_book_bookId`
    FOREIGN KEY (`book_id`)
    REFERENCES `barrel_of_books_test`.`book` (`book_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


delimiter //
create procedure set_known_good_state()
begin
	
delete from genre;
alter table genre auto_increment = 1;

insert into genre(genre_id, name, description) values
(1, 'Fantasy', 'Fantasy description'),
(2, 'Adventure', 'Adventure description'),
(3, 'Romance', 'Romance description'),
(4, 'Horror', 'Horror description');
    
end //
-- 4. Change the statement terminator back to the original.
delimiter ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
