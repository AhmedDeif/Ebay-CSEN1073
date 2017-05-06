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
