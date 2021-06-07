#!/bin/bash
# shellcheck disable=SC2126
# shellcheck disable=SC2009
COUNT_GREP=$(ps -ef | grep telegram-bot.jar | wc -l)
echo "$COUNT_GREP"
if [ "$COUNT_GREP" -ne 2 ] 
then
	cd /home/project/karmanchik-chtotib/ || exit
	java -jar bots/telegram-bot/target/telegram-bot.jar &
	echo 2
fi
