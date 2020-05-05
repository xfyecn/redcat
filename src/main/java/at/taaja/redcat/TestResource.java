package at.taaja.redcat;


import com.google.common.collect.Lists;
import io.taaja.models.generic.Coordinates;
import io.taaja.models.generic.LocationInformation;
import io.taaja.models.message.data.update.SpatialDataUpdate;
import io.taaja.models.message.data.update.actuator.AbstractActuatorUpdate;
import io.taaja.models.message.data.update.actuator.PositionUpdate;
import io.taaja.models.message.extension.operation.OperationType;
import io.taaja.models.message.extension.operation.SpatialOperation;
import io.taaja.models.record.spatial.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("/dev")
@Produces(MediaType.APPLICATION_JSON)
public class TestResource {

    private static String errMsg = "use /dev/example/{Area|Corridor|SpatialEntity|SpatialDataUpdate|SpatialOperation|LocationInformation} as query param";

    @GET
    @Path("/example/{className}")
    public Object test(@PathParam("className") String className){

        if(className == null){
            return errMsg;
        }

        PositionUpdate positionUpdate = new PositionUpdate();
        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude(1000f);
        coordinates.setLatitude(11.12f);
        coordinates.setLongitude(46.34f);
        positionUpdate.setPosition(coordinates);
        SpatialDataUpdate spatialDataUpdate = new SpatialDataUpdate(UUID.randomUUID().toString(), positionUpdate);
        Map<String, AbstractActuatorUpdate> actuators = spatialDataUpdate.getActuators();


        Corridor corridor = new Corridor();
        corridor.setId(UUID.randomUUID().toString());
        corridor.setShape(Corridor.ShapeType.Circular);
        corridor.setValidFrom(new Date());
        corridor.setValidUntil(new Date());
        corridor.setPriority(ExtensionPriority.VERY_HIGH_PRIORITY);


        List<List<Waypoint>> wpl = new ArrayList<>();
        List<Waypoint> waypoints = new ArrayList<>();
        wpl.add(waypoints);

        Waypoint waypoint = new Waypoint();
        waypoint.setAltitude(1000f);
        waypoint.setLatitude(11.12f);
        waypoint.setLongitude(46.34f);
        waypoint.setAdditionalData("additional data");
        waypoints.add(waypoint);

        corridor.setCoordinates(wpl);
        corridor.setActuators(actuators);

        Area area = new Area();
        area.setId(UUID.randomUUID().toString());
        area.setElevation(123f);
        area.setHeight(456f);
        area.setValidFrom(new Date());
        area.setValidUntil(new Date());
        area.setPriority(ExtensionPriority.HIGH_PRIORITY);
        List<List<LongLat>> lll = new ArrayList<>();
        List<LongLat> longlats = new ArrayList<>();
        lll.add(longlats);
        LongLat longLat = new LongLat();
        longLat.setLatitude(11.11f);
        longLat.setLongitude(46.46f);
        longlats.add(longLat);
        longlats.add(longLat);
        longlats.add(longLat);
        area.setCoordinates(lll);
        area.setActuators(actuators);



        switch (className.toLowerCase()){
            case "spatialentity":
            case "area":
                return area;

            case "corridor":
                return corridor;

            case "spatialdataupdate":
                return spatialDataUpdate;

            case "spatialoperation":
                SpatialOperation spatialOperation = new SpatialOperation();
                spatialOperation.setIntersectingExtensions(Lists.newArrayList(UUID.randomUUID().toString(), UUID.randomUUID().toString()));
                spatialOperation.setTargetId(UUID.randomUUID().toString());
                spatialOperation.setOperationType(OperationType.Created);

                return spatialOperation;

            case "locationinformation":

                LocationInformation locationInformation = new LocationInformation();
                List<SpatialEntity> spatialEntities = locationInformation.getSpatialEntities();
                corridor.setActuators(null);
                spatialEntities.add(corridor);
                area.setActuators(null);
                spatialEntities.add(area);
                locationInformation.setAltitude(123f);
                locationInformation.setLatitude(456f);
                locationInformation.setLongitude(789f);

                return locationInformation;

            default:
                return errMsg;
        }
    }


}