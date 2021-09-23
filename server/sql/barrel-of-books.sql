-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema barrel_of_books
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `barrel_of_books` ;

-- -----------------------------------------------------
-- Schema barrel_of_books
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `barrel_of_books` DEFAULT CHARACTER SET utf8 ;
USE `barrel_of_books` ;

-- -----------------------------------------------------
-- Table `barrel_of_books`.`book`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrel_of_books`.`book` ;

CREATE TABLE IF NOT EXISTS `barrel_of_books`.`book` (
  `book_id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(200) NOT NULL,
  `description` VARCHAR(2000) NULL,
  `price` DECIMAL(6,2) NOT NULL,
  `author` VARCHAR(45) NOT NULL,
  `quantity` INT NOT NULL,
  `publisher` VARCHAR(45),
  `language` VARCHAR(45),
  `pages` INT,
  `age_range` VARCHAR(45),
  `dimensions` VARCHAR(45),
  `isbn13` VARCHAR(45),
  PRIMARY KEY (`book_id`),
  UNIQUE INDEX `bookId_UNIQUE` (`book_id` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `barrel_of_books`.`store`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrel_of_books`.`store` ;

CREATE TABLE IF NOT EXISTS `barrel_of_books`.`store` (
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
-- Table `barrel_of_books`.`genre`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrel_of_books`.`genre` ;

CREATE TABLE IF NOT EXISTS `barrel_of_books`.`genre` (
  `genre_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(200) NULL,
  PRIMARY KEY (`genre_id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `barrel_of_books`.`transaction`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrel_of_books`.`transaction` ;

CREATE TABLE IF NOT EXISTS `barrel_of_books`.`transaction` (
  `transaction_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(45) NOT NULL,
  `date` DATETIME NOT NULL,
  `total` DECIMAL(6,2) NOT NULL,
  `employee_discount` TINYINT NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`transaction_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `barrel_of_books`.`cart_item`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrel_of_books`.`cart_item` ;

CREATE TABLE IF NOT EXISTS `barrel_of_books`.`cart_item` (
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
    REFERENCES `barrel_of_books`.`book` (`book_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_cart_item_transactionId`
    FOREIGN KEY (`transaction_id`)
    REFERENCES `barrel_of_books`.`transaction` (`transaction_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `barrel_of_books`.`genre_book`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrel_of_books`.`genre_book` ;

CREATE TABLE IF NOT EXISTS `barrel_of_books`.`genre_book` (
  `genre_id` INT NOT NULL,
  `book_id` INT NOT NULL,
  PRIMARY KEY (`genre_id`, `book_id`),
  INDEX `fk_genre_book_bookId_idx` (`book_id` ASC) VISIBLE,
  CONSTRAINT `fk_genre_book_genreId`
    FOREIGN KEY (`genre_id`)
    REFERENCES `barrel_of_books`.`genre` (`genre_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_genre_book_bookId`
    FOREIGN KEY (`book_id`)
    REFERENCES `barrel_of_books`.`book` (`book_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `barrel_of_books`.`store_book`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `barrel_of_books`.`store_book` ;

CREATE TABLE IF NOT EXISTS `barrel_of_books`.`store_book` (
  `store_id` INT NOT NULL,
  `book_id` INT NOT NULL,
  `quantity` INT NOT NULL,
  PRIMARY KEY (`store_id`, `book_id`),
  INDEX `fk_store_book_bookId_idx` (`book_id` ASC) VISIBLE,
  CONSTRAINT `fk_store_book_storeId`
    FOREIGN KEY (`store_id`)
    REFERENCES `barrel_of_books`.`store` (`store_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_store_book_bookId`
    FOREIGN KEY (`book_id`)
    REFERENCES `barrel_of_books`.`book` (`book_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- data
delimiter //
create procedure set_basic_data()
begin

delete from genre_book;
delete from genre;
alter table genre auto_increment = 1;
delete from store_book;
delete from store;
alter table store auto_increment = 1;
delete from book;
alter table book auto_increment = 1;
delete from cart_item;
alter table cart_item auto_increment = 1;
delete from transaction;
alter table transaction auto_increment = 1;

insert into genre(genre_id, name, description) values
(1, 'Fantasy', 'Fantasy description'),
(2, 'Adventure', 'Adventure description'),
(3, 'Romance', 'Romance description'),
(4, 'Horror', 'Horror description');

insert into book(book_id, title, description, price, author, quantity, publisher, language, pages, age_range, dimensions, isbn13) values
  (1, 'Harry Potter and the Sorcerer\'s Stone', 'Harry Potter spent ten long years living with Mr. and Mrs. Dursley, an aunt and uncle whose 
  outrageous favoritism of their perfectly awful son Dudley leads to some of the most inspired dark comedy since Charlie and the Chocolate 
  Factory. But fortunately for Harry, he\'s about to be granted a scholarship to a unique boarding school called THE HOGWORTS SCHOOL OF 
  WITCHCRAFT AND WIZARDRY, where he will become a school hero at the game of Quidditch (a kind of aerial soccer played high above the ground 
  on broomsticks), he will make some wonderful friends, and, unfortunately, a few terrible enemies. For although he seems to be getting your 
  run-of-the-mill boarding school experience (well, ok, even that\'s pretty darn out of the ordinary), Harry Potter has a destiny that he was 
  born to fulfill. A destiny that others would kill to keep him from.', 13.49, 'J.K. Rowling', 115, '‎Scholastic Press (October 1, 1998)',
  'English', 309, '9 - 12 years', '6.1 x 1.3 x 9.1 inches', '‎978-0590353403'),
  (2, 'Harry Potter and the Chamber of Secrets', 'The Dursleys were so mean that hideous that summer that all Harry Potter wanted was to get back to the Hogwarts School for Witchcraft
  and Wizardry. But just as he\'s packing his bags, Harry receives a warning from a strange, impish creature named Dobby who says that if 
  Harry Potter returns to Hogwarts, disaster will strike. And strike it does. For in Harry\'s second year at Hogwarts, fresh torments and horrors arise, including an outrageously stuck-up new professor,
 Gilderoy Lockheart, a spirit named Moaning Myrtle who haunts the girls\' bathroom, and the unwanted attentions of Ron Weasley\'s younger 
 sister, Ginny. But each of these seem minor annoyances when the real trouble begins, and someone--or something--starts turning Hogwarts students to stone. 
Could it be Draco Malfoy, a more poisonous rival than ever? Could it possibly be Hagrid, whose mysterious past is finally told? Or could it 
be the one everyone at Hogwarts most suspects...Harry Potter himself?', 11.94, 'J.K. Rowling', 125, 'Scholastic (July 1, 1999)',
  'English', 352, '9 - 12 years', '6.3 x 1.4 x 9.1 inches', '‎978-0439064866'),
  (3, 'Harry Potter and the Prisoner of Azkaban', 'For twelve long years, the dread fortress of Azkaban held an infamous prisoner named 
  Sirius Black. Convicted of killing thirteen people with a single curse, he was said to be the heir apparent to the Dark Lord, Voldemort.
  Now he has escaped, leaving only two clues as to where he might be headed: Harry Potter\'s defeat of You-Know-Who was Black\'s downfall 
  as well. And the Azkaban guards heard Black muttering in his sleep, "He\'s at Hogwarts . . . he\'s at Hogwarts."Harry Potter isn\'t safe,
  not even within the walls of his magical school, surrounded by his friends. Because on top of it all, there may well be a traitor in their
  midst.', 13.97, 'J.K. Rowling', 93, '‎‎Scholastic; 1st edition (October 1, 1999)',
  'English', 435, '9 - 12 years', '6.38 x 1.41 x 9.24 inches', '‎978-0439136358'),
  (4, 'Gone Girl', 'On a warm summer morning in North Carthage, Missouri, it is Nick and Amy Dunne’s fifth wedding anniversary. Presents are
  being wrapped and reservations are being made when Nick’s clever and beautiful wife disappears. Husband-of-the-Year Nick isn’t doing himself
  any favors with cringe-worthy daydreams about the slope and shape of his wife’s head, but passages from Amy\'s diary reveal the alpha-girl
  perfectionist could have put anyone dangerously on edge. Under mounting pressure from the police and the media—as well as Amy’s fiercely
  doting parents—the town golden boy parades an endless series of lies, deceits, and inappropriate behavior. Nick is oddly evasive, and he’s
  definitely bitter—but is he really a killer?', 23.80, 'Gillian Flynn', 73, 'Crown Publishing Group',
  'English', 432, null, '5.20 x 1.10 x 7.90 inches', '978-0307588371'),
  (5, 'Madhouse at the End of the Earth: The Belgica\'s Journey into the Dark Antarctic Night', 'In Madhouse at the End of the Earth, Julian Sancton
  unfolds an epic story of adventure and horror for the ages. As the Belgica’s men teetered on the brink, de Gerlache relied increasingly on
  two young officers whose friendship had blossomed in captivity: the expedition’s lone American, Dr. Frederick Cook—half genius, half con
  man—whose later infamy would overshadow his brilliance on the Belgica; and the ship’s first mate, soon-to-be legendary Roald Amundsen, even
  in his youth the storybook picture of a sailor. Together, they would plan a last-ditch, nearly certain-to-fail escape from the ice—one that
  would either etch their names in history or doom them to a terrible fate at the ocean’s bottom.', 15.76, 'Julian Sancton', 103, 'Crown Publishing Group',
  'English', 368, null, '6.3 x 1.2 x 9.5 inches', '978-1984824332'),
  (6, 'Life of Pi', 'Pi Patel is an unusual boy. The son of a zookeeper, he has an encyclopedic knowledge of animal behavior, a fervent love
  of stories, and practices not only his native Hinduism, but also Christianity and Islam. When Pi is sixteen, his family emigrates from India
  to North America aboard a Japanese cargo ship, along with their zoo animals bound for new homes. 
  The ship sinks. Pi finds himself alone in a lifeboat, his only companions a hyena, an orangutan, a wounded zebra, and Richard Parker, a
  450-pound Bengal tiger. Soon the tiger has dispatched all but Pi, whose fear, knowledge, and cunning allow him to coexist with Richard
  Parker for 227 days lost at sea. When they finally reach the coast of Mexico, Richard Parker flees to the jungle, never to be seen again.
  The Japanese authorities who interrogate Pi refuse to believe his story and press him to tell them "the truth." After hours of coercion,
  Pi tells a second story, a story much less fantastical, much more conventional-but is it more true? Life of Pi is at once a realistic,
  rousing adventure and a meta-tale of survival that explores the redemptive power of storytelling and the transformative nature of fiction.
  It\'s a story, as one character puts it, to make you believe in God.', 21.83, 'Yann Martel', 123, 'Mariner Books; First edition (June 4, 2002)',
  'English', 336, '14 years and up', '9.3 x 6.38 x 1.1', '978-1984824332')
  ;
  
  
  
insert into genre_book(genre_id, book_id) values
  (1,1),
  (1,3),
  (2,2);
  
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
  (1,1,'2020-09-09', 9.42, true, 'ORDERED'),
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

SET SQL_SAFE_UPDATES = 0;
call set_basic_data();
SET SQL_SAFE_UPDATES = 1;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
