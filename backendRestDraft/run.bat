del /S /F /Q .db 

mvn clean package exec:java -Dexec.args="--spring.datasource.initialize=true"