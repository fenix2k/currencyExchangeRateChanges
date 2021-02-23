FROM ubuntu:latest

RUN apt-get update && apt-get upgrade -y
RUN apt install openjdk-11-jdk -y
RUN java -version

COPY build/libs/currencyExchangeRateChanges-0.0.1.jar /usr/local/src/currencyExchangeRateChanges-0.0.1.jar

WORKDIR /usr/local/src/

RUN jar -xf currencyExchangeRateChanges-0.0.1.jar

CMD java org.springframework.boot.loader.JarLauncher
