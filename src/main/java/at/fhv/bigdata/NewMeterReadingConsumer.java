package at.fhv.bigdata;

import at.fhv.bigdata.influx.MeterReadingInflux;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.apache.avro.generic.GenericRecord;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.influxdb.dto.Point;
import org.jboss.logging.Logger;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.text.ParseException;

@SuppressWarnings("ALL")
@ApplicationScoped
public class NewMeterReadingConsumer {

    private static final Logger LOGGER = Logger.getLogger("NewMeterReadingConsumer");

    @Inject
    InfluxSink sink;

    @Incoming("new-meterreadings")
    public void receive(GenericRecord record) {
        LOGGER.info(record);

        MeterReadingInflux meterReadingInflux = new MeterReadingInflux(
                "meterreading-influx",
                record.get("readingTime").toString(),
                ((GenericRecord) record.get("meterReadingIdentifier")).get(0).toString(),
                ((GenericRecord) ((GenericRecord) record.get("forMeter")).get("serialNumber")).get(0).toString(),
                ((GenericRecord) ((GenericRecord) record.get("forMeter")).get("meterIndividualName")).get(0).toString(),
                ((GenericRecord) ((GenericRecord) ((GenericRecord) record.get("forMeter")).get("ofType")).get("physicalMeterElementId")).get(0).toString(),
                ((GenericRecord) ((GenericRecord) ((GenericRecord) record.get("forMeter")).get("ofType")).get("physicalMeterElementName")).get(0).toString(),
                (Double) ((GenericRecord) record.get("meterReadingValue")).get(0)
        );

        try {
            sink.sendMeterReadingInflux(meterReadingInflux);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
