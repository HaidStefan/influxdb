## Environment Setup

### Starting Kafka, InfluxDB and Grafana
    docker-compose up -d
    
### Verify that all Containers are running
    docker ps -a

### Install InfluxDB Plugin
    docker container exec -it connect confluent-hub install confluentinc/kafka-connect-influxdb:latest
Choose "1, Yes, Yes, Yes"

### Add Connector to Kafka
    curl -X POST -d @influxdb-sink-connector.json http://localhost:8083/connectors -H "Content-Type: application/json"