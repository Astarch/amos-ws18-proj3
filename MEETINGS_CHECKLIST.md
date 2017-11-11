# Project Meetings Checklist

## Meeting Schedule:
1. Meeting Preparation
    -   *Sprint Manager:*
    -   +  Create release branch from develop ("release/sprint-xx")
        +  Tag release branch with "sprint-xx-release-candidate"
    -   *New and old Product Owner*:
        +  Note release-candidate tag for sprint in release plan
        +  Ensure backlog is ready for planning
2. Old Sprint review
    -   *Sprint Manager:*
        +  Compile, Build and run Tests on release branch
    -   *Product Owner:*
        +  Walk through sprint backlog feature by feature and check fulfillment
3. Old Sprint release
    -   *Product Owner:*
        +  Decide whether release candidate should be released
    -   *Sprint Manager:*
        +  Merge release branch into master and develop
        +  Tag Master with release tag ("sprint-xx-release")
4. Old Sprint retroperspective
    - *Old Scrum Master*
        + Review sprints impediments
            * Report on progress
            * Review remaining problems
    - *New Scrum Master*
        + Ask for new impediments and take note
        + Note Impediments in impediments backlog
    - *Everyone*
        + Answer Happiness Index
5. New Sprint planning
    -   *Product Owner:*
        +  Reprioritize backlog
    -  *Software Developer*
        +  Estimate size of each backlog item -> planning poker!
        +  Commit to backlog items in sprint backlog

## Tasks:
### Sprint Manager:
Prepare and release sprint release
1. Create release branch from develop
2. fix bugs / ensure release is running
3. merge release branch into master
4. tag release
5. merge release branch into develop
6. delete release branch

# General project management informations 

## Roles

- Product Owner         (PO)
- Scrum Master          (SM)
- Software Developer    (SD)

### Product Owner (PO)

- Responsibility for Product
- Provides product vision + requirements
- Plans develeopment + tracks progress
- Clean up product and sprint backlog
- Update release plan

### Scrum Master (SM)

- Tracks and resolves impediments

### Software Developer (SD)

- Responsibility for design and implementation of product
- Estimates complexity of product features
- Organizes and allocates design and implementation tasks

## Deliverables

### 1. Process artifacts (Before team meeting)

- Snapshot of planning document

Quality Criteria:

- Cleanliness, completeness, correctness
- Usefulness

### 2. Product artifacts (between sprints / team meeting)

Release on GitHub:

- Code
- Documentation (User + Technical)

Quality Criteria:

- Cleanliness of source code
- Use of commit messages!
- Effective branching
- Correct tags and releases!
- (test coverage)!

### 3. Stand-up emails (two - three a week)

Short summery via https://uni1-happy.appspot.com 

### 4. Happiness index (once a week)

Feedback via https://uni1-happy.appspot.com 
