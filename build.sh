set -ex

USERNAME=bastiencarbonnier
REPOSITORY=personal-project

IMAGE=splinter
docker build -t $USERNAME/$REPOSITORY/$IMAGE:latest .