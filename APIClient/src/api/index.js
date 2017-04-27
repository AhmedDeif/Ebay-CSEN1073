import { Router } from 'express'
import {
    default as Amqp
} from 'amqplib'
const router = new Router()
import uuid from 'uuid/v4'
import when from 'when';

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

console.log({ Amqp });
let connection = Amqp.connect("amqp://ebay:ebay@172.17.0.2:5672")

console.log({ connection });
let data = { "action": "createItem", "data": { "itemName": "AAAA pro", "price": "70", "desc": "item created", "categoryID": "1", "quantity": "5", "sellerID": "1" } };
connection.then((conn) => {
        return conn.createChannel();
    }).then((channel) => {
        let correlationId = uuid();
        return when.all([
            channel.assertQueue('EbayMerchantsRequest'),
            // ch.assertExchange('bar'),
            // ch.bindQueue('foo', 'bar', 'baz'),
            channel.sendToQueue('EbayMerchantsRequest', new Buffer(JSON.stringify(data)), { correlationId }),
            channel.consume('EbayMerchantsResponse', function({ content, fields, properties }) {
                let recievedCorrId = properties.correlationId;
                console.log("RECIVED MESSAGE: ", {content, fields, properties});
                if (recievedCorrId === correlationId) {
                    console.log("CORRECT MESSAGE: ", {content});

                    channel.cancel(fields.consumerTag);
                }

            })
        ]);
    })
    // connection.on('error', (error) => {
    //   console.log("CANNOT CONNECT TO MQ ", error );
    // })

// connection.then( (connect) => {
//   console.log("MQ CONNECTION READY");
//
//   connection.exchange('EbayMerchantsExchange', (merchantsExchange) => {
//     connection.queue('EbayMerchantsRequest', (reqQueue) => {
//       reqQueue.bind('EbayMerchantsExchange');
//       connection.queue('EbayMerchantsResponse', (resQueue) => {
//         resQueue.bind('EbayMerchantsExchange');
//
//         data = {"action":"createItem","data":{"itemName":"AAAA pro","price":"70","desc":"item created","categoryID":"1","quantity":"5","sellerID":"1"}};
//         merchantsExchange.publish('EbayMerchantsRequest', data, (delivered) => {
//           console.log("MESSAGE delivered? ", delivered);
//
//         });
//
//         resQueue.subscribe(function (message, headers, deliveryInfo, messageObject) {
//           console.log('Got a message with routing key ' + deliveryInfo.routingKey);
//           res.send('OK');
//         });
//       })
//     })
//   })
// })
//

router.get("/", (req, res, next) => {

})
export default router
