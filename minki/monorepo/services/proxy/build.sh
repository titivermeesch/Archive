cp ../../plugins/proxyfranz/build/libs/proxyfranz*.jar ./plugins
docker build --platform linux/amd64 -t minki-proxy .
