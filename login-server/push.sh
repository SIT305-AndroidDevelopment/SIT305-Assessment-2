#!/usr/bin/env bash

git add .
git commit -m "自动提交:$1"
git push origin master
#ssh  root@server4.guanweiming.com "cd /root/phone-manager-server/ && bash run.sh"
#ssh root@39.104.21.172 "cd /root/ipv6-server/ && bash run.sh"
ssh root@39.97.112.144 "cd /root/ipv6-server/ && bash run.sh"