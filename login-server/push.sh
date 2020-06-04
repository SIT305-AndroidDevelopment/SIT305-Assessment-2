#!/usr/bin/env bash

git add .
git commit -m "自动提交:$1"
git push origin master
ssh root@49.235.23.118 "cd /root/login-server/ && bash run.sh"