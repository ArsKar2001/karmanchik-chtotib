#!/bin/bash
# shellcheck disable=SC2009
PID=$(ps -ef | grep telegram-bot.jar | grep -v grep | awk '{print $2}')
if [ -n "$PID" ]
then
	echo "$PID"
	kill -9 "$PID"
fi
