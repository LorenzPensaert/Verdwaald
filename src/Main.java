import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        try {
            Lost l = new Lost();
            l.start();
        } catch (Exception ex){
            System.err.println(ex);
        }
    }
}

class Lost {
    private Queue<World> worlds;

    public Lost () {
        this.worlds = new LinkedList<>();
    }

    public void start() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int _numTasks = Integer.parseInt(br.readLine());
        if(!((_numTasks >= 1) && (_numTasks <= 100))) System.err.print("1 <= numTasks <= 100");
        for(int i = 0; i < _numTasks; i++){
            String[] _rowsAndColumns = br.readLine().split(" ");
            int numRows = Integer.parseInt(_rowsAndColumns[0]);
            int numColumns = Integer.parseInt(_rowsAndColumns[1]);

            char[][] worldDescription = new char[numRows][numColumns];
            for(int j = 0; j < numRows; j++){
                worldDescription[j] = br.readLine().toCharArray();
            }

            int numRoute = Integer.parseInt(br.readLine());

            String route = br.readLine();

            if (route.length() == numRoute){
                //Create a new world and perform it's routes
                World w = new World(worldDescription, route);
                worlds.add(w);
            }
        }

        for(int i = 0; i <= _numTasks - 1; i++) {
            World w = worlds.poll();
            System.out.println(i+1 + " " + w.findRoute());
        }
    }
}

class World {
    private char[][] _worldDescription;
    private String _route;

    public World (char[][] worldDescription, String route){
        this._worldDescription = worldDescription;
        this._route = route;
    }

    public String findRoute() {
        List<Location> lstStartingPoints = findStartingPoints();
        List<Location> lstEndPoints = new ArrayList<>();
        for(Location loc : lstStartingPoints){
            Location loc2 = executeRoute(loc);
            if(loc2 != null && !lstEndPoints.contains(loc2))
                lstEndPoints.add(loc2);
        }

        if(lstEndPoints.size() == 0){
            return "ONMOGELIJK";
        }

        lstEndPoints.sort(Comparator.comparing(Location::getY).thenComparing(Location::getX));

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < lstEndPoints.size(); i++){
            if(lstEndPoints.size() - i > 1)
                sb.append(lstEndPoints.get(i).getLocation() + " ");
            else
                sb.append(lstEndPoints.get(i).getLocation());
        }
        return sb.toString();
    }

    private List<Location> findStartingPoints(){
        char _start = _route.charAt(0);
        List<Location> lstStartingPoints = new ArrayList<Location>();

        for(int i = 0; i < _worldDescription.length; i++){
            for(int j = 0; j < _worldDescription[0].length; j++){
                if(_worldDescription[i][j] == _start){
                    lstStartingPoints.add(new Location(i, j));
                }
            }
        }

        return lstStartingPoints;
    }

    private Location executeRoute(Location startingLocation) {
        Location currentLocation = startingLocation;
        for(int i = 1; i < _route.length(); i++) {
            // If this character is even, it is a feature
            if(i % 2 == 0) {
                if(_route.charAt(i) != _worldDescription[currentLocation.getX()][currentLocation.getY()])
                    return null;
            }
            // If this character is uneven, it is a movement
            else {
                switch(_route.charAt(i)){
                    case 'L': if(checkPossibleMovement(currentLocation, _route.charAt(i))) currentLocation.setY(currentLocation.getY() - 1);
                        break;
                    case 'R': if(checkPossibleMovement(currentLocation, _route.charAt(i))) currentLocation.setY(currentLocation.getY() + 1);
                        break;
                    case 'O': if(checkPossibleMovement(currentLocation, _route.charAt(i))) currentLocation.setX(currentLocation.getX() + 1);
                        break;
                    case 'B': if(checkPossibleMovement(currentLocation, _route.charAt(i))) currentLocation.setX(currentLocation.getX() - 1);
                        break;
                }
            }
        }

        return currentLocation;
    }

    private boolean checkPossibleMovement(Location currentLocation, char direction){
        if (direction == 'L') return currentLocation.getY() - 1 >= 0;
        if (direction == 'R') return currentLocation.getY() + 1 < _worldDescription[0].length;
        if (direction == 'O') return currentLocation.getX() + 1 < _worldDescription.length;
        if (direction == 'B') return currentLocation.getX() - 1 >= 0;
        return false;
    }
}

class Location {
    private int _x, _y;

    public Location (int x, int y){
        this._x = x;
        this._y = y;
    }

    public int getX () {
        return this._x;
    }

    public void setX (int x) { this._x = x; }

    public int getY () {
        return this._y;
    }

    public void setY (int y) {this._y = y; }

    public String getLocation () {
        int x = _x + 1;
        int y = _y + 1;
        return "(" + y + "," + x + ")";
    }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Location)) {
                return false;
            }
            Location other = (Location) obj;
            return this._x == other._x && this._y == other._y;
        }
}
