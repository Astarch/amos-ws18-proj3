rm -rf ./.db

mvn clean package exec:java -Dexec.args="--spring.datasource.initialize=true"
