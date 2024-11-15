#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname ${ABSPATH})
source ${ABSDIR}/profile.sh

REPOSITORY=/home/ec2-user/app
PROJECT_NAME=LoginWebService

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR NAME: $JAR_NAME"

echo "> JAR에 실행권한 추가"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

IDLE_PROFILE=$(find_idle_profile)

echo "> $JAR_NAME를 profile=$IDLE_PROFILE로 실행합니다."

nohup java -jar \
    -Dspring.config.location=/home/ec2-user/app/application-real-db.yml,/home/ec2-user/app/application-$IDLE_PROFILE.yml,/home/ec2-user/app/application-oauth.yml,/home/ec2-user/app/application-redis.yml,/home/ec2-user/app/application-mail.yml \
    -Dspring.profiles.active=$IDLE_PROFILE \
    $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
