 
Feature: Tests the Read Query Service using a REST client. 

Scenario: Tests out pagination capability
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"
And I POST a REST request to URL "/q/reads" with payload
"""
{
	"sortCriteria" :[
		{"name":"name","ascendingOrder": true}
	],
	"pageNum": 2,
	"numRowsInPage": 15
}
"""
Then the http status code is 200
And the top level code is 200
And success is true 
And the REST response key "numRowsReturned" is "15"
And the REST response key "currentPage" is "2"
And the REST response key "maxPages" is "2"
And the REST response key "list[0].row.name" is "Narendra"
And the REST response key "list[0].row.id" is "25"
And the REST response key "list[14].row.name" is "Vikas"
And the REST response key "list[14].row.id" is "18"


Scenario: Test Likes query
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"
When I POST a REST request to URL "/q/reads" with payload
"""
{
	"filters" :{
		"name": "ja"
	}
}
"""
Then the http status code is 200
And the top level code is 200
And success is true 
And the REST response key "numRowsReturned" is "1"
And the REST response key "list[0].row.name" is "Vijay"
And the REST response key "list[0].row.id" is "29"


Scenario: Test Specific - Test Contains with an array and sort descending
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"
When I POST a REST request to URL "/q/reads" with payload
"""
{
	"filters" :{
		"branch": [ "Gurgaon", "Jaipur", "Trivandrum"]
	},
	"sortCriteria" :[
		{"name":"branch","ascendingOrder": false}
	]
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "3"
And the REST response key "list[2].row.name" is "Kamala"
And the REST response key "list[0].row.id" is "16"
And the REST response key "list[1].row.id" is "21"
And the REST response key "list[2].row.id" is "8"


Scenario: Test Specific - Test Two params
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"
When I POST a REST request to URL "/q/reads" with payload
"""
{
	"filters" :{
		"branch": [ "Bangalore"],
		"name": "ka"
	},
	"sortCriteria" :[
		{"name":"name","ascendingOrder": true}
	]
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"
And the REST response key "list[0].row.id" is "5"
And the REST response key "list[1].row.id" is "18"
And the REST response key "list[0].row.name" is "Akash"
And the REST response key "list[1].row.name" is "Vikas"


Scenario: Test Specific - Test Two params but with one of them just as a string
  When I construct a REST request with authorization header in realm "tenant0" for user "t0-premium" and password "t0-premium"
  And I construct a REST request with header "x-chenile-tenant-id" and value "tenant0"
When I POST a REST request to URL "/q/reads" with payload
"""
{
	"filters" :{
		"branch": "Bangalore",
		"name": "ka"
	},
	"sortCriteria" :[
		{"name":"name","ascendingOrder": true}
	]
}
"""
Then the http status code is 200
And the top level code is 200
And success is true
And the REST response key "numRowsReturned" is "2"
And the REST response key "list[0].row.id" is "5"
And the REST response key "list[1].row.id" is "18"
And the REST response key "list[0].row.name" is "Akash"
And the REST response key "list[1].row.name" is "Vikas"

