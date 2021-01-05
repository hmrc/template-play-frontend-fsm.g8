#!/usr/bin/env bash

if [[ -f ./build.sbt ]] && [[ -d ./src/main/g8 ]]; then

   mkdir -p target
   cd target
   if [[ -d .makeitg8 ]] && [[ -d .makeitg8/.git ]] ; then
        cd .makeitg8
        git pull origin master
   else
        rm -r .makeitg8
        git clone https://github.com/arturopala/make-it-g8.git .makeitg8
        cd .makeitg8
   fi

   sbt "run --noclear --source ../../target/sandbox/trader-services-route-one-frontend --target ../.. --name template-play-27-frontend-fsm --package uk.gov.hmrc.traderservices --description A+Giter8+template+for+creating+HMRC+Digital+Scala+Play+2.7+Stateful+Frontend+Microservice  -K serviceName=Trader+Services+Route+One+Frontend serviceUrlPrefix=send-documents-for-customs-check serviceTargetPort=9379 authorisedIdentifierKey=EORINumber serviceTitle=Send+Documents+For+Customs+Check authorisedServiceName=HMRC-CUS-ORG servicePrefix=Trader+Services" -Dbuild.test.command="sbt test it:test" 

else

    echo "WARNING: run the script ./update-g8.sh in the template root folder"
    exit -1

fi
