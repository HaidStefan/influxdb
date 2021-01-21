package at.fhv.bigdata;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


@SuppressWarnings("ALL")
@ApplicationScoped
public class InfluxMeasurementConsumer {

    private static final Logger LOGGER = Logger.getLogger("InfluxMeasurementConsumer");

    @Inject
    InfluxSink sink;

    @Incoming("influx-measurement")
    public void receive(String jsonString) {
        LOGGER.info(jsonString);

        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(jsonString);

            try {
                sink.writeToInfluxDB(json);
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
