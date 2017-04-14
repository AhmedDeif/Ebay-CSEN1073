CREATE TABLE IF NOT EXISTS Users (
	ID                     									INT NOT NULL DEFAULT NEXTVAL('seq_Users_ID'),
	firstName                  								VARCHAR(100) NOT NULL,
	lastName                  								VARCHAR(100) NOT NULL,
	email                  									VARCHAR(100) NOT NULL,
	usr_password                  							VARCHAR(100) NOT NULL,
	gender                  								VARCHAR(100),
	dateOfBirth                  							DATE,
	creationTime    		    							TIMESTAMP NOT NULL,
    CONSTRAINT const_pk_Users_ID                			PRIMARY KEY (ID)
);

--
CREATE SEQUENCE seq_Items_ID  MINVALUE 2 START WITH 2;
CREATE TABLE IF NOT EXISTS Items (
	ID                     									INT NOT NULL DEFAULT NEXTVAL('seq_Items_ID'),
	itemName                  								VARCHAR(100) NOT NULL,
	price               									DOUBLE(10,2)  NOT NULL,
	description       		    							VARCHAR(500),
	sellerID												INT NOT NULL,
	quantity												INT NOT NULL,
	categoryId												INT,
	creationTime    		    							TIMESTAMP NOT NULL,
    CONSTRAINT const_pk_Items_ID                			PRIMARY KEY (ID),
	CONSTRAINT const_fk_item_sellerID 						FOREIGN KEY(sellerID) REFERENCES Users(ID) ON DELETE CASCADE,
	CONSTRAINT chk_Price CHECK (price > 0)

);


CREATE SEQUENCE seq_Category_ID  MINVALUE 1 START WITH 1;
CREATE TABLE IF NOT EXISTS Categories (
	ID                     									INT NOT NULL DEFAULT NEXTVAL('seq_Category_ID'),
	categoryName                							VARCHAR(100) NOT NULL,
    creationTime    		   		 						TIMESTAMP NOT NULL,
    CONSTRAINT const_pk_category_ID                			PRIMARY KEY (ID)
);


-- CREATE TABLE IF NOT EXISTS Ratings (
-- 	itemID													INT NOT NULL,
-- 	oneStar                									INT DEFAULT 0,
-- 	twoStar              							  		INT DEFAULT 0,
-- 	threeStar                								INT DEFAULT 0,
-- 	fourStar                								INT DEFAULT 0,
-- 	fiveStar                								INT DEFAULT 0,
-- 	creationTime    		    							TIMESTAMP NOT NULL,
-- 	CONSTRAINT const_pk_rating_itemID                		PRIMARY KEY (itemID),
-- 	CONSTRAINT const_fk_rating_itemID  						FOREIGN KEY(itemID) REFERENCES Items(ID)
-- );


CREATE TABLE IF NOT EXISTS Carts (
	ID                     									INT NOT NULL DEFAULT NEXTVAL('seq_Category_ID'),
	userID													INT NOT NULL,
	creationTime    		    							TIMESTAMP NOT NULL,
	CONSTRAINT const_pk_rating_ID                			PRIMARY KEY (ID),
	CONSTRAINT const_fk_cart_userID  						FOREIGN KEY(userID) REFERENCES Users(ID) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Comments (
	ID                     									INT NOT NULL DEFAULT NEXTVAL('seq_Category_ID'),
	comment_text											VARCHAR(500),
	userID													INT NOT NULL,
	itemID													INT NOT NULL,
	creationTime    		    							TIMESTAMP NOT NULL,
	CONSTRAINT const_pk_comments_rating_ID                			PRIMARY KEY (ID),
	CONSTRAINT const_fk_comment_userID  					FOREIGN KEY(userID) REFERENCES Users(ID) ON DELETE CASCADE,
	CONSTRAINT const_fk_comment_itemID  					FOREIGN KEY(itemID) REFERENCES Items(ID) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS Cart_Item (
	cartID                     								INT NOT NULL ,
	itemID													INT NOT NULL,
	quantity												INT NOT NULL,
	creationTime    		    							TIMESTAMP NOT NULL,
	CONSTRAINT const_pk_cart_item_ID                		PRIMARY KEY (cartID, itemID),
	CONSTRAINT const_fk_cart_item_itemID  					FOREIGN KEY(itemID) REFERENCES Items(ID) ON DELETE CASCADE,
	CONSTRAINT const_fk_cart_item_cartID  					FOREIGN KEY(cartID) REFERENCES Carts(ID) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS User_Rating (
	userID                     								INT NOT NULL ,
	rating													INT NOT NULL,
	itemID													INT NOT NULL,
	creationTime    		    							TIMESTAMP NOT NULL,
	CONSTRAINT const_pk_user_rating_ID                		PRIMARY KEY (userID, itemID),
	CONSTRAINT const_fk_user_rating_userID  				FOREIGN KEY(userID) REFERENCES Users(ID) ON DELETE CASCADE,
	CONSTRAINT const_fk_user_rating_itemID  				FOREIGN KEY(itemID) REFERENCES Items(ID) ON DELETE CASCADE,
	CONSTRAINT chk_rating CHECK (rating > 0 and rating <6)


);


CREATE TABLE IF NOT EXISTS Item_Category (
	itemID                     								INT NOT NULL ,
	categoryID												INT NOT NULL,
	creationTime    		    							TIMESTAMP NOT NULL,
	CONSTRAINT const_pk_item_category_ID        			PRIMARY KEY (itemID, categoryID),
	CONSTRAINT const_fk_item_category_itemID				FOREIGN KEY(itemID) REFERENCES Items(ID) ON DELETE CASCADE,
	CONSTRAINT const_fk_item_category_categoryID  			FOREIGN KEY(categoryID) REFERENCES Categories(ID) ON DELETE CASCADE
);
