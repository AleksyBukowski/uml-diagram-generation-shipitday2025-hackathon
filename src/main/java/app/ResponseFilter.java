package app;

public class ResponseFilter {
    public static String filterTheResponse(String response) {
        String filtered = response.replaceAll("(?s)<think>.*?</think>", "");
        int start = filtered.indexOf("@startuml");
        int end = filtered.indexOf("@enduml");
        if (start != -1 && end != -1 && end > start) {
            return filtered.substring(start, end + "@enduml".length());
        } else {
            return "No valid PlantUML diagram found in the response.";
        }
    }
}
