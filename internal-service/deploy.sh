sudo docker rm -f poc-internal-service
sudo docker build -t poc-internal-service .
sudo docker run -d \
                --name poc-internal-service \
                --network internal-network \
                poc-internal-service
