FROM openjdk:17.0.1
EXPOSE 8082
COPY D:/tools/keys keys
ADD build/libs/EEL_task_3.war EEL_task_3.war
CMD ["java", "-jar", "EEL_task_3.war"]