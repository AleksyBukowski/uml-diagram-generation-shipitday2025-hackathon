package app;

import java.io.FileOutputStream;

import static app.ResponseFilter.filterTheResponse;
import static com.aisupport.AIUMLFetchingService.getUMLResponse;
import static fetching.PlantUMLFetchingService.getDiagramAsPng;

public class Main {
    
    public static void main(String[] args) throws Exception {
        String uml = getUMLResponse("C:\\Users\\albukows\\Downloads\\the7javafiles");
        String filteredUml = filterTheResponse(uml);
        System.out.println(filteredUml);

        byte[] image = getDiagramAsPng(uml);
        // Save the file as diagram.png
        try (FileOutputStream fos = new FileOutputStream("diagram.png")) {
            fos.write(image);
        }

        System.out.println("✅ Diagram saved as diagram.png");
    }
}
