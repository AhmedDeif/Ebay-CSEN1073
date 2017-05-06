---------------------------CREATE AUCTION--------------------------------------------------
DECLARE
    lTimeCurrent TIMESTAMP;
    lUID        INTEGER;
    lItemID       INTEGER;
    aID INTEGER;
BEGIN
    SELECT into lUID id FROM Users WHERE id = pUID;




    SELECT into lItemID id FROM Item WHERE id = pItemID AND users_id = lUID;



    INSERT into Auction(
        item_id, start_date, end_date, min_price)
            values(
                pItemID,
                pStartDate,
                pEndDate,
                pStartPrice
                );
    SELECT into aID id FROM Auction WHERE item_id = pItemID;


    return aID;
END;

-----------------------------------CREATE BID---------------------------------------------

declare
  ltimeCurrent    TIMESTAMP;

  auctionID INT;
   minAmount DECIMAL;
begin
  select into auctionID id from Auction where item_id = itemID;
  select into ltimeCurrent 'now';
  insert into Bid(amount, users_id, auction_id, created_at)
              values(bid_amount, userID, auctionID, ltimeCurrent);
  return 1;
end;

-------------------------------UPDATE AUCTION-------------------------------------------------

DECLARE
    lTimeCurrent TIMESTAMP;
    lUID        INTEGER;
    lItemID       INTEGER;
    lStartDate DATE;
BEGIN

    UPDATE Auction
        set item_id = pItemID,
        start_date = pStartDate,
        end_date = pEndDate,
        min_price = pStartPrice
        where id = auctionID;

    return 200;
END;

-------------------------------GET AUCTIONS FOR USER-------------------------------------------
DECLARE
    auctions refcursor;
BEGIN

    OPEN auctions FOR  SELECT * FROM Auction INNER JOIN Item ON (Item.users_id = pUID);
        RETURN auctions;
END;
-------------------------------- DELETE AUCTION -------------------------------------------
DECLARE
    lID INTEGER;
BEGIN
    SELECT into lID id FROM Auction WHERE id = pID;

    -- Auction does not exist
    if(lID is null) then
        return -401;
    end if;

    -- Delete the Auction
    DELETE FROM Auction WHERE id = pID;

    return 200;
END;

------------------------------RETRIEVE WINNING BID--------------------------------------------------------

declare
  auctionID INT;
    maxAmount DECIMAL;
begin
  select into auctionID id from Auction where item_id = itemID;

    if(auctionID is null) then
    return -404;
    end if;

    select into maxAmount amount from Bid where auction_id = auctionID AND max(amount);

    if(bidID is null) then
    return -404;
  end if;

    return maxAmount;
end;
-----------------------------RETIEVE MY CURRENT BIDS---------------------------------------------------


declare
    lTimeCurrent TIMESTAMP;
begin
    SELECT into lTimeCurrent  'now';
    RETURN QUERY
    SELECT * FROM Bid,Auction,Item
    WHERE Auction.end_date < lTimeCurrent AND Bid.user = userID;


end;
--------------------------VIEW MY PURCHASED ITEMS--------------------------------------------------

declare
    bidID INT;
    items integer[];
begin
    select into bidID bid_id from Transaction where user_id = userID;
    if(bidID is null) then
    return -404;
    end if;

    select into items item_id from Bid where id = bid_id;

    return items;
end;
---------------------------VIEW WINNING BID-----------------------------------------------


declare
begin
	RETURN QUERY 
    SELECT *  FROM Bid,Auction,Item
    WHERE Bid.amount = max(amount) AND Auction.id = auctionID;

end;
