rm -rf ./target/site/jacoco

mvn test jacoco:report
