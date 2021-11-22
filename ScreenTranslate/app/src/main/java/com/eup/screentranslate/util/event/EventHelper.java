package com.eup.screentranslate.util.event;

public class EventHelper {
    public enum StateChange {
        START_SCANNING,
        STOP_SCANNING,
        SWAP_FLOATTAB,
        OPEN_SETTING_APP,
        STOP_SERVICE,
        CHANGE_LANG_APP,
        SCREEN_SHOT,
        INIT_ADS,
        POST_ADS_INTER
    }
    public StateChange stateChange;
    public EventHelper(StateChange st){
        this.stateChange = st;
    }
}
