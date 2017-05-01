router.get("/", (req, res, next) => {
    let data = { "action": "createUser", "data": { "firstName": "test user", "lastName": "aa", "email": "test@test.com", "password": "123" } };
    sendRequestToQueue('CreateItemCategoryCmd', data, 'EbayMerchantsRequest', 'EbayMerchantsResponse', function(response) {

    })
})


export default router;