# Usa una imagen base de Tomcat 10 (o la versión que prefieras)
FROM tomcat:10-jdk17

# Elimina la aplicación predeterminada que viene en Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia el archivo WAR generado al directorio webapps de Tomcat
# Asegúrate de que el WAR generado en `target/` se llame `backendfi2.war`
COPY target/backendfi2.war /usr/local/tomcat/webapps/ROOT.war

# Expone el puerto en el que Tomcat está escuchando (8080)
EXPOSE 8080

# Define el comando de inicio para Tomcat
CMD ["catalina.sh", "run"]
