import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
    private static ArrayList<ArrayList<Point>> clusters = new ArrayList<ArrayList<Point>>();
    private static ArrayList<Point> Given = new ArrayList<Point>(), centroids = new ArrayList<Point>();
    private static int k = 0;
    private static Scanner sc;


    private static void workItOut(){//this is the actual work
        boolean continueReading;
        do {//first asking about number of clusters
            continueReading = false;//sooo asking until k is valid ... obvious work
            System.out.print("please enter K : ");
            try {
                k = Integer.parseInt(sc.nextLine());
                if (k > Given.size()) {
                    System.out.println("k cannot be greater than the number of points in data");
                    continueReading = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("please enter a number");
                continueReading = true;
            }
        } while (continueReading);
        DistanceCalculationMethod method = DistanceCalculationMethod.Euclidienne;
        int p = 1;// in case of minkowski
        
        System.out.println("choose which are your initial centroids");
        int i = 1;
        for (Point point : Given) {
            //listing all points of the dataset so the user can choose k of them
            //(i could've let him choose any k points but it always is better to choose from the dataset)
            System.out.println(i + ") " + point);
            i++;
        }
        for (int j = 0; j < k; j++) {//letting the user choose the centroids
            do {
                continueReading = false;
                try {
                    System.out.println("give centroid number " + (j + 1) + " : ");
                    Point point = Given.get(Integer.parseInt(sc.nextLine()) - 1);
                    if (centroids.contains(point))
                        j--;
                    else
                        centroids.add(point);
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.println("please enter a right number");
                    continueReading = true;
                }
            } while (continueReading);
        }
        do {
            continueReading = false;
            i = 1;
            System.out.println("choose the method you want");//asking for the distance method as long as the answer is invalid
            for (DistanceCalculationMethod dcm : DistanceCalculationMethod.values()) {//letting him choose the distance measuring method
                System.out.println(i + ") " + dcm);
                i++;
            }
            try {
                method = DistanceCalculationMethod.values()[Integer.parseInt(sc.nextLine().replace("\\s", "")) - 1];
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                continueReading = true;
            }
        } while (continueReading);
        if (method == DistanceCalculationMethod.Minkowski) {//if Minkowski choosed we need to ask abt p and yes usual asking stuff
            do {
                System.out.println("give p to specify Minkowski degree : ");
                continueReading = false;
                try {
                    p = Integer.parseInt(sc.nextLine().replace("\\s", ""));
                    if (p < 1) {
                        System.out.println("p cannot be less than 1");
                        continueReading = true;
                    }
                } catch (NumberFormatException e) {
                    continueReading = true;
                }
            } while (continueReading);
        }
        sc.close();//you're all set user k_means work starts here!

        ArrayList<Point> previousCentroids = null;//this points to the previous cycle's centroids

        int iterationNumber = 1;//counting number of iterations as mentioned in the given but it wasn't mentioned to actually output it idk y
        while (true) {//always looping until broken
            // initializing clusters
            clusters.clear();
            for (Point point : centroids) {
                clusters.add(new ArrayList<>());// index of centroid in centroids is the same of his cluster in clusters
            }//reinitializing current clusters to k clusters in the array list to actually refill them in the following code

            for (Point point : Given) {//for every point in the dataset
                double minDistance;//read the first comment after this if else mess
                if (method.equals(DistanceCalculationMethod.Euclidienne))
                        minDistance = Math.sqrt((point.x - centroids.get(0).x) * (point.x - centroids.get(0).x)
                                + (point.y - centroids.get(0).y) * (point.y - centroids.get(0).y));
                else if (method.equals(DistanceCalculationMethod.Manhattan))
                    minDistance = Math.abs(point.x - centroids.get(0).x) + Math.abs(point.y - centroids.get(0).y);
                else
                    minDistance = Math.pow(
                                            Math.pow(Math.abs(point.x - centroids.get(0).x), p)
                                          + Math.pow(Math.abs(point.y - centroids.get(0).y), p)
                                        , 1 / (double)p);
                //minDistance initialized to the distance between the point and the first centroid
                Point centroidChosen = centroids.get(0);
                //first the first centroid is the chosen centroid as the min distance is first calculated with the first centroid
                for (Point centroid : centroids) {//for every centroid i am looking for the minimum distance with  the current point
                    double distance = 0;
                    if (method.equals(DistanceCalculationMethod.Euclidienne))
                        distance = Math.sqrt((point.x - centroid.x) * (point.x - centroid.x)
                                + (point.y - centroid.y) * (point.y - centroid.y));
                    else if (method.equals(DistanceCalculationMethod.Manhattan))
                        distance = Math.abs(point.x - centroid.x) + Math.abs(point.y - centroid.y);
                    else
                        distance = Math.pow(Math.pow(Math.abs(point.x - centroid.x), p)
                                + Math.pow(Math.abs(point.y - centroid.y), p), 1 / (double)p);
                    if (distance < minDistance) {//actually setting the centroid with minimum distance
                        minDistance = distance;
                        centroidChosen = centroid;
                    }
                }//here the centroid with minimum distance to the current point is set
                //we should then add that point to cluster of that centroid
                clusters.get(centroids.indexOf(centroidChosen)).add(point);// index of centroid in centroids is the same of his cluster in clusters
            }//after filling every point in its cluster
            centroids.clear();//we clear current centroid array list
            for (int j = 0; j < k; j++) {//here we recalculate centroids
                ArrayList<Point> cluster = clusters.get(j);
                double sumX = 0;
                double sumY = 0;
                for (Point point : cluster){
                    sumX += point.x;
                    sumY += point.y;
                }//usual sum of x and why in one cluster
                centroids.add(new Point(sumX/cluster.size(), sumY/cluster.size()));
                //sum over size which gives the average x and y are the coordinates of the cluster's new centroid
            }
            iterationNumber++;//everything's done we should increment the number of iterations
            if (previousCentroids != null && SameCentroids(previousCentroids , centroids))
                //checking if we should break the loop and that is when the current centroids are the same as the ones before
                break;
            previousCentroids = new ArrayList<>();
            previousCentroids.addAll(centroids);//previous centroids for the next iteration are the current centroids...
        }
        System.out.println("the algo took "+iterationNumber+" iterations");
        for (ArrayList<Point> cluster : clusters) {//finally printing clusters come in nice view and structure
            System.out.print("Cluster "+(clusters.indexOf(cluster) + 1)+" : [");
            for (Point point : cluster) {
                System.out.print(point);
                if(cluster.indexOf(point) != cluster.size() - 1)
                    System.out.print(", ");
            }
            System.out.println(']');
        }
    }
    public static void getParamsFrom_file(java.io.File file){//gets dataset from the file created from geogebra java class
        try{
            Scanner scf = new Scanner(file);
            boolean continueReading;
            do {
                String input = scf.nextLine();
                continueReading = !input.toLowerCase().equals("end");
                if (continueReading) {
                    input = input.replaceAll("\\s", "");
                    String[] xAndY = input.split(",");
                    try {
                        Point point = new Point(Double.parseDouble(xAndY[0]), Double.parseDouble(xAndY[1]));
                        if (!Given.contains(point))
                            Given.add(point);
                        else
                            System.out.println("already in");
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        System.out.println("invalid input");
                    }
                }
            } while (continueReading);
            scf.close();
        }catch(Exception e){

        }
    }
    private static void getParamsFrom_stdIn(){//getting dataset points from stdin doesn't rly need comments does it!!
        boolean continueReading;
        System.out.println("Enter end to stop giving data");
        do {
            System.out.print("Enter Data where 1 point => x , y : ");
            String input = sc.nextLine();
            continueReading = !input.toLowerCase().equals("end");
            if (continueReading) {
                input = input.replaceAll("\\s", "");
                String[] xAndY = input.split(",");
                try {
                    Point point = new Point(Double.parseDouble(xAndY[0]), Double.parseDouble(xAndY[1]));
                    if (!Given.contains(point))
                        Given.add(point);
                    else
                        System.out.println("already in");
                } catch (NumberFormatException | IndexOutOfBoundsException e) {
                    System.out.println("invalid input");
                }
            }
        } while (continueReading);
    }
    private static boolean SameCentroids(ArrayList<Point> centroids1,ArrayList<Point> centroids2){
        //this is called to stop the loop of changing centroids when centroids of the loop before is the same as the current one
        if(centroids1.size() != centroids2.size())//no reason for this to happen but it's just logic that's nice to have
            return false;
        for (int i = 0; i < centroids1.size(); i++)
            if(!centroids1.get(i).equals(centroids2.get(i))){
                return false;
            }
        return true;
    }
    public static void main(String[] args) throws Exception {
        sc = new Scanner(System.in);
        //i have two modes one if it was run by geogebra extracted points and the other takes input from geogebra
        if(args == null)//if not run from user directly (called from geogebra class)
        {
            Path path = Paths.get("./points_element_in_page_Points.txt");
            getParamsFrom_file(new java.io.File(path.toAbsolutePath().toString()));
        }
        else
            getParamsFrom_stdIn();
        workItOut();
        sc.close();
    }
}
//if you have questions i hope you contact me anywhere