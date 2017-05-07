import { Router } from 'express'
import {
    default as Amqp
} from 'amqplib'
import uuid from 'uuid/v4'
import when from 'when';
import { StringDecoder } from 'string_decoder';
const router = new Router();


/**
 * @apiDefine master Master access only
 * You must pass `access_token` parameter or a Bearer Token authorization header
 * to access this endpoint.
 */
/**
 * @apiDefine admin Admin access only
 * You must pass `access_token` parameter or a Bearer Token authorization header
 * to access this endpoint.
 */
/**
 * @apiDefine user User access only
 * You must pass `access_token` parameter or a Bearer Token authorization header
 * to access this endpoint.
 */
/**
 * @apiDefine listParams
 * @apiParam {String} [q] Query to search.
 * @apiParam {Number{1..30}} [page=1] Page number.
 * @apiParam {Number{1..100}} [limit=30] Amount of returned items.
 * @apiParam {String[]} [sort=-createdAt] Order of returned items.
 * @apiParam {String[]} [fields] Fields to be returned.
 */

function sendRequestToQueue(action, data, requestQueue, responseQueue, callback) {
    // let connection = Amqp.connect("amqp://ebay:ebay@172.20.10.2:5672")
    let connection = Amqp.connect("amqp://ebay:ebay@127.0.0.1:5672")
    console.log({ connection });
    // let data = { "action": "createItem", "data": { "itemName": "AAAA pro", "price": "70", "desc": "item created", "categoryID": "1", "quantity": "5", "sellerID": "1" } };
    // let data = { "action": "createUser", "data": { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } };
    let dataToSend = {
        action,
        data
    }
    connection.then((conn) => {
        return conn.createChannel();
    }).then((channel) => {
            let correlationId = uuid();
            when.all([
                channel.assertQueue(requestQueue),
                channel.sendToQueue(requestQueue, new Buffer(JSON.stringify(dataToSend)), { correlationId }),
                channel.consume(responseQueue, (message) => {
                    let { content, fields, properties } = message;
                    let recievedCorrId = properties.correlationId;
                    let decoder = new StringDecoder('utf8');
                    content = decoder.write(content);
                    if (recievedCorrId === correlationId) {
                        console.log("CORRECT MESSAGE: ", { message });
                        channel.ack(message);
                        channel.cancel(message.fields.consumerTag);
                        callback(message.content);
                    } else {
                        // console.log("Discarded MESSAGE");
                        channel.nack(message);
                    }

                })
            ]);
    })
}

// CREATE
router.post("/user", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('createUser', data, 'EbayUsersRequest', 'EbayUsersResponse', function(response) {
      res.send(response);
    })
    // res.send(data);
})
router.post("/search", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    console.log("SEARCH API");
    sendRequestToQueue('search', data, 'EbaySearchRequest', 'EbaySearchResponse', function(response) {
      res.send(response);
    })
    // res.send(data);
})
router.post("/find_user", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('getUserCommand', data, 'EbayUsersRequest', 'EbayUsersResponse', function(response) {
      res.send(response);
    })
    // res.send(data);
})

router.post("/item", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('createItem', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.post("/comment", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('createComment', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.post("/user_rating", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('createUserRating', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.post("/category", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('createCategory', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.post("/item_category", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('createItemCategory', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.post("/cart", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('createCart', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.post("/cart_item", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('addItemToCart', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

// EDIT
router.put("/item/", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('editItem', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.put("/comment", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('editComment', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.put("/user_rating", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('editUserRating', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.put("/category", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('editCategory', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.put("/cart_item", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('updateItemInCart', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

// DELETE
router.delete("/item", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('deleteItem', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.delete("/user_rating", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('deleteUserRating', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.delete("/category", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('deleteCategory', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.delete("/item_category", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('deleteItemCategory', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.delete("/cart", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('deleteCart', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.delete("/cart_item", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('deleteItemInCart', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

// GET
router.post("/view_item", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('viewItem', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.post("/find_item/", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('findItem', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.get("/view_comment", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('viewComment', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.get("/view_comment", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('findComment', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.get("/view_item_rating", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('viewItemUserRating', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.get("/find_item_rating", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('findItemRating', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.get("/view_category", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('viewCategory', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.get("/find_category", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('findCategory', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.get("/item_category", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('findItemCategory', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.get("/cart", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('findCart', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

router.get("/cart_item", (req, res, next) => {
    // let data = { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } ;
    let data = req.body
    sendRequestToQueue('viewItemsInCart', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {
      res.send(response);
    })
})

export default router
