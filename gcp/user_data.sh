#!/bin/bash
set -e

apt-get update
apt-get install -y docker.io docker-compose-v2 git

systemctl enable docker
systemctl start docker

usermod -aG docker ubuntu

git clone ${repo_url} /app
cd /app

docker compose up --build -d order-service order-client

echo "Done" > /var/log/user_data_done.log
