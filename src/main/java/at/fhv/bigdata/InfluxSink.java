package at.fhv.bigdata;

import at.fhv.bigdata.influx.MeterReadingInflux;
import net.minidev.json.JSONObject;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.jboss.logging.Logger;

import javax.inject.Singleton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Singleton
public class InfluxSink {

    private static final String INFLUX_DB_URL = "http://localhost:8086";
    private static final String INFLUX_DB_DATABASE = "influx";

    private static final Logger LOGGER = Logger.getLogger("InfluxSink");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private final InfluxDB influxDB;

    public InfluxSink() {
        influxDB = InfluxDBFactory.connect(INFLUX_DB_URL);

        Pong response = influxDB.ping();
        if (response.getVersion().equalsIgnoreCase("unknown")) {
            LOGGER.error("Error pinging server.");
        }

        influxDB.setDatabase(INFLUX_DB_DATABASE);
    }

    void sendMeterReadingInflux(MeterReadingInflux meterReading) throws ParseException {
        Point p = Point.measurement(meterReading.getMeasurement())
                .time(sdf.parse(meterReading.getTime()).getTime(), TimeUnit.MILLISECONDS)
                .tag("meterReadingIdentifier", meterReading.getMeterReadingIdentifier())
                .tag("meterSerialNumber", meterReading.getMeterSerialNumber())
                .tag("meterIndividualName", meterReading.getMeterIndividualName())
                .tag("physicalMeterElementId", meterReading.getPhysicalMeterElementId())
                .tag("physicalMeterElementName", meterReading.getPhysicalMeterElementName())
                .addField("numericValue", meterReading.getNumericValue())
                .build();

        LOGGER.info("Sent to influx: " + p.toString());
        influxDB.write(p);
    }

    void writeToInfluxDB(JSONObject jsonObject) throws ParseException {
        JSONObject tags = ((JSONObject) jsonObject.get("tags"));

        //Tags
        Map<String, String> tagsMap = new HashMap<>();
        tags.forEach((key, tag) -> tagsMap.put(key, (String) tag));

        //Fields
        Map<String, Object> fieldsMap = new HashMap<>();
        jsonObject.forEach(fieldsMap::put);

        fieldsMap.remove("time");
        fieldsMap.remove("measurement");
        fieldsMap.remove("tags");

        //Temp
        int id = -1;

        // Workaround if room is empty
        if (jsonObject.getAsString("room") == null)
            return;

        // Workaround: Map raw values into one hot encoding
        switch (jsonObject.getAsString("room")) {
            case "Schlafen":
                id = 1;
                fieldsMap.put("Schlafen", 1);
                fieldsMap.put("Wohnen", 0);
                fieldsMap.put("Bad", 0);
                break;
            case "Wohnen":
                id = 2;
                fieldsMap.put("Schlafen", 0);
                fieldsMap.put("Wohnen", 1);
                fieldsMap.put("Bad", 0);
                break;
            case "Bad":
                id = 3;
                fieldsMap.put("Schlafen", 0);
                fieldsMap.put("Wohnen", 0);
                fieldsMap.put("Bad", 1);
                break;
        }

        fieldsMap.put("room", id);

        Point p = Point.measurement(jsonObject.getAsString("measurement"))
                .time(sdf.parse(jsonObject.getAsString("time")).getTime(), TimeUnit.MILLISECONDS)
                .tag(tagsMap)
                .fields(fieldsMap)
                .build();

        LOGGER.info("Sent to influx: " + p.toString());
        influxDB.write(p);
    }
}
