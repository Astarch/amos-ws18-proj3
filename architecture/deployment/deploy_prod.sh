#!bin/bash
set -e

tar -zcvf backendRestDraft.tar.gz backendRestDraft/
wget "https://www.dropbox.com/s/4t137xjyadtsgci/AMOS_EC2_KEY.pem"
echo password | sudo -S chmod 400 AMOS_EC2_KEY.pem #TODO change password
scp -i AMOS_EC2_KEY.pem -o BatchMode=yes -o StrictHostKeyChecking=no backendRestDraft.tar.gz ubuntu@18.216.122.218:/home/ubuntu
rm backendRestDraft.tar.gz
ssh -i AMOS_EC2_KEY.pem -o BatchMode=yes -o StrictHostKeyChecking=no ubuntu@18.216.122.218 "sudo kill $(ps aux | grep '/usr/lib/jvm/java-8-oracle/bin/java -classpath /usr/share/maven' | awk '{print $2}' || true) || true; sudo rm -rf /home/ubuntu/pretrendr/backend/repo || true; sudo mv /home/ubuntu/backendRestDraft.tar.gz /home/ubuntu/pretrendr/backend; sudo rm -rf /home/ubuntu/backendRestDraft.tar.gz; cd /home/ubuntu/pretrendr/backend; sudo tar -xvzf backendRestDraft.tar.gz; cd /home/ubuntu/pretrendr/backend/repo; sudo rm /home/ubuntu/pretrendr/backend/backendRestDraft.tar.gz; cd /home/ubuntu/pretrendr/backend/repo/backendRestDraft; sudo sh run.sh </dev/null &>/dev/null &"
sudo rm AMOS_EC2_KEY.pem
