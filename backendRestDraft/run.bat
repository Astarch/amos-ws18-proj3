del /S /F /Q .db 

mvn clean package -DskipTests -Dmaven.test.skip=true exec:java -Dexec.args="--spring.datasource.initialize=true --pretrendr.s3.update.onstart=false"