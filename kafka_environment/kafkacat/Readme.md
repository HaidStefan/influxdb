#### Docker Image
    docker pull confluentinc/cp-kafkacat

#### View all Topics
    docker run --tty -rm --network cp-all-in-one_default confluentinc/cp-kafkacat kafkacat -b broker:29092 -L

#### Json Data Sample
    {
        "measurement": "<Influx_Measurement_Name>",
        "tags": {
            "meterReadingIdentifier": "3dbec499-80b8-4b31-9012-9f6c479c3228",
            "meterSerialNumber":"eba5b585-8bd3-498a-a3bc-6f8cd4b9f975",
            "meterIndividualName": "/C:/workdir/bigdata/sensor-layer/target/classes/example%5csmall%5cTA_Bad_1.txt",
            "physicalMeterElementId": "d2458a5e-c37d-42d7-8e1a-74f8eff89660",
            "physicalMeterElementName": "Merten"
        },
        "numericValue": 30.1
        "timestamp": "2018-05-17T03:30:20.866+02:00"
    }
    
    docker run --tty --network cp-all-in-one_default confluentinc/cp-kafkacat kafkacat -b broker:29092 -t generic-influx-sink -P {"measurement":"<Influx_Measurement_Name>","tags":{"meterReadingIdentifier":"3dbec499-80b8-4b31-9012-9f6c479c3228","meterSerialNumber":"eba5b585-8bd3-498a-a3bc-6f8cd4b9f975","meterIndividualName":"\/C:\/workdir\/bigdata\/sensor-layer\/target\/classes\/example%5csmall%5cTA_Bad_1.txt","physicalMeterElementId":"d2458a5e-c37d-42d7-8e1a-74f8eff89660","physicalMeterElementName":"Merten"},"numericValue":30.1,"timestamp":"2018-05-17T03:30:20.866+02:00"}