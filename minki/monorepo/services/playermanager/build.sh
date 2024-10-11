mkdir packages
cp -a ../../packages/events packages
cp -a ../../packages/cache packages
docker build --platform linux/amd64 -t minki-playermanager .
rm -rf ./packages
