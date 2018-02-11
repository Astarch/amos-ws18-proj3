(amos-ws18-proj3)
# Pretrendr

Pretrendr is an open source project to predict technology trends out of open data sources. It is a web-based solution to extract trend data out of the worlds broadcast, print and web news and predict if it will become a trend.

Pretrendr simplifies the identification of existing and emerging trends that reshape industries. News are linked and refined to evaluate trends in order to deliver accurate results. We provide an easy-to-use webpage where trends can be monitored and identified.

[DEMO](http://pretrendr.com)


For meeting checklist click [here](https://github.com/Astarch/amos-ws18-proj3/blob/develop/MEETINGS_CHECKLIST.md)

## Architecture

Pretrendr consists of five components:
* Frontend (main focus: user interface for the prediction and evaluation of trends)
* Backend (main focus: simplifies the communication between frontend and EMR; saves intermediate results)
* Database for user and trend management
* EMR (main focus: analyse common crawl data based on specifying settings)
* Common Crawl (data source)

The following picture shows the interaction between the five components:
![Pretrendr architecture](architecture/pretrendr_advanced.jpeg?raw=true "Pretrendr architecture")


## Staging (test / production system)
 
The system consits of three stages (hosting via AWS):
![Stages Overview](architecture/architecture_stages.pdf?raw=true "Stages Overview")
* Production / live stage 
    * Based on master branch (see section: Continuous Integration)
    * Prod-Server 1: 18.216.122.218
         * Backend
    * S3-Bucket 1: pretrendr.com
         * Frontend
* Test / staging stage 
     * Based on developer branch (see section: Continuous Integration)
     * Test-Server 1: 18.216.129.153
         * Backend
    * S3-Bucket 1: staging.pretrendr.com
         * Frontend
* Local Machine based on new feature branch

## Continuous Integration
To achieve continuous integration we set up circleci which checks firstly the build process and the JUnit tests for backend and also for frontend. If the tests are successfully proceeded and we want to merge the new git version either into master branch or develop branch, circleci will deploy the products (frontend, backend) to the following systems:
* Production / live stage based on git branch master
    * Backend will be deployed to the EC2 instance 18.216.122.218
    * Frontend will be deployed to the S3 bucket http://pretrendrfrontend.s3-website.us-east-2.amazonaws.com
* Test / Staging stage based on git branch develop
    * Backend will be deployed to the EC2 instance 18.216.129.153
    * Frontend will be deployed to the S3 bucket http://pretrendrfrontendstaging.s3-website.us-east-2.amazonaws.com


Circleci log: https://circleci.com/gh/Astarch/amos-ws18-proj3

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






