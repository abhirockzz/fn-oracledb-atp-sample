FROM fnproject/fn-java-fdk-build:jdk9-1.0.63

COPY ojdbc8.jar .
#deploy it to /function/repo. This location is referenced in <repository> in the pom.xml
RUN mvn deploy:deploy-file -Durl=file:///function/repo -Dfile=ojdbc8.jar -DgroupId=com.oracle.jdbc -DartifactId=ojdbc8 -Dversion=12.2.0.1 -Dpackaging=jar