# polyglot-app

```
./gradlew clean test bootRun

### Create new user
curl -i -H 'Content-Type: application/json' -X POST \
     -d '{"full_name":"Artur Mkrtchyan","credentials":{"email":"mkrtchyan.artur@gmail.com","password":"qwerty"}}' \
     http://localhost:8080/v1/users

### Create JWT Token / Login
curl -i -H 'Content-Type: application/json' -X POST \
     -d '{"email":"mkrtchyan.artur@gmail.com","password":"qwerty"}' \
     http://localhost:8080/v1/users/tokens

### Get current user
curl -i -H 'Content-Type: application/json' \
        -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJta3J0Y2h5YW4uYXJ0dXJAZ21haWwuY29tIiwiZXhwIjoxNDk5NjEyMDY2fQ.3Nrk0LBpTboezNgJcGZxeKe9ZE1axboRj7EMqbHmwGlSBur-O88GVg5dPDxOewqN0nNvw4-_e46lmpg-NX9qrA' \
        http://localhost:8080/v1/users/me

### Change name
curl -i -H 'Content-Type: application/json' --request PATCH \
        -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJta3J0Y2h5YW4uYXJ0dXJAZ21haWwuY29tIiwiZXhwIjoxNDk5NjEyMDY2fQ.3Nrk0LBpTboezNgJcGZxeKe9ZE1axboRj7EMqbHmwGlSBur-O88GVg5dPDxOewqN0nNvw4-_e46lmpg-NX9qrA' \
     -d '{"full_name":"Mkrtchyan Artur"}' \
        http://localhost:8080/v1/users/1

### Verify email
curl -i -H 'Content-Type: application/json' -X POST \
     -d 'f68afec2-f468-462c-909f-16a440a77619' \
     http://localhost:8080/v1/users/email-verification-request

```

