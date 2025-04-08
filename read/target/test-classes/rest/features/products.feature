Feature: Tests the Read Query Service using a REST client.
  Scenario: Tests out pagination capability
When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"
And I POST a REST request to URL "/q/products" with payload
"""
{
	"sortCriteria" :[
		{"name":"name","ascendingOrder": true}
	],
	"pageNum": 1,
	"numRowsInPage": 15
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"
And the REST response key "currentPage" is "1"
And the REST response key "maxPages" is "1"

