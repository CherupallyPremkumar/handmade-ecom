
# Pre-requisites for running this Makefile
# Make sure that git is installed
# install maven (mvn)
# install java (of course)

# In this makefile all the goals need to be documented with a line above that
# should start with 2 #s. Internal goals must have only one # so that they
# dont show up in the help below.
# A PHONY fake goal is required to make sure that Make does not expect a file
# with the name of the goal in the file system
# The git describe command below does not emit commit hash tags. 
# This gives flexibility to handle snapshots. For snapshots a new tag is not necessary
# this of course means that SNAPSHOT releases are flaky since we dont have tags to make them
# predictable

export version := $(shell git describe --tag --abbrev=0 2> /dev/null)
.DEFAULT_GOAL := help
ifndef version
	override version = 0.0.1-SNAPSHOT
endif

## build: Build the executable with the version extracted from GIT
.PHONY: build
build:
	mvn -Drevision=$(version) install

## it: Perform an integration test (generates Service Registry and Open API files in build-package/target folder)
.PHONY: it
it:
	mvn -Drevision=$(version) -DperformIt=true install

## test-script: Test the executable with a curl script for a quick validation
.PHONY: test-script
test-script:
	scripts/curl-scripts.sh 
	
## deploy: Deploys the executable with the version extracted from GIT
.PHONY: deploy
deploy:
	mvn -Drevision=$(version) deploy

## clean: Clean all previous builds
.PHONY: clean
clean:
	mvn -Drevision=$(version) clean

## run: Run the executable using the spring-boot plugin
.PHONY: run
run: 
	cd build-package && mvn -Drevision=$(version) spring-boot:run

# check-tag: checks if the tag name has been passed as a parameter to the "make" command
# This is internally used by the other goals and should not be invoked by the end user. Hence it 
# should not show up in the documentation
.PHONY: check-tag
check-tag:
	@ if ! test -n "$(tag)"; then echo "tag needs to be specified. Use 'make $(MAKECMDGOALS) tag=<tagname>'"; exit 1 ; fi

## tag: Tag the source with tag name xxx. Use "make tag tag=<tagname>". Creates an annotated tag in git
.PHONY: tag
tag: check-tag
	git tag -a -m $(tag) $(tag)
	@echo "Created a  tag $(tag) for the release. Now you can run 'make build'"

## create-hotfix: Creates a hotfix branch for tag xxx. Use it like "make create-hotfix tag=<tagname>". 
.PHONY: create-hotfix
create-hotfix: check-tag
	git checkout -b b$(tag) $(tag)
	@echo "Created a hotfix branch b$(tag) and checked it out for you"

## increment-major: Increment the Major version number
.PHONY: increment-major
increment-major:
	@scripts/increment-tag.sh major

## increment-minor: Increment the Minor version number
.PHONY: increment-minor
increment-minor:
	@scripts/increment-tag.sh minor

## increment-patch: Increment the Patch version number
.PHONY: increment-patch
increment-patch:
	@scripts/increment-tag.sh patch

## create-hotfix-latest: Creates a hotfix for the latest tag
.PHONY: create-hotfix-latest
create-hotfix-latest:
	@scripts/make-hotfix-branch.sh


## find-latest-local-tag: Finds the latest tag for this project
.PHONY: find-latest-local-tag
find-latest-local-tag:
	@git describe | cut -d- -f1

## merge-master: Merge the hotfix branch with master. Before this make sure that the hotifx has been tagged and released
.PHONY: merge-master
merge-master:
	@scripts/merge-hotfix-to-master.sh

## list-local-tags: List all the tags in origin
.PHONY: list-local-tags
list-local-tags:
	@git tag

## list-origin-tags: List all the tags in origin
.PHONY: list-origin-tags
list-origin-tags:
	@git ls-remote --tags origin | grep -v '\}' | cut -d/ -f3

## delete-origin-tag: Delete a tag at the origin
.PHONY: delete-origin-tag
delete-origin-tag: check-tag
	git push --delete origin $(tag)

## delete-local-tag: Delete a local tag
.PHONY: delete-local-tag
delete-local-tag: check-tag
	git tag -d $(tag)

## help: type for getting this help
.PHONY: help
help: Makefile
	@echo
	@echo " Choose a command to run :"
	@echo
	@sed -n 's/^##//p' $< | column -t -s ':' |  sed -e 's/^/ /'
	@echo
