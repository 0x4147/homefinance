mvn clean install
docker build -t homefinance:1.0 .
docker run --rm --detach --name=homefinance -p 8585:8585 homefinance:1.0
