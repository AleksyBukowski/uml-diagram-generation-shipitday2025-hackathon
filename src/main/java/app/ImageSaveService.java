package app;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.aisupport.AIUMLFetchingService.getUMLResponse;
import static app.ResponseFilter.filterTheResponse;
import static fetching.PlantUMLFetchingService.getDiagramAsPng;

public class ImageSaveService {
    String filePath = "C:\\Users\\albukows\\Downloads\\the7javafiles";
    String response = getUMLResponse(filePath);
    String filteredResponse = ResponseFilter.filterTheResponse(response);

    public ImageSaveService() throws IOException {
        return;
    }

    public void saveImage() {
        try {
            byte[] image = getDiagramAsPng(filteredResponse);
            try (FileOutputStream fos = new FileOutputStream("diagram.png")) {
                fos.write(image);
            }
        System.out.println("✅ Diagram saved as diagram.png");
        } catch (java.io.IOException e) {
            System.err.println("Error saving PlantUML diagram: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
