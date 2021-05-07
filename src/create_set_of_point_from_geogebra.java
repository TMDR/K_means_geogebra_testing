import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class create_set_of_point_from_geogebra{//this is meant to do the same as 
    public static void main(String[] args) throws Exception {
        Scanner sc =  new Scanner(System.in);
        System.out.print("give the sharable link of geogebra containing only your points.\nYou can obtain this link by opening geogebra online then opening calculator then draw your points and press the fancy share button, you'll obtain a link and voila! this is the one.\nEnter your link here please : ");
        String link = sc.nextLine();
        System.out.println("give the python path in your system after installing selenium in it by using pip install selenium ATTENTION AND UPDATE CHROME TO VERSION 90 OR CHANGE CHROMEDRIVER VERSION PLACED IN THIS FOLDER AND ITS PARENT");
        String pythonPath = sc.nextLine();//some persons like to install both python 2 and 3 in their pcs maybe even python is installed but it isn't in the
        //enviroment variables
        String os;
        do{
            System.out.println("are you running on windows, linux or mac? enter W or L or M please and it's case insensitve");
            os = sc.nextLine();//some persons like to install both python 2 and 3 in their pcs maybe even python is installed but it isn't in the path
        }while(!os.equalsIgnoreCase("w") && !os.equalsIgnoreCase("l") && !os.equalsIgnoreCase("m"));
        
        Process p;

        while((p = Runtime.getRuntime().exec(pythonPath+" ./src/seleniumGetThings_example.py "+link+" "+os.toLowerCase())).waitFor() != 0){
            //running python code to go through the link and give the div element having the points in it using selenium web driver in headless mode
            Scanner psc = new Scanner(p.getErrorStream());
            System.out.println(psc.nextLine());
            psc.close();
            System.out.println("retrying");//an error happened could be anything
        }
        Path filePath = Paths.get("./points_element_in_page.txt");//this is the path to the file created from python containg html needed to get points
        String content = Files.readString(filePath, StandardCharsets.UTF_8);//html to string
        int i;
        String s = filePath.getParent().toAbsolutePath().toString();
        s = s.substring(0,s.length()-2);//removing unnecessary \. in the end
        String newFilePath = s+"/"+filePath.getFileName().toString().substring(0, filePath.getFileName().toString().indexOf("."))+"_Points.txt";
        //just fixing its path
        PrintWriter FOS = new PrintWriter(new FileOutputStream(newFilePath));
        while((i = content.indexOf(" = ")) != -1){//writing points!!! they are the only ones preceded by " = " in the div element containg them
            content = content.substring(i+4,content.length() -1);
            String point = content.substring(0, content.indexOf(")"));
            FOS.println(point);
        }
        FOS.println("end");
        FOS.close();
        App.main(null);//Calling App with null (for it to read the file)
        sc.close();//not necessary as it is closed in App.main
    }
}