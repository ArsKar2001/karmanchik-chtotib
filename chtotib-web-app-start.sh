#!/bin/bash
# shellcheck disable=SC2126
# shellcheck disable=SC2009
COUNT_GREP=$(ps -ef | grep chtotib-web-app.war | wc -l)
echo "$COUNT_GREP"
if [ "$COUNT_GREP" -ne 2 ]
then
	cd /home/project/karmanchik-chtotib/ || exit
	java -jar web-app/rest-service/target/chtotib-web-app.war &
	echo 2
fi