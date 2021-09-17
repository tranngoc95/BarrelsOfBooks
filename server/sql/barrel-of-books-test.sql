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
  `user_id` VARCHAR(255) NOT NULL,
  `date` DATETIME NOT NULL,
  `total` DECIMAL(6,2) NOT NULL,
  `employee_discount` TINYINT NOT NULL,
  `status` VARCHAR(255) NOT NULL,
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

delete from cart_item;
alter table cart_item auto_increment = 1;
delete from genre_book;
delete from genre;
alter table genre auto_increment = 1;
delete from store_book;
delete from store;
alter table store auto_increment = 1;
delete from book;
alter table book auto_increment = 1;
delete from `transaction`;
alter table `transaction` auto_increment = 1;



insert into genre(genre_id, name, description) values
(1, 'Fantasy', 'Fantasy description'),
(2, 'Adventure', 'Adventure description'),
(3, 'Romance', 'Romance description'),
(4, 'Horror', 'Horror description');

insert into book(book_id, title, description, price, author, quantity) values
  (1, 'hp', 'magic', 13.45, 'jk rowling', 45),
  (2, 'red robin', 'yum', 14.55, 'bear', 9),
  (3, 'got', 'fiction', 13.45, 'george rr martin', 45);
  
insert into genre_book(genre_id, book_id) values
  (1,1),
  (1,3),
  (2,2),
  
  insert into store(store_id, address, city, state, postal_code, phone_number) values 
  (1, 'address 1', 'Greenfield', 'WI', '12345', '12345678'),
  (2, 'address 2', 'Bloomington', 'MN', '43121', '12435678'),
  (3, 'address 3', 'Greendale', 'WI', '53121', '12435978')
  ;
  
  insert into store_book(store_id, book_id, quantity) values 
  (1,1,33),
  (2,3,60),
  (3,2,50);
  
  insert into transaction(transaction_id, user_id, date, total, employee_discount, status) values
  (1,1,'2020-09-09', '9.42', true, 'ORDERED'),
  (2,3,'2020-11-09', 14.55, false, 'SHIPPED'),
  (3,1,'2020-03-22', 9.42, true, 'DELIVERED');
  
  insert into cart_item(cart_item_id, user_id, book_id, quantity, transaction_id) values 
  (1,1,1,2,null),
  (2,1,1,1,1),
  (3,3,2,1,2),
  (4,1,3,1,3);

end //
-- 4. Change the statement terminator back to the original.
delimiter ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
