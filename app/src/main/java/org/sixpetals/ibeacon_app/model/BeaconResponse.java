package org.sixpetals.ibeacon_app.model;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.altbeacon.beacon.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public class BeaconResponse implements Serializable {
    public int id;
    public String name;
    public String beaconId;
    public String text;
    public String imageFileName;
    public Identifier getBeaconId() {
       return Identifier.parse(this.beaconId);
    }
}

