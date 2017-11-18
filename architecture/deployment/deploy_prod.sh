#!bin/bash
cd backendRestDraft/
tar -zcvf backendRestDraft.tar.gz backendRestDraft/
wget "https://www.dropbox.com/s/4t137xjyadtsgci/AMOS_EC2_KEY.pem"
echo password | sudo -S chmod 400 AMOS_EC2_KEY.pem #TODO change password
scp -i AMOS_EC2_KEY.pem -o BatchMode=yes -o StrictHostKeyChecking=no backendRestDraft.tar.gz ubuntu@18.216.122.218:/home/ubuntu
rm backendRestDraft.tar.gz
ssh -i AMOS_EC2_KEY.pem -o BatchMode=yes -o StrictHostKeyChecking=no 18.216.122.218 "sudo kill $(ps aux | grep 'java -jar pretrendr-' | awk '{print $2}' || true) || true; sudo rm -rf /home/ubuntu/pretrendr/backend/backendRestDraft; sudo rm /home/ubuntu/pretrendr/backend/pretrendr-0.1.0.jar; sudo mv /home/ubuntu/backendRestDraft.tar.gz /home/ubuntu/pretrendr/backend; sudo rm -rf /home/ubuntu/backendRestDraft.tar.gz; cd /home/ubuntu/pretrendr/backend; sudo tar -xvzf backendRestDraft.tar.gz; sudo rm /home/ubuntu/pretrendr/backend/backendRestDraft.tar.gz; cd /home/ubuntu/pretrendr/backend/backendRestDraft; sudo mvn compile || true; sudo mvn compile || true; sudo mvn clean install || true; sudo mv /home/ubuntu/pretrendr/backend/backendRestDraft/target/pretrendr-0.1.0.jar /home/ubuntu/pretrendr/backend/pretrendr-0.1.0.jar; sudo java -jar /home/ubuntu/pretrendr/backend/pretrendr-0.1.0.jar </dev/null &>/dev/null &"
sudo rm AMOS_EC2_KEY.pem
