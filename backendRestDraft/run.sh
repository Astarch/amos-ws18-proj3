rm -rf ./.db

mvn clean package -DskipTests -Dmaven.test.skip=true exec:java -Dexec.args="--spring.datasource.initialize=true"
