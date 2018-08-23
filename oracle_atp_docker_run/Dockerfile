FROM fnproject/fn-java-fdk:jdk9-1.0.63

WORKDIR /function

#Oracle ATP client credentials contents
COPY cwallet.sso /function/cwallet.sso
COPY ewallet.p12 /function/ewallet.p12
COPY ojdbc.properties /function/ojdbc.properties
COPY keystore.jks /function/keystore.jks
COPY sqlnet.ora /function/sqlnet.ora
COPY tnsnames.ora /function/tnsnames.ora
COPY truststore.jks /function/truststore.jks