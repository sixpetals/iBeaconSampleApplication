package org.sixpetals.ibeacon_app.model;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by takuma.sugimoto on 2015/08/05.
 */
public class BeaconResponseSet
{

    private ObjectMapper objectMapper = null;
    private JsonFactory jsonFactory = null;
    private JsonParser jp = null;
    private ArrayList<BeaconResponse> responses = null;
    private BeaconResponses res = null;

    public BeaconResponseSet()
    {
        objectMapper = new ObjectMapper();
    }

    public void init(InputStream is)
    {
        try
        {
            res = objectMapper.readValue(is, BeaconResponses.class);
            responses = res.get("beaconResponses");
        }
        catch (JsonParseException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<BeaconResponse> findAll()
    {
        return responses;
    }

    public BeaconResponse findById(int id)
    {
        return responses.get(id);
    }
}
