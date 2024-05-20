sudo docker rm -f poc-external-service
sudo docker build -t poc-external-service .
sudo docker run -d \
                --name poc-external-service \
                --network internal-network \
                -p 8080:8080 \
                poc-external-service
