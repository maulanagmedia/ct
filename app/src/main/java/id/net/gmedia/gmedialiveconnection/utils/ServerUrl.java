package id.net.gmedia.gmedialiveconnection.utils;

public class ServerUrl {

    private static final String baseUrl = "https://erpsmg.gmedia.id/client_tools/";

    public static final String auth = baseUrl + "api/auth/login/";
    public static final String getLiveTraffic = baseUrl + "api/router/live_traffic/";
    public static final String getUpDownConnection = baseUrl + "api/router/status_up_down/";
    public static final String stopLoveConnection = baseUrl + "api/router/stop_live_traffic/";
    public static final String getLiveTorch = baseUrl + "api/router/live_client_traffic/";
    public static final String getTorchTraffic = baseUrl + "api/router/client_traffic/";
    public static final String stopTorchConnection = baseUrl + "api/router/stop_client_traffic/";

}
