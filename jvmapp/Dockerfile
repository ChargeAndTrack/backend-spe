FROM eclipse-temurin:21-jdk AS build

ENV WORKINGDIR=/root
WORKDIR ${WORKINGDIR}
RUN mkdir -p ${WORKINGDIR} && chmod 666 ${WORKINGDIR}

COPY jvmapp/ ${WORKINGDIR}/
COPY gradlew ${WORKINGDIR}/
COPY gradle/ ${WORKINGDIR}/gradle/

RUN ./gradlew clean buildFatJar --no-daemon

FROM eclipse-temurin:21-jre

ENV WORKINGDIR=/app
WORKDIR ${WORKINGDIR}
COPY --from=build /root/build/libs/*.jar ${WORKINGDIR}/

EXPOSE 3000

ENTRYPOINT ["java", "-jar","jvmapp.jar"]
