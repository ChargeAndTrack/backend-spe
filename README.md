# backend-spe

ChargeAndTrack backend for the SPE project.

## How to use

Follow these steps in order to run the backend:
- clone the repository or downlaod the latest release;
- go to the root directory (`/backend-spe`);
- in order to setup the `.env` file needed for the _jvmapp_ you can alternatively:
    - run the command`./setup_env.sh`
    - rename the `jvmapp/.env.example` file as `jvmapp/.env` and replace the `JWT_SECRET` value with a random string generated for example by:
        - `node -e "console.log(require('crypto').randomBytes(32).toString('hex'))"`
        - `head -c 32 /dev/urandom | xxd -p -c 64`
- replace the `HF_SECRET` in the `jvmapp/.env` file with your Hugging Face token;
- run the following commands:
    ```
    $ docker compose build
    $ docker compose up
    ```

### How to run tests

To run the tests, while the system is running, simply execute the following command:
```
$ ./gradlew clean test
```

