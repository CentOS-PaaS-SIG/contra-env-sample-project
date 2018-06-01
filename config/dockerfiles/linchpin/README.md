# Usage of linchpin container


## Build image
From within the linchpin directory:

```bash
docker build --file Dockerfile.linchpin --tag ci-spawner .
```

## AWS credentials

AWS credentials should be passed as environment variables when the container is run, 
named `AWS_SECRET_ACCESS_KEY` and `AWS_ACCESS_KEY_ID`.