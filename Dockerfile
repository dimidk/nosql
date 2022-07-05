FROM openjdk:11
ADD target/nosql-0.0.1-SNAPSHOT.jar nosql-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","nosql-0.0.1-SNAPSHOT.jar"]