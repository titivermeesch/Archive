mkdir packages
mkdir services
cp -a ../../packages/events packages
cp -a ../gama services
cp -a ../queuemanager services
docker build --platform linux/amd64 -t minki-matchmaker .
rm -rf ./packages
rm -rf ./services
