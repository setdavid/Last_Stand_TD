import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

//Based on pseudo-code from A* algorithm page of Wikipedia!

public class PathFinder {
    private Tile[][] tileMap;
    private final int rowDimension;
    private final int colDimension;

    private final String MOVEMENT_TYPE = "nondiagonal";
    private final boolean CUT_CORNERS = false;

    private Collection<Tile> openSet;
    private Collection<Tile> closedSet;

    private final Tile startNode;
    private final Tile targetNode;

    private LinkedList<Tile> resultingPath = null;

    public PathFinder(Tile[][] tileMap, Tile startNode, Tile targetNode) {
        this.tileMap = tileMap;
        this.rowDimension = tileMap.length;
        this.colDimension = tileMap[0].length;

        this.startNode = startNode;
        this.targetNode = targetNode;

        this.openSet = new HashSet<Tile>();
        this.closedSet = new HashSet<Tile>();
        this.openSet.add(startNode);

        startNode.setGScore(0);
        startNode.setFScore(euclideanHFunc(startNode, targetNode));
    }

    public void reset() {
        for (int r = 0; r < tileMap.length; r++) {
            for (int c = 0; c < tileMap[0].length; c++) {
                tileMap[r][c].reset();
            }
        }

        resultingPath = null;

        this.openSet = new HashSet<Tile>();
        this.closedSet = new HashSet<Tile>();
        this.openSet.add(startNode);

        startNode.setGScore(0);
        startNode.setFScore(euclideanHFunc(startNode, targetNode));
    }

    private double euclideanHFunc(Tile node, Tile targetNode) {
        return 100 * Math.sqrt(Math.pow(Math.abs(node.getCol() - targetNode.getCol()), 2)
                + Math.pow(Math.abs(node.getRow() - targetNode.getRow()), 2));
    }

    public void startPF() {
        boolean continueLoop = true;
        while (!openSet.isEmpty() && continueLoop) {
            Tile currentNode = lowestFScoreSearch();

            if (currentNode == null) {
                continueLoop = false;
                failure("interrupted");
            } else {
                openSet.remove(currentNode);
                closedSet.add(currentNode);

                if (currentNode == targetNode) {
                    continueLoop = false;
                    this.resultingPath = reconstructPath(startNode, currentNode);
                } else {
                    analyzeNeighbors(currentNode, targetNode, MOVEMENT_TYPE, CUT_CORNERS);

                    if (openSet.isEmpty()) {
                        continueLoop = false;
                        failure("no path");
                    }
                }
            }
        }

    }

    private Tile lowestFScoreSearch() {
        double lowestFScore = Integer.MAX_VALUE;
        Tile lowestFScoreNode = null;
        for (Tile t : openSet) {
            double f = t.getFScore();

            if (f != Integer.MAX_VALUE) {
                if (f < lowestFScore) {
                    lowestFScore = f;
                    lowestFScoreNode = t;
                } else if (f == lowestFScore) {
                    if (t.getGScore() < lowestFScoreNode.getGScore()) {
                        lowestFScoreNode = t;
                    }
                }
            } else {
                failure("interrupted");
            }
        }

        return lowestFScoreNode;
    }

    private void analyzeNeighbors(Tile currentNode, Tile targetNode, String movementType, boolean cutCorners) {
        for (int row = currentNode.getRow() - 1; row <= currentNode.getRow() + 1; row++) {
            for (int col = currentNode.getCol() - 1; col <= currentNode.getCol() + 1; col++) {
                if ((row >= 0 && row < rowDimension) && (col >= 0 && col < colDimension)
                        && !(row == currentNode.getRow() && col == currentNode.getCol())
                        && (!closedSet.contains(tileMap[row][col])) && (tileMap[row][col].getType() != "block")) {
                    if (moveRestrictions(movementType, cutCorners, row, col, currentNode, tileMap)) {
                        Tile neighborNode = tileMap[row][col];
                        double potentialGScore = currentNode.getGScore() + euclideanHFunc(currentNode, neighborNode);

                        if (potentialGScore < neighborNode.getGScore()) {
                            neighborNode.setCameFrom(currentNode);
                            neighborNode.setGScore(potentialGScore);
                            neighborNode.setFScore(potentialGScore + euclideanHFunc(neighborNode, targetNode));

                            // global.updateJS.fScoreDrawUpdate(neighborNode);
                        }

                        if (!openSet.contains(neighborNode)) {
                            openSet.add(neighborNode);
//                            neighborNode.setInOpenSet(true);
                        }

//                        global.interactionsJS.setNodesAnalyzed(global.interactionsJS.nodesAnalyzed + 1);
                    }
                }
            }
        }

    }

    private boolean moveRestrictions(String movementType, boolean cutCorners, int row, int col, Tile currentNode,
            Tile[][] tileMap) {
        boolean mtAllowed = false;
        boolean ccAllowed = false;

        int[] cornerLocation = locatedOnCorner(row, col, currentNode);

        if (movementType == "diagonal") {
            mtAllowed = true;

            if (cutCorners) {
                ccAllowed = true;
            } else {
                boolean ccAllowedY = false;
                boolean ccAllowedX = false;

                if (cornerLocation[2] < 2) {
                    ccAllowed = true;
                } else {
                    if (cornerLocation[0] == 1) {
                        if (tileMap[row + 1][col].getType() != "block") {
                            ccAllowedY = true;
                        } else {
                            ccAllowedY = false;
                        }
                    } else if (cornerLocation[0] == -1) {
                        if (tileMap[row - 1][col].getType() != "block") {
                            ccAllowedY = true;
                        } else {
                            ccAllowedY = false;
                        }
                    }

                    if (cornerLocation[1] == -1) {
                        if (tileMap[row][col + 1].getType() != "block") {
                            ccAllowedX = true;
                        } else {
                            ccAllowedX = false;
                        }
                    } else if (cornerLocation[1] == 1) {
                        if (tileMap[row][col - 1].getType() != "block") {
                            ccAllowedX = true;
                        } else {
                            ccAllowedX = false;
                        }
                    }

                    if (ccAllowedX && ccAllowedY) {
                        ccAllowed = true;
                    } else {
                        ccAllowed = false;
                    }
                }
            }
        } else if (movementType == "nondiagonal") {
            ccAllowed = true;

            if (cornerLocation[2] < 2) {
                mtAllowed = true;
            } else {
                mtAllowed = false;
            }
        }

        return mtAllowed && ccAllowed;

    }

    private int[] locatedOnCorner(int row, int col, Tile currentNode) {
        int[] locationInfo = new int[3];
        int numItems = 0; // 2 if on corner and 1 if not

        if (row == currentNode.getRow() - 1) {
            locationInfo[0] = 1; // "top"
            numItems++;
        } else if (row == currentNode.getRow() + 1) {
            locationInfo[0] = -1; // "bottom";
            numItems++;
        }

        if (col == currentNode.getCol() - 1) {
            locationInfo[1] = -1; // "left";
            numItems++;
        } else if (col == currentNode.getCol() + 1) {
            locationInfo[1] = 1; // "right";
            numItems++;
        }

        locationInfo[2] = numItems;

        return locationInfo;
    }

    private LinkedList<Tile> reconstructPath(Tile startNode, Tile currentNode) {
        LinkedList<Tile> totalPath = new LinkedList<Tile>();
        Tile targetNode = currentNode;

        while (currentNode != startNode) {
            if (currentNode != null) {
                reconstructUpdate(currentNode, totalPath);

                currentNode = currentNode.getCameFrom();
            } else {
                return resultingPath = null;
            }
        }

        reconstructUpdate(currentNode, totalPath);

        return totalPath;
    }

    public void reconstructUpdate(Tile currentNode, LinkedList<Tile> totalPath) {
        if (closedSet.contains(currentNode) && !openSet.contains(currentNode)) {
            totalPath.addFirst(currentNode);
        }
    }

    private void failure(String reason) {
        String explanation = "";

        if (reason == "no path") {
            explanation = "no path found";
        } else {
            explanation = "simulation interrupted";
        }

        resultingPath = null;
        System.out.println(explanation);
    }

    public LinkedList<Tile> getResultingPath() {
        return this.resultingPath;
    }

    public static void printPath(LinkedList<Tile> path) {
        if (path != null) {
            Iterator<Tile> iterator = path.listIterator();

            while (iterator.hasNext()) {
                Tile t = iterator.next();
                System.out.println("(" + t.getCol() + ", " + t.getRow() + ")");
            }
        } else {
            System.out.println("Null Path");
        }
    }

}