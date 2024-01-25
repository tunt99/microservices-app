#!/usr/bin/sh
# check-config-server-started.sh

apt-get update -y

yes | apt-get install curl

# Function to perform health check
healthCheck() {
    curl -s -o /dev/null -I -w "%{http_code}" http://config-server:8888/actuator/health
}

curlResult=$(healthCheck)
echo "result status code:" "$curlResult"

while [ ! "$curlResult" = "200" ]; do
  >&2 echo "Config server is not up yet!"
  sleep 2
  curlResult=$(healthCheck)
done

/cnb/lifecycle/launcher