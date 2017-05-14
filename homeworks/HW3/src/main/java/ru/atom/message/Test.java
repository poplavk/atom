package ru.atom.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.atom.model.Temporary;

/**
 * Created by mkai on 5/9/17.
 */
public class Test {
    public String direction;

    public Test(@JsonProperty("direction") String str){
        direction = str;
    }
}
