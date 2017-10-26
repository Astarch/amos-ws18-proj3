# amos-ws18-proj3
## Git Workflow (git-flow)
We will use a variation of [git-flow](http://nvie.com/posts/a-successful-git-branching-model/) to ensure we always have a working version and minimize merge conflicts. Most important are the `develop` and the `master` branch. 

<img src="https://github.com/Astarch/amos-ws18-proj3/blob/master/git-flow.png" width="400">

The `master` branch always contains a release-ready version and is also the branch where release-tags are being made (only by the release manager!).

The `develop` branch is the branch where all of the development will happen and where releases are scheduled from.

### Feature Development Workflow
To start developing a new feature please follow the next steps:
1. Start a new branch from the `develop` branch named `feature/x-y-z` where x-y-z is the feature name
2. Develop feature 
3. Merge `develop` into `feature`
4. Start a pull request on github and add atleast one reviewer!
5. When pull request is accepted merge `feature` into `develop` and delete `feature` branch


### Release Workflow
1. Start a new branch from `develop` named `release/rel-xyz` where xyz is the release version
2. Fix Bugs etc, ensure everything is working!
3. Merge `release` into `master` and back into `develop` if bugfixes have been made
4. Delete `release` branch
5. Tag `master` as release on github! 


## User Database Setup
The database for our user management is based on Redis (NoSQL database)
We are running on a single EC2 instance (AWS):

- Type: t2.micro
- OS: Ubuntu Server 16.04 LTS
- DNS: ec2-18-220-147-20.us-east-2.compute.amazonaws.com
- Redis version: 3.2.

Point of contact: *Florian*






