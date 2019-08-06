FROM fnproject/fn-java-fdk-build:jdk9-1.0.83 as build-stage
WORKDIR /function
ENV MAVEN_OPTS -Dhttp.proxyHost= -Dhttp.proxyPort= -Dhttps.proxyHost= -Dhttps.proxyPort= -Dhttp.nonProxyHosts= -Dmaven.repo.local=/usr/share/maven/ref/repository

ADD pom.xml /function/pom.xml

#add script and related XML templates
ADD setup-mvn-security.sh /function/setup-mvn-security.sh
RUN chmod a+x setup-mvn-security.sh
ADD settings.template /function/settings.template
ADD settings-security.template /function/settings-security.template

ARG MASTER_PASSWORD
ARG ORACLE_USERID
ARG ORACLE_PASSWORD
#run the script to generate XMLs
RUN ./setup-mvn-security.sh ${MASTER_PASSWORD} ${ORACLE_USERID} ${ORACLE_PASSWORD}
RUN ["mvn", "package", "dependency:copy-dependencies", "-DincludeScope=runtime", "-DskipTests=true", "-Dmdep.prependGroupId=true", "-DoutputDirectory=target", "-Dsettings.security=settings-security.xml", "--settings", "settings.xml", "--fail-never"]

ADD src /function/src
RUN ["mvn", "package"]

FROM fnproject/fn-java-fdk:jdk9-1.0.83
WORKDIR /function
COPY --from=build-stage /function/target/*.jar /function/app/

#Oracle ATP client credentials contents
COPY cwallet.sso /function/cwallet.sso
COPY ewallet.p12 /function/ewallet.p12
COPY ojdbc.properties /function/ojdbc.properties
COPY sqlnet.ora /function/sqlnet.ora
COPY tnsnames.ora /function/tnsnames.ora

CMD ["io.fnproject.example.GetEmployeeFunction::handle"]