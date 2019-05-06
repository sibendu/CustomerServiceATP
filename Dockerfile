FROM openjdk:8
 
COPY Wallet_sddemoatp/*  /Wallet_demoatp/ 
COPY build/libs/CustomerServiceATP.jar  CustomerServiceATP.jar

EXPOSE 8080
CMD ["java","-jar","CustomerServiceATP.jar"]