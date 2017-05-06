CREATE TABLE "auction" ("id" serial NOT NULL PRIMARY KEY, "min_price" numeric(10, 4) NOT NULL, "start_date" date NOT NULL, "end_date" date NOT NULL);
--
-- Create model Bid
--
CREATE TABLE "bid" ("id" serial NOT NULL PRIMARY KEY, "amount" numeric(10, 4) NOT NULL, "created_at" date NOT NULL, "auction_id" integer NOT NULL, "users_id" integer NOT NULL);
--
--
--
-- Create model Transaction
--
CREATE TABLE "transaction" ("id" serial NOT NULL PRIMARY KEY, "bid_id" integer NOT NULL);
--
-- Add field item to auction
--
ALTER TABLE "auction" ADD COLUMN "item_id" integer NOT NULL;
ALTER TABLE "auction" ALTER COLUMN "item_id" DROP DEFAULT;
ALTER TABLE "bid" ADD CONSTRAINT "bid_auction_id_72a4abe6_fk_auction_id" FOREIGN KEY ("auction_id") REFERENCES "auction" ("id") DEFERRABLE INITIALLY DEFERRED;
ALTER TABLE "bid" ADD CONSTRAINT "bid_users_id_5dc4e7ab_fk_userss_id" FOREIGN KEY ("users_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX "bid_efa6e4ff" ON "bid" ("auction_id");
CREATE INDEX "bid_e8701ad4" ON "bid" ("users_id");
ALTER TABLE "item" ADD CONSTRAINT "item_users_id_8f95d1aa_fk_userss_id" FOREIGN KEY ("users_id") REFERENCES "users" ("id") DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX "item_e8701ad4" ON "item" ("users_id");
ALTER TABLE "transaction" ADD CONSTRAINT "transaction_bid_id_31b778ad_fk_bid_id" FOREIGN KEY ("bid_id") REFERENCES "bid" ("id") DEFERRABLE INITIALLY DEFERRED;
CREATE INDEX "transaction_594bb2aa" ON "transaction" ("bid_id");
CREATE INDEX "auction_82bfda79" ON "auction" ("item_id");
ALTER TABLE "auction" ADD CONSTRAINT "auction_item_id_a7f7ad52_fk_item_id" FOREIGN KEY ("item_id") REFERENCES "item" ("id") DEFERRABLE INITIALLY DEFERRED;
COMMIT;
