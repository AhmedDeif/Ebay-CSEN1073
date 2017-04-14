-------------------------------------------------------------------------------------------------------------------------------------------------
-- ITEM FUNCTIONS
create or replace function createItem(
	itemName 		   VARCHAR(100),
	price	           decimal(10,2),
    description	       VARCHAR(500),
    categoryId         INT,
    quantity	       INT,
    sellerID           INT
    )
returns INT as'

declare
    ltimeCurrent    TIMESTAMP;
    lID				INT;

begin
    select into lID  nextval(''seq_Items_ID'');
    select into ltimeCurrent ''now'';

	insert  into Items
                (ID,itemName,price,description,quantity,sellerID,creationTime)
            values(
                lID,
                itemName,
                price,
                description,
                quantity,
                sellerID,
                ltimeCurrent
            ); 

    insert  into Item_Category
                (itemID,categoryID,creationTime)
            values(lID,categoryId,ltimeCurrent);    
	Return lID;


end;
' language 'plpgsql';

create or replace function editItem(
    _itemID             INT,
    _itemName          VARCHAR(100),
    _price             decimal(10,2),
    _description           VARCHAR(500),
    _quantity          INT,
    _sellerID           INT
    )
returns INT as'

begin
    UPDATE Items
    SET "itemname"=_itemName,
        "price" = _price,
        "description" = _description,
        "quantity" = _quantity
    WHERE "id" = _itemID;
return _itemID;
end;
' language 'plpgsql';


create or replace function findItem(
    _itemID             INT
    )
returns refcursor as'
DECLARE
      items refcursor; 
BEGIN
    OPEN items FOR  SELECT * FROM Items where id = _itemID;
        RETURN items;

END;
' language 'plpgsql';



create or replace function viewItem()
returns refcursor as'
DECLARE
      items refcursor; 
BEGIN
    OPEN items FOR  SELECT * FROM Items;
        RETURN items;

END;
' language 'plpgsql';

create or replace function deleteItem(
    itemID             INT
    )
returns INT as'
begin
	DELETE FROM Items WHERE ID = itemID;
return itemID;
end;
' language 'plpgsql';

-------------------------------------------------------------------------------------------------------------------------------------------------
-- RATING FUNCTIONS

create or replace function createUserRating(
    userID	       INT,
    itemID         INT,
	rating	       INT
    )
returns INT as'
declare
    ltimeCurrent    TIMESTAMP;
begin
    select into ltimeCurrent ''now'';
	insert  into User_Rating
                (userID,itemID,rating,creationTime)
            values(
                userID,
                itemID,
                rating,
                ltimeCurrent
            );

return rating;
end;

' language 'plpgsql';

create or replace function editUserRating(
    _userID	       INT,
    _itemID         INT,
	_rating			INT
    )
returns INT as'
begin
    UPDATE User_Rating
    SET "userid"=_userID,
        "itemid" = _itemID,
        "rating" = _rating
    WHERE userid = _userID and itemid = _itemID;
return _rating;
end;
' language 'plpgsql';

create or replace function findItemUserRating(
    _itemID         INT,
	_userID			INT
    )
returns refcursor as'
DECLARE
      itemsRating refcursor; 
BEGIN
    OPEN itemsRating FOR  SELECT * FROM User_Rating where itemid = _itemID and userid = _userID;
        RETURN itemsRating;

END;
' language 'plpgsql';




create or replace function deleteUserRating(
    _itemID             INT,
	_userID				INT
    )
returns INT as'
begin
	DELETE FROM User_Rating WHERE userid = _userID and itemid = _itemID;
return _itemID;
end;
' language 'plpgsql';

create or replace function viewItemUserRating(
    _itemID         INT
    )
returns refcursor as'
DECLARE
      itemsRatings refcursor; 
BEGIN
    OPEN itemsRatings FOR  SELECT * FROM User_Rating where itemid = _itemID;
        RETURN itemsRatings;

END;
' language 'plpgsql';


create or replace function calculateRating(
    _itemID     INT 
)
returns int as '
DECLARE 
    avg_rating int;
BEGIN
    SELECT AVG(rating) into avg_rating  
    FROM User_Rating
    where itemid = _itemID;
    return avg_rating;


END;

'language 'plpgsql';


-------------------------------------------------------------------------------------------------------------------------------------------------
-- COMMENTS FUNCTIONS
create or replace function createComment(
	comment_text 		   VARCHAR(500),
	itemID 						INT,
	userID 						INT
)
returns INT as'

declare
    ltimeCurrent    TIMESTAMP;
    lID				INT;

begin
    select into lID  nextval(''seq_Items_ID'');
    select into ltimeCurrent ''now'';

	insert  into Comments
                (ID,comment_text,userID,itemID,creationTime)
            values(
                lID,
                comment_text,
                userID,
                itemID,
                ltimeCurrent
            );
return lID;
end;
' language 'plpgsql';

create or replace function editComment(
	_comment_text 		   VARCHAR(500),
	_itemID 						INT,
	_userID 						INT,
	_commentID 				INT
    )
returns INT as'

begin
    UPDATE Comments
    SET "comment_text"=_comment_text,
        "itemid" = _itemID,
        "userid" = _userID
    WHERE "id" = _commentID;
return _commentID;
end;
' language 'plpgsql';


create or replace function findComment(
    commentID             INT
    )
returns refcursor as'
DECLARE
      comments refcursor; 
BEGIN
    OPEN comments FOR  SELECT * FROM Comments WHERE "id" = commentID;

        RETURN items;

END;
' language 'plpgsql';


create or replace function viewComment(
    )

returns refcursor as'
DECLARE
      comments refcursor; 
BEGIN
    OPEN comments FOR SELECT * FROM Comments;

        RETURN items;

END;
' language 'plpgsql';

-------------------------------------------------------------------------------------------------------------------------------------------------
-- CATEGORY FUNCTIONS


create or replace function createCategory(
    categoryName           VARCHAR(100)
    )
returns INT as'

declare
    ltimeCurrent    TIMESTAMP;
    lID             INT;

begin
    select into lID  nextval('seq_Category_ID');
    select into ltimeCurrent 'now';

    insert  into Categories
                (ID,categoryName,creationTime)
            values(
                lID,
                categoryName,
                ltimeCurrent
            );
return lID;
end;
' language 'plpgsql';

create or replace function editCategory(
	_ID        				INT,
	_categoryName 		   VARCHAR(100)
    )
returns VARCHAR as'

declare 
	_name VARCHAR;
begin
	select categoryname into _name from Categories WHERE "id" = _ID;
    UPDATE Categories
    SET "categoryname"=_categoryName
    WHERE "id" = _ID;
return _name;
end;

' language 'plpgsql';

create or replace function findCategory(
   _categoryID             INT
   )
returns refcursor as'
DECLARE
     categories refcursor;
BEGIN
   OPEN categories FOR  SELECT * FROM Categories where id = _categoryID;
       RETURN categories;

END;
' language 'plpgsql';

create or replace function findCategoryName(
   _categoryID             INT
   )
returns VARCHAR as'

Declare _name VARCHAR;
BEGIN
   SELECT categoryname into _name  FROM Categories where id = _categoryID;
    RETURN _name;

END;

' language 'plpgsql';


create or replace function viewCategory(
   )
returns refcursor as'
DECLARE
     categories refcursor;
BEGIN
   OPEN categories FOR  SELECT * FROM Categories ;
       RETURN categories;

END;
' language 'plpgsql';

create or replace function deleteCategory(
    _ID             INT
    )
returns VARCHAR as'
declare
    _name    VARCHAR;
begin
    SELECT categoryname into _name FROM Categories where id = _ID;
    DELETE from Categories
    WHERE "id" = _ID;
return _name;
end;
' language 'plpgsql';

-------------------------------------------------------------------------------------------------------------------------------------------------
-- ITEM CATEGORY FUNCTIONS

create or replace function createItemCategory(
    itemID          INT,
    categoryID         INT
    )
returns INT as'
declare
    ltimeCurrent    TIMESTAMP;

begin
    select into ltimeCurrent 'now';
	insert  into Item_Category
        (itemID,categoryID,creationTime)
            values(
                itemID,
                categoryID,
                ltimeCurrent
            );
return itemID;
end;
' language 'plpgsql';



create or replace function findItemCategory(
   _itemID             INT,
   _categoryID         INT
   )
returns refcursor as'
DECLARE
     itemcategories refcursor;
BEGIN
   OPEN itemcategories FOR  SELECT * FROM item_category where itemid = _itemID AND categoryid = _categoryID;
       RETURN itemcategories;

END;
' language 'plpgsql';

create or replace function deleteItemCategory(
    _itemID             INT,
    _categoryID        INT
    )
returns INT as'
begin
    DELETE from item_category
    WHERE "itemid" = _itemID and "categoryid"=_categoryID;
return _itemID;
end;
' language 'plpgsql';

-------------------------------------------------------------------------------------------------------------------------------------------------
-- ITEM CATEGORY FUNCTIONS

---------------------------------------------------------------------------------------------------------------------------------------
create or replace function createCart(
    userID                      INT
    )
returns INT as'
    declare
            ltimeCurrent    TIMESTAMP;
            lID             INT;

    begin
        select into ltimeCurrent ''now'';
            select into lID  nextval(''seq_Category_ID'');

        insert  into Carts
                (ID,
                    userID,
                    creationTime)

      values(lID,
             userID,
             ltimeCurrent);
   	return lID;
	end;

' language 'plpgsql';

---------------------------------------------------------------------------------------------------------------------------------------
create or replace function deleteCart(
    ID                      INT
    )
returns INT as'

    begin
       delete from Carts where Carts.ID = ID ;
   	return ID;
	end;

' language 'plpgsql';
---------------------------------------------------------------------------------------------------------------------------------------
create or replace function findCart(
    _ID                      INT
    )
returns refcursor as'
    DECLARE
        carts refcursor;
    begin
            OPEN carts FOR SELECT * FROM Carts WHERE Carts.ID = _ID;
   	return carts;
	end;

' language 'plpgsql';

---------------------------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------------------------------

---------------------------------------------------------------------------------------------------------------------------------------

create or replace function addItemToCart(
    _cartID                      INT,
    _itemID                      INT,
    _quantity                    INT
    )
returns INT as'
    declare
    ltimeCurrent    TIMESTAMP;

begin
    select into ltimeCurrent ''now'';

    insert  into cart_item
                (cartID,itemID,quantity,creationTime)
            values(
                _cartID,
                _itemID,
                _quantity,
                ltimeCurrent
            );
return _itemID;
end;

' language 'plpgsql';


create or replace function updateItemInCart(
    _cartID                      INT,
    _itemID                      INT,
    _quantity                    INT
    )
returns INT as'
    begin
        UPDATE cart_item
        SET quantity = _quantity
        WHERE cartID =_cartID AND itemID =_itemID;
    return _cartID;
    end;


' language 'plpgsql';
---------------------------------------------------------------------------------------------------------------------------------------
create or replace function deleteItemInCart(
    _cartID                      INT,
    _itemID                      INT
    )
returns INT as'
    begin
        delete from cart_item where cartID =_cartID AND itemID =_itemID;
    return _itemID;
    end;

' language 'plpgsql';

---------------------------------------------------------------------------------------------------------------------------------------
create or replace function viewItemsInCart(
    _cartID                      INT
    )
returns refcursor as'    
    DECLARE
      items refcursor; 
    begin
        OPEN items FOR SELECT * FROM cart_item
            WHERE cartID = _cartID;
    return items;
	end;
' language 'plpgsql';
