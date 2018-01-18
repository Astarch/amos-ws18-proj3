#!bin/bash
set -e
cd ..
tar -zcvf amos-ws18-proj3.tar.gz repo/
wget "https://www.dropbox.com/s/4t137xjyadtsgci/AMOS_EC2_KEY.pem"
echo password | sudo -S chmod 400 AMOS_EC2_KEY.pem
scp -i AMOS_EC2_KEY.pem -o BatchMode=yes -o StrictHostKeyChecking=no amos-ws18-proj3.tar.gz ubuntu@18.216.129.153:/home/ubuntu
rm amos-ws18-proj3.tar.gz
echo "Step1"
ssh -i AMOS_EC2_KEY.pem -o BatchMode=yes -o StrictHostKeyChecking=no ubuntu@18.216.129.153 "sudo systemctl stop pretrendr.service; sudo rm /home/ubuntu/pretrendr/backend/pretrendr-0.1.0.jar; sudo rm /home/ubuntu/pretrendr/backend/repo/backendRestDraft/keystore.p12; sudo rm -rf /home/ubuntu/pretrendr/backend/repo; sudo rm -rf /home/ubuntu/pretrendr/backend/src; sudo rm -rf /home/ubuntu/pretrendr/backend/repo; sudo mv /home/ubuntu/amos-ws18-proj3.tar.gz /home/ubuntu/pretrendr/backend; cd /home/ubuntu/pretrendr/backend; sudo tar -xvzf amos-ws18-proj3.tar.gz; sudo rm /home/ubuntu/pretrendr/backend/amos-ws18-proj3.tar.gz; sudo rm -rf /home/ubuntu/pretrendr/backend/log; cd /home/ubuntu/pretrendr/backend/repo/backendRestDraft; sudo rm -rf /home/ubuntu/pretrendr/backend/.db"
echo "Step2"
ssh -i AMOS_EC2_KEY.pem -o BatchMode=yes -o StrictHostKeyChecking=no ubuntu@18.216.129.153 "cd /home/ubuntu/pretrendr/backend/repo/backendRestDraft; sudo mvn test jacoco:report"
echo "Step3"
ssh -i AMOS_EC2_KEY.pem -o BatchMode=yes -o StrictHostKeyChecking=no ubuntu@18.216.129.153 "cd /home/ubuntu/pretrendr/backend/repo/backendRestDraft; sudo mvn compile"
echo "Step4"
ssh -i AMOS_EC2_KEY.pem -o BatchMode=yes -o StrictHostKeyChecking=no ubuntu@18.216.129.153 "cd /home/ubuntu/pretrendr/backend/repo/backendRestDraft; sudo mvn clean install"
echo "Step5"
ssh -i AMOS_EC2_KEY.pem -o BatchMode=yes -o StrictHostKeyChecking=no ubuntu@18.216.129.153 "sleep 15; sudo mv /home/ubuntu/pretrendr/backend/repo/backendRestDraft/keystore.p12 /home/ubuntu/pretrendr/backend/"
echo "Step6"
ssh -i AMOS_EC2_KEY.pem -o BatchMode=yes -o StrictHostKeyChecking=no ubuntu@18.216.129.153 "cd /home/ubuntu/pretrendr/backend/repo/backendRestDraft; sudo mv /home/ubuntu/pretrendr/backend/repo/backendRestDraft/target/pretrendr-0.1.0.jar /home/ubuntu/pretrendr/backend; sudo mv /home/ubuntu/pretrendr/backend/repo/backendRestDraft/src /home/ubuntu/pretrendr/backend/; sudo systemctl start pretrendr.service"
sudo rm AMOS_EC2_KEY.pem
sudo rm AMOS_EC2_KEY.pem
echo "Test: Pretrendr (Backend) deployed"
