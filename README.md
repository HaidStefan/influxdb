# kafka-influx-mapper project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `kafka-influx-mapper-1.0.0-SNAPSHOT-runner.jar` file in the `/target` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application is now runnable using `java -jar target/kafka-influx-mapper-1.0.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/kafka-influx-mapper-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html.

# Command Mode

Guide: https://quarkus.io/guides/command-mode-reference


### MeterReading-Influx Avro:

    {
        "measurement": "temperature-humidity",
        "tags": {
            "meterReadingIdentifier": "3dbec499-80b8-4b31-9012-9f6c479c3228",
            "meterSerialNumber":"eba5b585-8bd3-498a-a3bc-6f8cd4b9f975",
            "meterIndividualName": "/C:/workdir/bigdata/sensor-layer/target/classes/example%5csmall%5cTA_Bad_1.txt",
            "physicalMeterElementId": "d2458a5e-c37d-42d7-8e1a-74f8eff89660",
            "physicalMeterElementName": "Merten"
        },
        "numericValue": 30.1
    }

Converted Here: https://toolslick.com/generation/metadata/avro-schema-from-json


### Avro Class Codegen
    mvn generate-sources

### Avro Tutorials Used:
https://quarkus.io/blog/kafka-avro/
https://quarkus.io/guides/kafka

### Influxdb Notes

#### show data
    select * from "meterreading-influx"

#### delete all measurements
    DROP SERIES FROM /.*/