#!/bin/bash

SECRET=$(head -c 32 /dev/urandom | xxd -p -c 64)

ENV_EXAMPLE="./jvmapp/.env.example"
ENV="./jvmapp/.env"
cp $ENV_EXAMPLE $ENV

if grep -q "^JWT_SECRET=" $ENV; then
    sed -i "s/^JWT_SECRET=.*/JWT_SECRET=$SECRET/" $ENV
else
    echo "JWT_SECRET=$SECRET" >> $ENV
fi
