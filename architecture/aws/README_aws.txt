######################################################################################################
######################################################################################################
############# Creating a AWS instance with Logstash & Elasticsearch ##################################
######################################################################################################
######################################################################################################

1) create instance on aws, choose a unix based system (suggestion 2 or 1), at least 8 GiB, m4.large
2) SSH to instance (click button connect, copy paste provided string to AWS CLI)
3) Install Java 8, uninstall Java 7

sudo yum install java-1.8.0

sudo yum remove java-1.7.0-openjdk



4) Install elasticsearch, configure elasticsearch to boot on instance boot



sudo rpm -i https://download.elastic.co/elasticsearch/release/org/elasticsearch/distribution/rpm/elasticsearch/2.3.3/elasticsearch-2.3.3.rpm

sudo systemctl daemon-reload

sudo systemctl enable elasticsearch.service



5) (Install cloud-aws plugin if using multiple instances) and start elasticsearch, check if cluster healthy

cd /usr/share/elasticsearch/ 

sudo bin/plugin install cloud-aws

sudo service elasticsearch start
curl localhost:9200/_cluster/health?pretty

curl 

6) Configure elasticsearch for all ports (open access), set ES port to 9200

cd '/etc/elasticsearch/'
sudo nano elasticsearch.yml

# search for network.host, change and add:

network.host: 0.0.0.0

#
# Set a custom port for HTTP:
 
http.port: 9200


7) Install logstash

sudo rpm -i https://artifacts.elastic.co/downloads/logstash/logstash-6.1.0.rpm


# Using provided startup.options file: /etc/logstash/startup.options




8 ) Configure logstash (using code provided in 'logstash.conf')

cd /etc/logstash/conf.d/
sudo nano logstash.conf

# create file logstash.conf and copy paste code from provided file 'logstash.conf', add the secret key + id, save and exit. 

9) Run logstash to index files in s3 bucket 'elasticamos'. Do this only ONCE to index all data, do not repeat this step. 
Logstash does not need to be started or run every time you use elasticsearch. This step might take some time. 

cd /usr/share/logstash 
sudo bin/logstash -f /etc/logstash/conf.d/logstash.conf

10) ERROR HANDLING: getting pipeline error that logstash is already running, kill: 


ps aux | grep logstash

sudo kill <ID1> <ID2>

11) If indexing ok : Find index for using elasticsearch

curl localhost:9200/_cat/indices?v

12) test search elasticsearch to see if everything is working correctly: 

 curl -XGET 'localhost:9200/<index>/_count?pretty' -d ' { "query" : { "query_string" : { "query" : "bitcoin" } } } '
 

13) COPY data into bucket:

s3 cp s3://gdelt-open-data/events/<eventID>.csv s3://elasticamos

 

14) DELETE data from logstash: 

curl -XDELETE 'http://localhost:9200/*'





