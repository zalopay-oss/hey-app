#!/usr/bin/env bash
docker run -v hey-redis-data:/data --name hey-redis -d redis  --appendonly yes

cd hey-backend
docker rmi Image hey-backend:1.0
docker build -t hey-backend:1.0 .
docker run --rm -d -p 8080:8080 -p 8090:8090 -v hey-backend-log:/usr/src/app/log -e "env=production" --link hey-redis:redis --name hey-backend-app  hey-backend:1.0
echo 'Back end Done'
cd ..

cd hey-frontend
docker rmi Image hey-frontend:1.0
docker build -t hey-frontend:1.0 .
docker run --rm -d -v ${PWD}:/usr/src/app -p 3000:3000 --name hey-frontend-app hey-frontend:1.0
cd ..

