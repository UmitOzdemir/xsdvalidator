
For testing please run these commands under project directory.

```
curl -F file=@"./src/test/resources/pain.001.001.03.valid.xml" http://localhost:8080/hsbc/upload/pain001
curl -F file=@"./src/test/resources/pain.001.001.03.invalid.xml" http://localhost:8080/hsbc/upload/pain001
curl -F file=@"./src/test/resources/pain.001.001.03.fatal.xml" http://localhost:8080/hsbc/upload/pain001
```