docker kill homefinance
docker image rm homefinance:1.0

mvn clean install

docker build -t homefinance:1.0 .
docker run --rm --detach --name=homefinance -p 8585:8585 homefinance:1.0

#push to docker hub
docker tag homefinance:1.0 0x4147/homefinance:1.0
docker push 0x4147/homefinance:1.0