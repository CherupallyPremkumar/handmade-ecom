display_cmd=cat
which jq && display_cmd=jq # if jq is present in path use jq to format the json payload well

function execute(){
	cmd="curl $1"
	header="curl -i $1"
	echo "Command: curl $1" | tr -d '\n' | tr '\t' ' ' | tr -s ' ' # all newlines ignored. all tabs to spaces. fuse multiple spaces to one space
	echo
	echo "---------------------------------------" 
	# eval $header 2> /dev/null | sed -n '/^[A-Za-z-]*:/p' # print only lines that have a word in the beginning (word can include -)
	echo 
	eval $cmd 2> /dev/null | $display_cmd
	echo "---------------------------------------" 
}

execute localhost:8080/info 

#----
json_create='
{
	"openedBy": "USER1",
	"description": "Unable to login to my mail account"
}
'
json_assign='
 {
	"assignee": "MY-ASSIGNEE",
	"comment": "MY-ASSIGNEE-CAN-FIX-THIS"
}
'
json_resolve='
{
	"comment": "CANNOT-DUPLICATE"
}
'
json_close='
{
	"comment": "OK"
}
'

tmpfile=/tmp/tmp.$$
echo "Creating tenant with payload |$json_create| "
curl -X POST -d "${json_create}" -H 'Content-Type: application/json' localhost:8080/tenant > $tmpfile
cat $tmpfile | jq
id=$(cat $tmpfile | jq ".payload.mutatedEntity.id" | tr -d '"')
echo "Obtained ID = $id"
rm -f $tmpfile

# execute  "-X POST -d '${json_create}' -H 'Content-Type: application/json' localhost:8080/tenant"

execute  "-X PUT -d '${json_assign}' -H 'Content-Type: application/json' localhost:8080/tenant/${id}/assign"

execute  "-X PUT -d '${json_resolve}' -H 'Content-Type: application/json' localhost:8080/tenant/${id}/resolve"

execute  "-X PUT -d '${json_close}' -H 'Content-Type: application/json' localhost:8080/tenant/${id}/close"

execute "-X GET -H 'Content-Type: application/json' localhost:8080/health-check/tenantService"
