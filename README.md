# amos-ws18-proj3

For meeting checklist click [here](https://github.com/Astarch/amos-ws18-proj3/blob/develop/MEETINGS_CHECKLIST.md)

## Architecture / Stages

The system consits of three stages:
![Stages Overview](architecture/architecture_stages.pdf?raw=true "Stages Overview")
* Production / live stage 
    * Based on master branch
    * Prod-Server 1: 18.216.122.218
         * Backend
    * S3-Bucket 1: pretrendr.com
         * Frontend
* Test/Staging stage 
     * Based on developer branch
     * Test-Server 1: 18.216.129.153
         * Backend
    * S3-Bucket 1: staging.pretrendr.com
         * Frontend
* Local Machine based on new feature branch

## Continuous Integration
### Continuous Integration Backend

To achieve continuous integration we set up a jenkins service which check the github repository every five minutes. If there is a change on github, the following shell script will be executed:

```bash
sudo rm -rf /home/ubuntu/pretrendr/backend/backendRestDraft || true
sudo mv "/var/lib/jenkins/workspace/deploy-test-system/backendRestDraft" /home/ubuntu/pretrendr/backend
cd /home/ubuntu/pretrendr/backend/backendRestDraft
sudo mvn compile
sudo mvn clean install || true
sudo rm /home/ubuntu/pretrendr/backend/pretrendr-0.1.0.jar || true
sudo mv /home/ubuntu/pretrendr/backend/backendRestDraft/target/pretrendr-0.1.0.jar /home/ubuntu/pretrendr/backend/pretrendr-0.1.0.jar
sudo rm -rf /var/lib/jenkins/workspace/* || true
cd /home/ubuntu/pretrendr/backend
sudo java -jar pretrendr-0.1.0.jar </dev/null &>/dev/null &
```

### Continuous Integration Frontend

TODO

## Git Workflow (git-flow)

**Feature Freeze:** Wednesday 23:59!

Meaning: No more merging of branches onto the `develop` branch from pull requests until the release manager created the new release branch and pushed it!.

We will use a variation of [git-flow](http://nvie.com/posts/a-successful-git-branching-model/) to ensure we always have a working version and minimize merge conflicts. Most important are the `develop` and the `master` branch. 

<img src="https://github.com/Astarch/amos-ws18-proj3/blob/master/git-flow.png" width="400">

The `master` branch always contains a release-ready version and is also the branch where release-tags are being made (only by the release manager!). **PLEASE DON'T TOUCH THIS BRANCH OTHER THAN FOR RELEASES!!!** 

The `develop` branch is the branch where all of the development will happen and where releases are scheduled from.

### Feature Development Workflow
To start developing a new feature please follow the next steps:
1. Start a new branch from the `develop` branch named `feature/x-y-z` where x-y-z is the feature name
2. Develop feature 
3. Merge `develop` into `feature`
4. Start a pull request on github (make sure to set develop as branch to merge into later!) and add atleast one reviewer!
5. When pull request is accepted merge `feature` into `develop` and delete `feature` branch


### Release Workflow
1. Start a new branch from `develop` named `release/sprint-release-xyz` where xyz is the release version
2. Fix Bugs etc, ensure everything is working!
3. Merge `release` into `master` and back into `develop` if bugfixes have been made
4. Delete `release` branch
5. Tag `master` as release on github! 






