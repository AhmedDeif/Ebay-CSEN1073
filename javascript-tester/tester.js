
// run using node.js pick one of the below libraries
//var request = require('request');
var unirest = require('unirest');
//var http    =  require('http');

function handleError( jqXHR, textStatus, errorThrown ){
    console.log("Connection With Server Failed", textStatus + ' ' +  errorThrown);
}

function stripNewLineCharacter( strText ){
    strText = strText.replace(/(?:\r\n|\r|\n)/g, '<br />');
    return strText;
}


function sendRequest_vista( strJSON, callbackfunc ){

    request.cookie("");
    request.post('http://localhost:3000/',
                { form: { SrvReq: strJSON } },
                function(err,httpResponse,body){callbackfunc(err,httpResponse,body)}
    );
}

function sendRequest_win8( strJSON, callbackfunc ){

    unirest.post('http://localhost:3000/')
    .header('Accept', 'application/json')
    .send({ "SrvReq": strJSON })
    .end(function (response) {
        console.log(response.body);
    });
}

function sendRequest( strJSON, callbackfunc ){
    console.log( "trying to send " + strJSON );
    sendRequest_win8(strJSON, callbackfunc);
}

function deleteItemCategory(itemID, categoryID){
    var gsRequest           =   new Object( );
    gsRequest.action        =   "deleteItemCategory";
    var gsRequestData       =   new Object( );
    gsRequestData.itemID    =   itemID;
    gsRequestData.categoryID=   categoryID;
    gsRequest.data          =   gsRequestData;
    var strJSON = JSON.stringify(gsRequest);
    console.log(strJSON);
    // sendRequest( strJSON, attemptLoginResponse );
}

function createCart(userID){
    var gsRequest           =   new Object( );
    gsRequest.action        =   "createCart";
    var gsRequestData       =   new Object( );
    gsRequestData.userID    =   userID;
    gsRequest.data          =   gsRequestData;
    var strJSON = JSON.stringify(gsRequest);
    console.log(strJSON)  ;
    // sendRequest( strJSON, attemptLoginResponse );
}

function deleteCart(ID){
    var gsRequest           =   new Object( );
    gsRequest.action        =   "deleteCart";
    var gsRequestData       =   new Object( );
    gsRequestData.ID        =   ID;
    gsRequest.data          =   gsRequestData;
    var strJSON = JSON.stringify(gsRequest);
    console.log(strJSON)  ;
}

function findCart(ID){
    var gsRequest           =   new Object( );
    gsRequest.action        =   "findCart";
    var gsRequestData       =   new Object( );
    gsRequestData.ID        =   ID;
    gsRequest.data          =   gsRequestData;
    var strJSON = JSON.stringify(gsRequest);
    console.log(strJSON);
    // sendRequest( strJSON, attemptLoginResponse );
}

function addItemToCart(cartID, itemID, quantity){
    var gsRequest           =   new Object( );
    gsRequest.action        =   "addItemToCart";
    var gsRequestData       =   new Object( );
    gsRequestData.cartID    =   cartID;
    gsRequestData.itemID    =   itemID;
    gsRequestData.quantity  =   quantity;
    gsRequest.data          =   gsRequestData;
    var strJSON = JSON.stringify(gsRequest);
    console.log(strJSON);
    // sendRequest( strJSON, attemptLoginResponse );
}

function updateItemInCart(cartID, itemID, quantity){
    var gsRequest           =   new Object( );
    gsRequest.action        =   "updateItemInCart";
    var gsRequestData       =   new Object( );
    gsRequestData.cartID    =   cartID;
    gsRequestData.itemID    =   itemID;
    gsRequestData.quantity  =   quantity;
    gsRequest.data          =   gsRequestData;
    var strJSON = JSON.stringify(gsRequest);
    console.log(strJSON);
    // sendRequest( strJSON, attemptLoginResponse );
}

function deleteItemInCart(cartID, itemID){
    var gsRequest           =   new Object( );
    gsRequest.action        =   "deleteItemInCart";
    var gsRequestData       =   new Object( );
    gsRequestData.cartID    =   cartID;
    gsRequestData.itemID    =   itemID;
    gsRequest.data          =   gsRequestData;
    var strJSON = JSON.stringify(gsRequest);
    console.log(strJSON);
    // sendRequest( strJSON, attemptLoginResponse );
}

function viewItemsInCart(cartID){
    var gsRequest           =   new Object( );
    gsRequest.action        =   "viewItemsInCart";
    var gsRequestData       =   new Object( );
    gsRequestData.cartID    =   cartID;
    gsRequest.data          =   gsRequestData;
    var strJSON = JSON.stringify(gsRequest);
    console.log(strJSON);
    // sendRequest( strJSON, attemptLoginResponse );
}


function addUserResponse( err,httpResponse,body ){
    console.log( body );
}

function attemptLoginResponse(){
    console.log("life is so easy you know");
}
//// Nesreen ////
// ITEM
function createItem(itemName, price, desc,categoryID, quantity, sellerID){
   var gsRequest           =   new Object( );
   gsRequest.action        =   "createItem";
   var gsRequestData       =   new Object( );
   gsRequestData.itemName     =   itemName;
   gsRequestData.price  =   price;
   gsRequestData.desc     =   desc;
   gsRequestData.categoryID     =   categoryID;
   gsRequestData.quantity  =   quantity;
   gsRequestData.sellerID  =   sellerID;
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   console.log(strJSON);
   // sendRequest( strJSON, attemptLoginResponse );
}

function editItem(itemID, itemName, price, quantity,desc, sellerID){
   var gsRequest           =   new Object( );
   gsRequest.action        =   "editItem";
   var gsRequestData       =   new Object( );
   gsRequestData.itemID    =   itemID;
   gsRequestData.itemName  =   itemName;
   gsRequestData.price     =   price;
   gsRequestData.desc      =   desc;
   gsRequestData.quantity  =   quantity;
   gsRequestData.sellerID  =   sellerID;
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
    console.log(strJSON);

   // sendRequest( strJSON, attemptLoginResponse );
}
function deleteItem(itemID){
   var gsRequest           =   new Object( );
   gsRequest.action        =   "deleteItem";
   var gsRequestData       =   new Object( );
   gsRequestData.itemID    =   itemID;
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   // sendRequest( strJSON, attemptLoginResponse );
    console.log(strJSON);

}
function findItem(itemID){
   var gsRequest           =   new Object( );
   gsRequest.action        =   "findItem";
   var gsRequestData       =   new Object( );
   gsRequestData.itemID    =   itemID;
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   console.log(strJSON);
   // sendRequest( strJSON, attemptLoginResponse );
}
function viewItem(){
   var gsRequest           =   new Object( );
   gsRequest.action        =   "viewItem";
   var gsRequestData       =   new Object( );
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   console.log(strJSON);
   // sendRequest( strJSON, attemptLoginResponse );
}
// Command
function createComment(comment_text, itemID, userID){
   var gsRequest           =   new Object( );
   gsRequest.action        =   "createComment";
   var gsRequestData       =   new Object( );
   gsRequestData.comment_text  =   comment_text;
   gsRequestData.itemID     =   itemID;
   gsRequestData.userID      =   userID;
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   console.log(strJSON);
   // sendRequest( strJSON, attemptLoginResponse );
}
function editComment(comment_text, itemID, userID,commentID){
   var gsRequest           =   new Object( );
   gsRequest.action        =   "editComment";
   var gsRequestData       =   new Object( );
   gsRequestData.comment_text  =   comment_text;
   gsRequestData.itemID     =   itemID;
   gsRequestData.userID      =   userID;
   gsRequestData.commentID = commentID
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   console.log(strJSON);
   // sendRequest( strJSON, attemptLoginResponse );
}
function viewComment(){
   var gsRequest           =   new Object( );
   gsRequest.action        =   "viewComment";
   var gsRequestData       =   new Object( );
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   console.log(strJSON);
   // sendRequest( strJSON, attemptLoginResponse );
}
function findComment(commentID){
   var gsRequest           =   new Object( );
   gsRequest.action        =   "findComment";
   var gsRequestData       =   new Object( );
   gsRequestData.commentID = commentID;
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   console.log(strJSON);
   // sendRequest( strJSON, attemptLoginResponse );
}
//Rating
function createUserRating(rating, itemID, userID){
   var gsRequest           =   new Object( );
   gsRequest.action        =   "createUserRating";
   var gsRequestData       =   new Object( );
   gsRequestData.rating  =   rating;
   gsRequestData.itemID     =   itemID;
   gsRequestData.userID      =   userID;
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   sendRequest( strJSON, attemptLoginResponse );
}
function editUserRating(rating, itemID, userID){
   var gsRequest           =   new Object( );
   gsRequest.action        =   "editUserRating";
   var gsRequestData       =   new Object( );
   gsRequestData.rating  =   rating;
   gsRequestData.itemID     =   itemID;
   gsRequestData.userID      =   userID;
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   console.log(strJSON);
   // sendRequest( strJSON, attemptLoginResponse );
}
function deleteUserRating(itemID, userID){
   var gsRequest           =   new Object( );
   gsRequest.action        =   "deleteUserRating";
   var gsRequestData       =   new Object( );
   gsRequestData.itemID     =   itemID;
   gsRequestData.userID      =   userID;
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   sendRequest( strJSON, attemptLoginResponse );
}
function viewItemUserRating(itemID){
    var gsRequest           =   new Object( );
   gsRequest.action        =   "viewItemUserRating";
   var gsRequestData       =   new Object( );
   gsRequestData.itemID     =   itemID;
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   console.log(strJSON);
   // sendRequest( strJSON, attemptLoginResponse );
}
function findItemRating(itemID,userID){
    var gsRequest           =   new Object( );
   gsRequest.action        =   "findItemRating";
   var gsRequestData       =   new Object( );
   gsRequestData.itemID     =   itemID;
   gsRequestData.userID     =   userID;
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   console.log(strJSON);
   // sendRequest( strJSON, attemptLoginResponse );
}
function calculateRating(itemID){
    var gsRequest           =   new Object( );
   gsRequest.action        =   "calculateRating";
   var gsRequestData       =   new Object( );
   gsRequestData.itemID     =   itemID;
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   console.log(strJSON) ;
   // sendRequest( strJSON, attemptLoginResponse );
}
// end nesreen's func//

//----------Rana's part ----------
function createCategory(categoryName){
  var gsRequest           =   new Object( );
  gsRequest.action        =   "createCategory";
  var gsRequestData       =   new Object( );
  gsRequestData.categoryName    =   categoryName;
  gsRequest.data          =   gsRequestData;
  var strJSON = JSON.stringify(gsRequest);
  console.log(strJSON)
  // sendRequest( strJSON, attemptLoginResponse );
}

function editCategory(id, categoryName){

 var gsRequest           =   new Object( );
 gsRequest.action        =   "editCategory";
 var gsRequestData       =   new Object( );
 gsRequestData.id    =   id;
 gsRequestData.categoryName    =   categoryName;
 gsRequest.data          =   gsRequestData;
 var strJSON = JSON.stringify(gsRequest);
 console.log(strJSON);
 // sendRequest( strJSON, attemptLoginResponse );
}


function createItemCategory(itemID, categoryID){

 var gsRequest           =   new Object( );
 gsRequest.action        =   "createItemCategory";
 var gsRequestData       =   new Object( );
 gsRequestData.itemid    =   itemID;
 gsRequestData.categoryid    =   categoryID;
 gsRequest.data          =   gsRequestData;
 var strJSON = JSON.stringify(gsRequest);
 console.log(strJSON);
 // sendRequest( strJSON, attemptLoginResponse );
}

function deleteCategory(id){

 var gsRequest           =   new Object( );
 gsRequest.action        =   "deleteCategory";
 var gsRequestData       =   new Object( );
 gsRequestData.id    =   id;
 gsRequest.data          =   gsRequestData;
 var strJSON = JSON.stringify(gsRequest);
 console.log(strJSON);
 // sendRequest( strJSON, attemptLoginResponse );
}

function findCategory(categoryID){

 var gsRequest           =   new Object( );
 gsRequest.action        =   "findCategory";
 var gsRequestData       =   new Object( );
 gsRequestData.id    =   categoryID;
 gsRequest.data          =   gsRequestData;
 var strJSON = JSON.stringify(gsRequest);
 console.log(strJSON);
 // sendRequest( strJSON, attemptLoginResponse );
}

function findItemCategory(itemID, categoryID){

 var gsRequest           =   new Object( );
 gsRequest.action        =   "findItemCategory";
 var gsRequestData       =   new Object( );
 gsRequestData.itemid    =   itemID;
 gsRequestData.categoryid    =   categoryID;
 gsRequest.data          =   gsRequestData;
 var strJSON = JSON.stringify(gsRequest);
 console.log(strJSON);
 // sendRequest( strJSON, attemptLoginResponse );
}

function viewCategory(){

 var gsRequest           =   new Object( );
 gsRequest.action        =   "viewCategory";
 var gsRequestData       =   new Object( );
 gsRequest.data          =   gsRequestData;
 var strJSON = JSON.stringify(gsRequest);
 console.log(strJSON);

 // sendRequest( strJSON, attemptLoginResponse );
}

function search(search_txt){
  var gsRequest           =   new Object( );
  gsRequest.action        =   "search";
  var gsRequestData       =   new Object( );
  gsRequestData.search_txt    =   search_txt;
  gsRequest.data          =   gsRequestData;
  var strJSON = JSON.stringify(gsRequest);
  // sendRequest( strJSON, attemptLoginResponse );
  console.log(strJSON);
}

function createUser(firstName, lastName, email,password){
   var gsRequest           =   new Object( );
   gsRequest.action        =   "createUser";
   var gsRequestData       =   new Object( );
   gsRequestData.firstName     =   firstName;
   gsRequestData.lastName  =   lastName;
   gsRequestData.email     =   email;
   gsRequestData.password     =   password;
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   console.log(strJSON);
   // sendRequest( strJSON, attemptLoginResponse );
}

function addAddress(userID, address){
   var gsRequest           =   new Object( );
   gsRequest.action        =   "addAddressCommand";
   var gsRequestData       =   new Object( );
   gsRequestData.userid     =   userID;
   gsRequestData.address  =   address;
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   console.log(strJSON);
   // sendRequest( strJSON, attemptLoginResponse );
}
function updateAddress(userID, addressID, address) {
   var gsRequest           =   new Object( );
   gsRequest.action        =   "updateAddressCommand";
   var gsRequestData       =   new Object( );
   gsRequestData.userid     =   userID;
   gsRequestData.addressid  =   addressID;
   gsRequestData.address  =   address;

   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   console.log(strJSON);
   // sendRequest( strJSON, attemptLoginResponse );
}
function deleteAddress(userID, addressID){
   var gsRequest           =   new Object( );
   gsRequest.action        =   "deleteAddressCommand";
   var gsRequestData       =   new Object( );
   gsRequestData.userid     =   userID;
   gsRequestData.addressid  =   addressID;
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   console.log(strJSON);
   // sendRequest( strJSON, attemptLoginResponse );
}

function getAddress(userID, addressID) {
   var gsRequest           =   new Object( );
   gsRequest.action        =   "getAddressCommand";
   var gsRequestData       =   new Object( );
   gsRequestData.userid     =   userID;
   gsRequestData.addressid  =   addressID;
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   console.log(strJSON);
   // sendRequest( strJSON, attemptLoginResponse );
}

function getUser(userID) {
   var gsRequest           =   new Object( );
   gsRequest.action        =   "getUserCommand";
   var gsRequestData       =   new Object( );
   gsRequestData.userid     =   userID;
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   console.log(strJSON);
   // sendRequest( strJSON, attemptLoginResponse );
}

function updateUser(userID, firstName, lastName, email, password, gender, dob){
   var gsRequest           =   new Object( );
   gsRequest.action        =   "updateUserCommand";
   var gsRequestData       =   new Object( );
   gsRequestData.id        =   userID;
   gsRequestData.firstName        =   firstName;
   gsRequestData.lastName        =   lastName;
   gsRequestData.email        =   email;
   gsRequestData.password        =   password;
   gsRequestData.gender       =   gender;
   gsRequestData.dateOfBirth        =   dob;

   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   console.log(strJSON);
   // sendRequest( strJSON, attemptLoginResponse );
}
function deleteUser(userID){
   var gsRequest           =   new Object( );
   gsRequest.action        =   "deleteUserCommand";
   var gsRequestData       =   new Object( );
   gsRequestData.id        =   userID;
   gsRequest.data          =   gsRequestData;
   var strJSON = JSON.stringify(gsRequest);
   console.log(strJSON);
   // sendRequest( strJSON, attemptLoginResponse );
}

function addBid(user_id, item_id, bid_amount){
    var gsRequest           = new Object( );
    gsRequest.action        = "createBid";
    var gsRequestData       = new Object( );
    gsRequestData.user_id     = user_id;
    gsRequestData.item_id     = item_id;
    gsRequestData.bid_amount  = bid_amount;
    gsRequest.data          = 	gsRequestData;
    var strJSON = JSON.stringify(gsRequest);
	console.log(strJSON);

    // sendRequest( strJSON, addUserResponse );
}

function deleteAuction(pID) {
	var gsRequest           = new Object( );
    gsRequest.action        = "deleteAuction";
    var gsRequestData       = new Object( );
    gsRequestData.pID     = pID;
    gsRequest.data          = 	gsRequestData;
    var strJSON = JSON.stringify(gsRequest);
	console.log(strJSON);
}
function addUserResponse( err,httpResponse,body ){
    console.log( body );
}

function addAuction(pUID, pItemID, pStartPrice, pStartDate, pEndDate){
    var gsRequest           = new Object( );
    gsRequest.action        = "createAuction";
    var gsRequestData       = new Object( );
    gsRequestData.pUID     = pUID;
    gsRequestData.pItemID     = pItemID;
    gsRequestData.pStartPrice  = pStartPrice;
    gsRequestData.pStartDate  = pStartDate;
    gsRequestData.pEndDate  = pEndDate;

    gsRequest.data          = 	gsRequestData;
    var strJSON = JSON.stringify(gsRequest);
	console.log(strJSON);

    // sendRequest( strJSON, addUserResponse );
}

// function attemptLogin( email, password ){
// 	var gsRequest           = 	new Object( );
//     gsRequest.action        = 	"attemptLogin";
//     var gsRequestData       = 	new Object( );
//     gsRequestData.email	    = 	email;
//     gsRequestData.password	= 	password;
//     gsRequest.data          = 	gsRequestData;
//     var strJSON = JSON.stringify(gsRequest);
//     sendRequest( strJSON, attemptLoginResponse );
// }

// function attemptLoginResponse( err,httpResponse,body ){
//     console.log( body );
// }


// To start sending messages:
// addBid('1', '1', '10');
//attemptLogin("mohamed@m.com","johnpass");
// deleteAuction('1')

function editAuction(auctionID, pItemID, pStartPrice, pStartDate, pEndDate){
    var gsRequest           = new Object( );
    gsRequest.action        = "editAuction";
    var gsRequestData       = new Object( );
    gsRequestData.auctionID     = auctionID;
    gsRequestData.pItemID     = pItemID;
    gsRequestData.pStartPrice  = pStartPrice;
    gsRequestData.pStartDate  = pStartDate;
    gsRequestData.pEndDate  = pEndDate;

    gsRequest.data          = 	gsRequestData;
    var strJSON = JSON.stringify(gsRequest);
	console.log(strJSON);

    // sendRequest( strJSON, addUserResponse );
}
addAuction('2', '1', '100', '17-12-2017', '20-12-2017')
//----------Rana's part ----------

// To start sending messages:
// deleteItemCategory('3','1');
// createCart('1');
// deleteCart('16');
// findCart('3');
// addItemToCart('3','4','3');
// updateItemInCart('3','4','5');
// deleteItemInCart('3','4');
// viewItemsInCart('3');

// nesreens msgs

/////////////////////////////////////////////Item/////////////////////////////////
  // createItem('iphone 7s', '100', 'item created','1', '5', '1');
  // createItem('iphone 7s plus', '90', 'item created','1', '5', '1');
  // createItem('macbook pro', '950', 'item created','1', '5', '1');
  // createItem('ipad pro', '70', 'item created','1', '5', '1');


// editItem('24','iphone 6', '450', '1', 'item edited', '1');
// deleteItem('25');
// findItem('4');
// viewItem();
/////////////////////////////////////////////Comment/////////////////////////////////
// createComment('2nd comment because why not','4','1');
// editComment('test edit comment command', '4','1','15');
// viewComment();
// findComment('16');
/////////////////////////////////////////////Rating/////////////////////////////////
// createUserRating('3', '4' , '1');
// createUserRating('5', '4' , '3');
// createUserRating('2', '5' , '3');

// editUserRating('5','4','1');
// deleteUserRating('4','1');
// viewItemUserRating('4');
// findItemRating('4','3');
// calculateRating('4');
// end nesreens msgs
//attemptLogin("mohamed@m.com","johnpass");
// createCategory('testMongo2');
// viewItemRating('3');
// editCategory('9','Nesreen Category');
// createItemCategory('11','17');
// deleteCategory('16');
// findCategory('3');
// findItemCategory('3','3');
// viewCategory();
// search('7s')
// createUser("abdelazeem", "zeema", "nana@rana.com", "password");
// addAddress('1',"home sweet home <3 ");
// deleteAddress('1','1');
// deleteUser('4');
// getAddress('2','1');
// updateAddress('2','1',"IBIZAAAA")
// updateUser('3',"Rana","El Bendary", "rana@gmail.com", "topsecret", "female", "");
// getUser('77');
