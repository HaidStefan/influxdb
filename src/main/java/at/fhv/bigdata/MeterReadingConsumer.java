package at.fhv.bigdata;

import at.fhv.bigdata.influx.MeterReadingInflux;
import at.fhv.bigdata.influx.Tags;
import org.apache.avro.generic.GenericRecord;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@ApplicationScoped
public class MeterReadingConsumer {

    private static final Logger LOGGER = Logger.getLogger("MeterReadingConsumer");

    @Incoming("new-meterreadings")
    @Outgoing("meterreading-influx4")
    public MeterReadingInflux receive(GenericRecord record) {
        LOGGER.info(record);

        return new MeterReadingInflux(
                "meterreading-influx",
                ((GenericRecord) record.get("meterReadingIdentifier")).get(0).toString(),
                ((GenericRecord)((GenericRecord) record.get("forMeter")).get("serialNumber")).get(0).toString(),
                ((GenericRecord)((GenericRecord) record.get("forMeter")).get("meterIndividualName")).get(0).toString(),
                ((GenericRecord)((GenericRecord)((GenericRecord) record.get("forMeter")).get("ofType")).get("physicalMeterElementId")).get(0).toString(),
                ((GenericRecord)((GenericRecord)((GenericRecord) record.get("forMeter")).get("ofType")).get("physicalMeterElementId")).get(0).toString(),
                (Double)((GenericRecord) record.get("meterReadingValue")).get(0)

        );
    }
}
