import java.util.Collection;
import java.util.HashSet;

//Based on pseudo-code from A* algorithm page of Wikipedia 

public class PathFinder {
    private Tile[][] tileMap;
    private final int rowDimension;
    private final int colDimension;
    private Collection<Tile> openSet;
    private final Tile startNode;
    private final Tile targetNode;
    
//    let tileMap = global.pathfindingJS.tileMap;
//    let rowDimension = global.gridSetupJS.rowDimension;
//    let colDimension = global.gridSetupJS.colDimension;
//    let openSet;
    
    public PathFinder(Tile[][] tileMap, Tile startNode, Tile targetNode) {
        this.tileMap = tileMap;
        this.startNode = startNode;
        this.targetNode = targetNode;
        this.openSet = new HashSet<Tile>();
        this.openSet.add(startNode);
    }

    
    function aStarPathfinding(startNode, targetNode, heuristicFunc, movementType, cutCorners, timeout) {
//        global.interactionsJS.setSRunning(true);
//
//        openSet = new Array();
//
//        openSet.push(startNode);
        startNode.h = heuristicFunc(startNode, targetNode);
        startNode.fScore = startNode.h;

        startNode.setInOpenSet(true);
        startNode.gScore = 0;
        startNode.fScore = heuristicFunc(startNode, targetNode);

        whileLoop();

        function whileLoop() {
            let currentNode = lowestfScoreSearch();

            if (!currentNode) {
                return failure("interrupted");
            }

            openSet.splice(currentNode.indexInOpenSet, 1);
            currentNode.setInOpenSet(false);
            currentNode.setInClosedSet(true);

            if (currentNode === targetNode) {
                return reconstructPath(startNode, currentNode);
            } else {
                analyzeNeighbors(currentNode, targetNode, heuristicFunc, movementType, cutCorners);

                if (openSet.length != 0) {
                    return window.setTimeout(function () {
                        whileLoop();
                    }, timeout);
                } else {
                    return failure("no path");
                }
            }
        }
    }

    function lowestfScoreSearch() {
        let lowestfScore = Infinity;
        let lowestfScoreNode = null;
        for (let i = 0; i < openSet.length; i++) {

            if (!(openSet[i].fScore == Infinity)) {
                if (openSet[i].fScore < lowestfScore) {
                    lowestfScore = openSet[i].fScore;
                    lowestfScoreNode = openSet[i];
                    lowestfScoreNode.indexInOpenSet = i;
                }

                else if (openSet[i].fScore == lowestfScore) {
                    if (openSet[i].gScore < lowestfScoreNode.gScore) {
                        lowestfScoreNode = openSet[i];
                        lowestfScoreNode.indexInOpenSet = i;
                    }
                }
            } else {
                return failure("interrupted");
            }
        }

        return lowestfScoreNode;
    }

    function analyzeNeighbors(currentNode, targetNode, heuristicFunc, movementType, cutCorners) {
        for (let row = currentNode.row - 1; row <= currentNode.row + 1; row++) {
            for (let col = currentNode.col - 1; col <= currentNode.col + 1; col++) {
                if ((row >= 0 && row < rowDimension) &&
                    (col >= 0 && col < colDimension) &&
                    !(row == currentNode.row && col == currentNode.col) &&
                    (!tileMap[row][col].inClosedSet) &&
                    (tileMap[row][col].type != "block")) {
                    if (moveRestrictions(movementType, cutCorners, row, col, currentNode, tileMap)) {
                        let neighborNode = tileMap[row][col];
                        let potentialgScore = currentNode.gScore + weightFunc(currentNode, neighborNode);

                        if (potentialgScore < neighborNode.gScore) {
                            neighborNode.cameFrom = currentNode;
                            neighborNode.gScore = potentialgScore;
                            neighborNode.h = heuristicFunc(neighborNode, targetNode);
                            neighborNode.fScore = neighborNode.gScore + neighborNode.h;

                            // global.updateJS.fScoreDrawUpdate(neighborNode);
                        }

                        if (!neighborNode.inOpenSet) {
                            openSet.push(neighborNode);
                            neighborNode.setInOpenSet(true);
                        }

                        global.interactionsJS.setNodesAnalyzed(global.interactionsJS.nodesAnalyzed + 1);
                    }
                }
            }
        }
    }

    function weightFunc(currentNode, neighborNode) {
        return global.pathfindingJS.euclidianHFunc(currentNode, neighborNode);
    }

    function moveRestrictions(movementType, cutCorners, row, col, currentNode, tileMap) {
        let mtAllowed = false;
        let ccAllowed = false;

        if (movementType == "diagonal") {
            mtAllowed = true;

            if (cutCorners) {
                ccAllowed = true;
            } else {
                let cornerLocation = locatedOnCorner();
                let ccAllowedY = false;
                let ccAllowedX = false;

                if (cornerLocation[2] < 2) {
                    ccAllowed = true;
                } else {
                    if (cornerLocation[0] == "top") {
                        if (tileMap[row + 1][col].type != "block") {
                            ccAllowedY = true;
                        } else {
                            ccAllowedY = false;
                        }
                    }

                    else if (cornerLocation[0] == "bottom") {
                        if (tileMap[row - 1][col].type != "block") {
                            ccAllowedY = true;
                        } else {
                            ccAllowedY = false;
                        }
                    }

                    if (cornerLocation[1] == "left") {
                        if (tileMap[row][col + 1].type != "block") {
                            ccAllowedX = true;
                        } else {
                            ccAllowedX = false;
                        }
                    }

                    else if (cornerLocation[1] == "right") {
                        if (tileMap[row][col - 1].type != "block") {
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
        }

        else if (movementType == "nondiagonal") {
            ccAllowed = true;

            if (locatedOnCorner()[2] < 2) {
                mtAllowed = true;
            } else {
                mtAllowed = false;
            }
        }

        if (mtAllowed && ccAllowed) {
            return true;
        } else {
            return false;
        }

        function locatedOnCorner() {
            let locationInfo = new Array();
            let numItems = 0;

            if (row == currentNode.row - 1) {
                locationInfo[0] = "top";
                numItems++;
            }

            else if (row == currentNode.row + 1) {
                locationInfo[0] = "bottom";
                numItems++;
            }

            if (col == currentNode.col - 1) {
                locationInfo[1] = "left";
                numItems++;
            }

            else if (col == currentNode.col + 1) {
                locationInfo[1] = "right";
                numItems++;
            }

            locationInfo[2] = numItems;

            return locationInfo;
        }
    }

    function reconstructPath(startNode, currentNode) {
        let totalPath = new Array();
        let targetNode = currentNode;

        while (currentNode != startNode) {
            reconstructUpdate();

            currentNode = currentNode.cameFrom;
        }

        reconstructUpdate();

        global.updateJS.pathLengthUpdate(targetNode.gScore);
        global.updateJS.pathBlockLengthUpdate(totalPath.length - 1);

        global.interactionsJS.totalPath = totalPath;
        global.updateJS.toggleTravelerDrawUpdate(totalPath[global.interactionsJS.travelIndex]);

        function reconstructUpdate() {
            if (currentNode.inClosedSet && !currentNode.inOpenSet) {
                totalPath.unshift(currentNode);
                global.updateJS.reconstructDrawUpdate(currentNode, true);
            }
        }

        // travelPath(0);

        // function travelPath(travelIndex) {
        //     if (totalPath[travelIndex] == targetNode) {
        //         return;
        //     } else {
        //         global.updateJS.toggleTravelerDrawUpdate(totalPath[travelIndex]);
        //         window.setTimeout(function () {
        //             global.updateJS.toggleTravelerDrawUpdate(totalPath[travelIndex]);

        //             travelPath(travelIndex + 1);
        //         }, 150);

        //     }
        // }

        global.interactionsJS.setSRunning(false);

        return totalPath;
    }

    function failure(reason) {
        let explanation = "";

        if (reason == "no path") {
            explanation = "no path found";
        } else {
            explanation = "simulation interrupted"
        }

        console.log(explanation);
        global.updateJS.pathLengthUpdate(explanation);
        global.updateJS.pathBlockLengthUpdate(explanation);

        global.interactionsJS.setSRunning(false);
    }
}
